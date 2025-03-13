package com.example.code_challenge.controllers

import com.example.code_challenge.CodeChallengeApplication
import com.example.code_challenge.data.database.dto.LoginDto
import com.example.code_challenge.data.database.dto.TokensDto
import com.example.code_challenge.data.database.dto.UserDto
import com.example.code_challenge.data.database.entities.UserEntity
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.User
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = [CodeChallengeApplication::class]
)
class UserControllerMyTest {

    val client = HttpClient(CIO)
    val address = "http://localhost:8080${Mapping.USER}"
    val user1 = UserDto("email", "test", "123")
    val updateData = UserDto("email", "test1", "124")
    val userId = UUID.randomUUID()


    @BeforeEach
    fun init(): Unit = transaction {
        UsersTable.deleteAll()
        UserEntity.new(userId) {
            email = user1.email
            username = user1.username
            password = user1.password
        }
    }

    @Test
    fun getUserTest() = runBlocking {
        val tokens = getTokens()
        val request = client.get("$address/${user1.username}") {
            headers.append("Authorization", "Bearer ${tokens.accessToken}")
        }
        val user = Json.decodeFromString<UserDto>(request.bodyAsText())
        assertEquals(user1, user)
    }

    @OptIn(InternalAPI::class)
    @Test
    fun updateUser() = runBlocking {
        val tokens = getTokens()
        val request = client.put("$address/${user1.username}") {
            headers.append("Authorization", "Bearer ${tokens.accessToken}")
            body = Json.encodeToString(updateData)
        }
        val user = Json.decodeFromString<UserDto>(request.bodyAsText())
        assertEquals(updateData, user)
    }

    @Test
    fun deleteUser(): Unit = runBlocking {
        val tokens = getTokens()
        val request = client.delete("$address/${user1.username}") {
            headers.append("Authorization", "Bearer ${tokens.accessToken}")
        }
        assertThrows<EntityNotFoundException> {
            transaction { UserEntity[userId] }
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
        UsersTable.deleteAll()
    }
}