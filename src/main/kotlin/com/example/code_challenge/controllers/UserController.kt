package com.example.code_challenge.controllers

import com.example.code_challenge.data.database.dto.UserDto
import com.example.code_challenge.services.UserService
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Mapping.USER)
class UserController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/{email}", produces = ["application/json"])
    suspend fun getUser(@PathVariable email: String): ResponseEntity<*> {
        val user = newSuspendedTransaction {
            userService.getUser(email)
        }
        return ResponseEntity.ok(user)
    }

    @PostMapping("", produces = ["application/json"])
    suspend fun registerUser(@RequestBody userData: String): ResponseEntity<*> {
        val user = Json.decodeFromString<UserDto>(userData)
        newSuspendedTransaction {
            userService.createUser(user)
        }
        return ResponseEntity.ok(userData)
    }

    @PutMapping("/{email}", produces = ["application/json"])
    suspend fun updateUser(@PathVariable email: String, @RequestBody update: String): ResponseEntity<*> {
        val updateData = Json.decodeFromString<UserDto>(update)
        newSuspendedTransaction {
            userService.updateUser(email, updateData)
        }
        return ResponseEntity.ok(updateData)
    }

    @DeleteMapping("/{email}")
    suspend fun deleteUser(@PathVariable email: String){
        newSuspendedTransaction {
            userService.deleteUser(email)
        }
    }


}