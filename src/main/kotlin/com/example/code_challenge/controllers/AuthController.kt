package com.example.code_challenge.controllers

import com.example.code_challenge.data.database.dto.JwtProperties
import com.example.code_challenge.data.database.dto.LoginDto
import com.example.code_challenge.services.JwtService
import com.example.code_challenge.services.UserService
import com.example.first.Services.dto.TokensDto
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
        return if (transaction { userService.authUser(loginDto) }){
            val tokens = TokensDto(
                jwtService.generateAccessToken(loginDto.email),
                jwtService.generateRefreshToken(loginDto.email)
            )
            ResponseEntity.ok(tokens)
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        }


    }
}