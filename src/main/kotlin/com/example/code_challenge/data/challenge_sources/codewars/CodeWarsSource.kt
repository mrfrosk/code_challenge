package com.example.code_challenge.data.challenge_sources.codewars

import com.example.code_challenge.data.challenge_sources.ChallengeSources
import com.example.code_challenge.data.challenge_sources.IChallengeSource
import com.example.code_challenge.data.challenge_sources.codewars.dto.ChallengeDto
import com.example.code_challenge.data.challenge_sources.codewars.dto.ChallengesDto
import com.example.code_challenge.data.challenge_sources.codewars.dto.ShortChallengeDto
import com.example.code_challenge.data.database.dto.CodeChallengeDto
import com.example.code_challenge.data.database.dto.UserDto
import com.example.code_challenge.data.database.entities.CodeChallengeEntity
import com.example.code_challenge.data.database.entities.UserEntity
import com.example.code_challenge.data.database.tables.CodeChallengesTable
import com.example.code_challenge.data.database.tables.UsersTable
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import java.util.*

@Service
class CodeWarsSource: IChallengeSource {
    private val userUrl = "https://www.codewars.com/api/v1/users"
    private val challengeUrl = "code-challenges/completed"
    private val challengeInfoUrl = "https://www.codewars.com/api/v1/code-challenges/"
    private val client = HttpClient(CIO)

    override suspend fun getUser(username: String): UserDto {
       TODO("пока не решил, должен ли существовать этот метод в принципе")
    }

    private suspend fun getChallengesInfo(username: String): List<ShortChallengeDto> {
        val request = client.get("$userUrl/$username/$challengeUrl").bodyAsText()
        return Json.decodeFromString<ChallengesDto>(request).data

    }

    override suspend fun getChallenge(id: String): CodeChallengeDto {
        val request = client.get(challengeInfoUrl + id).bodyAsText()
        val body = Json.decodeFromString<ChallengeDto>(request)
        return toCodeChallenge(body)
    }

    override suspend fun getChallenges(username: String, offset: Int): List<CodeChallengeDto> {
        val user = UserEntity.find { UsersTable.username eq username }.first()
        val challenges = getChallengesInfo(username)
        val lastChallenge = getLastChallenge(user.id.value)
        val lastSavedIndex = if (lastChallenge == null) {
            0
        } else {
            val challenge = challenges.find { it.name == lastChallenge.name }
            challenges.indexOf(challenge)
        }
        return if (challenges.size - lastSavedIndex <= offset) {
            challenges.subList(lastSavedIndex, challenges.size).map {
                getChallenge(it.id)
            }
        } else {
            challenges.subList(lastSavedIndex, lastSavedIndex + offset).map {
                getChallenge(it.id)
            }
        }
    }
    private fun getLastChallenge(userId: UUID): CodeChallengeDto? {
        val challenge = CodeChallengeEntity.find { CodeChallengesTable.userId eq userId }.lastOrNull()
        return challenge?.toDto()
    }

    private fun toCodeChallenge(challenge: ChallengeDto) = CodeChallengeDto(
        challenge.name,
        challenge.description,
        ChallengeSources.CodeWars,
        challenge.rank.name,
        ""
    )
}