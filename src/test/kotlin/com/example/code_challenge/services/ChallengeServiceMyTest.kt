package com.example.code_challenge.services

import com.example.code_challenge.data.challenge_sources.ChallengeSources
import com.example.code_challenge.data.database.dto.CodeChallengeDto
import com.example.code_challenge.data.database.dto.UserDto
import com.example.code_challenge.data.database.entities.CodeChallengeEntity
import com.example.code_challenge.data.database.entities.UserEntity
import com.example.code_challenge.data.database.tables.CodeChallengesTable
import com.example.code_challenge.data.database.tables.UsersTable
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@SpringBootTest
class ChallengeServiceMyTest {

    @Autowired
    lateinit var challengeService: ChallengeService
    private final val userId = UUID.randomUUID()
    val userDto = UserDto("test", "test", "test")
    val codeChallenge = CodeChallengeDto(
        "test name",
        "test description",
        ChallengeSources.CodeWars,
        "8 kyu",
        "print('hello world')"
    )
    val codeChallenge1 = CodeChallengeDto(
        "test name1",
        "test description",
        ChallengeSources.CodeWars,
        "8 kyu",
        "print('hello world')"
    )

    @BeforeEach
    fun init() {
        transaction {
            UsersTable.deleteAll()
            CodeChallengesTable.deleteAll()
            UserEntity.new(userId) {
                email = userDto.email
                username = userDto.username
                password = userDto.password
            }

            CodeChallengeEntity.new {
                name = codeChallenge.name
                description = codeChallenge.description
                challengeSource = codeChallenge.challengeSource
                difficult = codeChallenge.difficult
                solution = codeChallenge.solution
                userEntity = UserEntity[userId]
            }
        }
    }

    @Test
    fun getCodeChallenge() {
        val codeChallengeBd = transaction {
            challengeService.getChallenge(userDto.username, codeChallenge.name)
        }
        assertEquals(codeChallenge, codeChallengeBd)
    }

    @Test
    fun createChallenge() {
        val codeChallengeBd = transaction {
            challengeService.createChallenge(userDto.username, codeChallenge1)
            challengeService.getChallenge(userDto.username, codeChallenge1.name)
        }
        assertEquals(codeChallenge1, codeChallengeBd)
    }

    @Test
    fun updateCodeChallenge() {
        val updateDifficult = CodeChallengeDto(
            "test name1",
            "test description",
            ChallengeSources.CodeWars,
            "3 kyu",
            "print('hello world')"
        )

        val challengeDto = transaction {
            challengeService.updateChallenge(userDto.username, updateDifficult)
        }

        val challengeFromDb = transaction {
            CodeChallengeEntity.find {
                CodeChallengesTable.name eq codeChallenge1.name
            }.first().toDto()
        }

        assertNotEquals(codeChallenge1, challengeDto)
        assertEquals(challengeDto, challengeFromDb)
    }

    @Test
    fun deleteCodeChallenge() {
        val mustNull = transaction {
            challengeService.deleteChallenge(userDto.username, codeChallenge.name)
            CodeChallengeEntity.find {
                CodeChallengesTable.name eq codeChallenge.name
            }.firstOrNull()
        }
        assertEquals(null, mustNull)
    }

    @AfterEach
    fun clear(): Unit = transaction {
        CodeChallengesTable.deleteAll()
        UsersTable.deleteAll()
    }

}