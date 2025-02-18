package com.example.code_challenge.controllers

import com.example.code_challenge.data.database.dto.CodeChallengeDto
import com.example.code_challenge.services.ChallengeService
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Mapping.CHALLENGE)
class ChallengeController {

    @Autowired
    lateinit var challengeService: ChallengeService

    @PostMapping("/codewars/load/{username}")
    suspend fun updateFromCodeWars(@PathVariable username: String) {
        return newSuspendedTransaction {
            challengeService.addChallengeFromSource(username)
        }
    }

    @GetMapping("/codewars/{username}/{name}", produces = ["application/json"])
    suspend fun getCodeWarsChallenge(@PathVariable username: String, @PathVariable name: String): ResponseEntity<*> {
        val challenge = newSuspendedTransaction {
            challengeService.getChallenge(username, name)
        }
        return ResponseEntity.ok(challenge)
    }

    @PostMapping("/codewars/{username}", produces = ["application/json"])
    suspend fun createCodeWarsChallenge(@PathVariable username: String, @RequestBody challengeData: String){
        val challenge = newSuspendedTransaction {
            val challenge = Json.decodeFromString<CodeChallengeDto>(challengeData)
            challengeService.createChallenge(username, challenge)
        }
    }

    @PutMapping("/codewars/{username}")
    suspend fun updateCodeWarsChallenge(@PathVariable username: String, @RequestBody challengeData: String): ResponseEntity<*> {
        val challenge = newSuspendedTransaction {
            val challenge = Json.decodeFromString<CodeChallengeDto>(challengeData)
            challengeService.updateChallenge(username, challenge)
        }

        return ResponseEntity.ok(challenge)
    }

    @DeleteMapping("/codewars/{username}/{name}")
    suspend fun deleteCodeWarsChallenge(@PathVariable username: String, @PathVariable name: String){
        newSuspendedTransaction {
            challengeService.deleteChallenge(username, name)
        }
    }


}