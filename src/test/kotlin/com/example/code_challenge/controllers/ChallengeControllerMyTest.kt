package com.example.code_challenge.controllers

import com.example.code_challenge.CodeChallengeApplication
import com.example.code_challenge.data.challenge_sources.ChallengeSources
import com.example.code_challenge.data.database.dto.CodeChallengeDto
import com.example.code_challenge.data.database.dto.LoginDto
import com.example.code_challenge.data.database.dto.TokensDto
import com.example.code_challenge.data.database.dto.UserDto
import com.example.code_challenge.data.database.entities.CodeChallengeEntity
import com.example.code_challenge.data.database.entities.UserEntity
import com.example.code_challenge.data.database.tables.CodeChallengesTable
import com.example.code_challenge.data.database.tables.UsersTable
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.asserter

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = [CodeChallengeApplication::class]
)
class ChallengeControllerMyTest {

    val client = HttpClient(CIO)
    val address = "http://localhost:8080${Mapping.CHALLENGE}"
    val user1 = UserDto("email", "test", "123")
    val userId: UUID = UUID.randomUUID()
    val challengeId: UUID = UUID.randomUUID()

    val codeChallenge = CodeChallengeDto(
        "test",
        "test",
        ChallengeSources.CodeWars,
        "test",
        "test"
    )
    val codeChallenge1 = CodeChallengeDto(
        "tes1",
        "test",
        ChallengeSources.CodeWars,
        "test",
        "test"
    )
    val updateData = CodeChallengeDto(
        "test",
        "test1",
        ChallengeSources.CodeWars,
        "test",
        "test"
    )


    @BeforeEach
    fun init(): Unit = transaction {
        CodeChallengesTable.deleteAll()
        UsersTable.deleteAll()
        UserEntity.new(userId) {
            email = user1.email
            username = user1.username
            password = user1.password
        }
        CodeChallengeEntity.new(challengeId) {
            name = codeChallenge.name
            description = codeChallenge.description
            challengeSource = codeChallenge.challengeSource
            difficult = codeChallenge.difficult
            solution = codeChallenge.solution
            userEntity = UserEntity[userId]
        }
    }

    @OptIn(InternalAPI::class)
    @Test
    fun createChallengeTest(): Unit = runBlocking {
        val tokens = getTokens()
        val request = client.post("$address/codewars/${user1.username}") {
            headers.append("Authorization", "Bearer ${tokens.accessToken}")
            body = Json.encodeToString(codeChallenge1)
        }.bodyAsText()

        println(request)
        val challenge = transaction {
            CodeChallengeEntity.find {
                CodeChallengesTable.name eq codeChallenge.name
            }.firstOrNull()?.toDto()
        }
        assertEquals(codeChallenge, challenge)
    }

    @Test
    fun getCodeChallenge(): Unit = runBlocking {
        val tokens = getTokens()
        val request = client.get("$address/codewars/${user1.username}/${codeChallenge.name}"){
            headers.append("Authorization", "Bearer ${tokens.accessToken}")
        }.bodyAsText()
        val challenge = Json.decodeFromString<CodeChallengeDto>(request)
        assertEquals(codeChallenge, challenge)
    }

    @OptIn(InternalAPI::class)
    @Test
    fun updateCodeChallenge(): Unit = runBlocking {
        val tokens = getTokens()
        val request = client.put("$address/codewars/${user1.username}"){
            headers.append("Authorization", "Bearer ${tokens.accessToken}")
            body = Json.encodeToString(updateData)
        }.bodyAsText()
        val challenge = Json.decodeFromString<CodeChallengeDto>(request)
        assertEquals(updateData, challenge)
    }


    @Test
    fun deleteCodeChallenge(): Unit = runBlocking {
        val tokens = getTokens()
        val request = client.delete("$address/codewars/${user1.username}/${codeChallenge.name}"){
            headers.append("Authorization", "Bearer ${tokens.accessToken}")
        }
        println(request.status)

        assertThrows<EntityNotFoundException> {
            transaction { CodeChallengeEntity[challengeId] }
        }
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
        CodeChallengesTable.deleteAll()
        UsersTable.deleteAll()
    }

}