package com.example.code_challenge.controllers

import com.example.code_challenge.data.database.dto.UserDto
import com.example.code_challenge.services.UserService
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin
@RequestMapping(Mapping.USER)
class UserController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/{username}", produces = ["application/json"])
    suspend fun getUser(@PathVariable username: String): ResponseEntity<*> {
        val user = newSuspendedTransaction {
            userService.getUser(username)
        }
        val entity = ResponseEntity.ok(user)
        return entity
    }


    @PutMapping("/{username}", produces = ["application/json"])
    suspend fun updateUser(@PathVariable username: String, @RequestBody update: String): ResponseEntity<*> {
        val updateData = Json.decodeFromString<UserDto>(update)
        newSuspendedTransaction {
            userService.updateUser(username, updateData)
        }
        return ResponseEntity.ok(updateData)
    }

    @DeleteMapping("/{username}")
    suspend fun deleteUser(@PathVariable username: String) {
        newSuspendedTransaction {
            userService.deleteUser(username)
        }
    }


}