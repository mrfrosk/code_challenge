package com.example.code_challenge.controllers

import com.example.code_challenge.data.database.dto.LoginDto
import com.example.code_challenge.services.JwtService
import com.example.code_challenge.services.UserService
import com.example.code_challenge.data.database.dto.TokensDto
import com.example.code_challenge.data.database.dto.UserDto
import kotlinx.serialization.json.Json
import org.apache.el.parser.Token
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin
@RequestMapping(Mapping.AUTH)
class AuthController {

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/login")
    suspend fun getTokens(@RequestBody data: String): ResponseEntity<TokensDto> {
        val loginDto = Json.decodeFromString<LoginDto>(data)
        return if (transaction { userService.authUser(loginDto) }) {
            val tokens = TokensDto(
                jwtService.generateAccessToken(loginDto.email),
                jwtService.generateRefreshToken(loginDto.email)
            )
            ResponseEntity.ok(tokens)
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        }
    }

    @PostMapping("/register", produces = ["application/json"])
    suspend fun registerUser(@RequestBody userData: String): ResponseEntity<String> {
        val user = Json.decodeFromString<UserDto>(userData)
        newSuspendedTransaction {
            userService.createUser(user)
        }
        return ResponseEntity.ok(userData)
    }

    @PostMapping("/register/oauth2")
    suspend fun createUserFromOAuth2(): ResponseEntity<*> {
        val data = (SecurityContextHolder.getContext().authentication.principal as DefaultOAuth2User).attributes
        val user = UserDto(data["email"].toString(), data["name"].toString(), UUID.randomUUID().toString())
        return newSuspendedTransaction {
            ResponseEntity.ok(userService.createUser(user))
        }
    }

    @PostMapping("tokens/update/{refreshToken}")
    suspend fun updateTokens(@PathVariable refreshToken: String): ResponseEntity<TokensDto> {
        val email = jwtService.getEmail(refreshToken)


        return if (jwtService.verifyRefreshToken(refreshToken)) {
            val newTokens = TokensDto(jwtService.generateAccessToken(email), jwtService.generateRefreshToken(email))
            ResponseEntity.ok(newTokens)
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        }
    }

}