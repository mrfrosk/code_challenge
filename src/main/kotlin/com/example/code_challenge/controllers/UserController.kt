package com.example.code_challenge.controllers

import com.example.code_challenge.data.database.dto.LoginDto
import com.example.code_challenge.data.database.dto.UserDto
import com.example.code_challenge.services.UserService
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@CrossOrigin
@RequestMapping(Mapping.USER)
class UserController {

    @Autowired
    lateinit var userService: UserService
    @GetMapping("/{email}", produces = ["application/json"])
    suspend fun getUser(@PathVariable email: String): ResponseEntity<*> {
        val user = newSuspendedTransaction {
            userService.getUser(email)
        }
        val entity = ResponseEntity.ok(user)
        return entity
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
    suspend fun deleteUser(@PathVariable email: String) {
        newSuspendedTransaction {
            userService.deleteUser(email)
        }
    }

    @PostMapping("/register/oauth2")
    suspend fun createUserFromOAuth2(): ResponseEntity<*> {
        val data = (SecurityContextHolder.getContext().authentication.principal as DefaultOAuth2User).attributes
        val user = UserDto(data["email"].toString(), data["name"].toString(), UUID.randomUUID().toString())
        return newSuspendedTransaction {
            ResponseEntity.ok(userService.createUser(user))
        }
    }

    @PostMapping("/login")
    suspend fun login(@RequestBody userDto: String): ResponseEntity<*> {
        val loginData = Json.decodeFromString<LoginDto>(userDto)
        val authStatus = newSuspendedTransaction {
            userService.authUser(loginData)
        }
        return if (authStatus) {
            ResponseEntity.ok().build<Nothing>()
        } else {
            throw AccessDeniedException("")
        }
    }
}