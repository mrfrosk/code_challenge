package com.example.code_challenge.controllers


import com.example.code_challenge.data.database.dto.LoginDto
import com.example.code_challenge.data.database.dto.UserDto
import com.example.code_challenge.services.UserService
import com.nimbusds.jose.shaded.gson.annotations.Expose
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(Mapping.AUTH)
class AuthController {

    @Autowired
    lateinit var userService: UserService

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