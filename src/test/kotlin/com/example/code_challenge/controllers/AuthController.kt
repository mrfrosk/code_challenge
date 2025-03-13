package com.example.code_challenge.controllers

import com.example.code_challenge.CodeChallengeApplication
import com.example.code_challenge.data.database.dto.LoginDto
import com.example.code_challenge.data.database.dto.TokensDto
import com.example.code_challenge.data.database.dto.UserDto
import com.example.code_challenge.data.database.entities.UserEntity
import com.example.code_challenge.data.database.tables.CodeChallengesTable
import com.example.code_challenge.data.database.tables.UsersTable
import com.example.code_challenge.services.JwtService
import com.nimbusds.oauth2.sdk.token.Tokens
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = [CodeChallengeApplication::class]
)
class AuthController {

    @Autowired
    lateinit var jwtService: JwtService

    val client = HttpClient(CIO)
    val address = "http://localhost:8080${Mapping.AUTH}"
    val user1 = UserDto("email", "test", "123")
    val userId: UUID = UUID.randomUUID()

    @BeforeEach
    fun init(): Unit = transaction {
        UsersTable.deleteAll()
        UserEntity.new(userId) {
            email = user1.email
            username = user1.username
            password = user1.password
        }
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getTokensTest(): Unit = runBlocking {
        val tokens = getTokens()
        assertEquals(true, jwtService.verifyAccessToken(tokens.accessToken))
        assertEquals(true, jwtService.verifyRefreshToken(tokens.refreshToken))
    }

    @OptIn(InternalAPI::class)
    fun getTokens() = runBlocking {
        val tokenRequest = client.post("http://localhost:8080/auth/login") {
            body = Json.encodeToString(LoginDto(user1.email, user1.password))
        }.bodyAsText()

        Json.decodeFromString<TokensDto>(tokenRequest)
    }

    @AfterEach
    fun clear(): Unit = transaction {
        UsersTable.deleteAll()
    }

}