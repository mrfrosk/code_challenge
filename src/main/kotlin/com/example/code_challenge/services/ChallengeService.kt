package com.example.code_challenge.services

import com.example.code_challenge.data.challenge_sources.IChallengeSource
import com.example.code_challenge.data.database.dto.CodeChallengeDto
import com.example.code_challenge.data.database.entities.CodeChallengeEntity
import com.example.code_challenge.data.database.entities.UserEntity
import com.example.code_challenge.data.database.tables.CodeChallengesTable
import com.example.code_challenge.data.database.tables.UsersTable
import org.jetbrains.exposed.sql.and
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChallengeService {

    @Autowired
    lateinit var source: IChallengeSource

    fun createChallenge(username: String, challenge: CodeChallengeDto) {
        val user = UserEntity.find { UsersTable.username eq username }.first()
        CodeChallengeEntity.new {
            name = challenge.name
            description = challenge.description
            challengeSource = challenge.challengeSource
            difficult = challenge.difficult
            solution = challenge.solution
            userEntity = user
        }
    }

    fun getChallenge(username: String, name: String): CodeChallengeDto {
        val userId = UserEntity.find { UsersTable.username eq username }.first().id.value
        return CodeChallengeEntity.find {
            (CodeChallengesTable.userId eq userId) and (CodeChallengesTable.name eq name)
        }.first().toDto()
    }

    fun updateChallenge(username: String, updateData: CodeChallengeDto): CodeChallengeDto {
        val userId = UserEntity.find { UsersTable.username eq username }.first().id.value
        val challenge = CodeChallengeEntity.find {
            CodeChallengesTable.userId eq userId
        }.first()
        challenge.updateFromDto(updateData)
        return challenge.toDto()
    }

    fun deleteChallenge(username: String, name: String) {
        val userId = UserEntity.find { UsersTable.username eq username }.first().id.value
        CodeChallengeEntity.find {
            (CodeChallengesTable.userId eq userId) and (CodeChallengesTable.name eq name)
        }.first().delete()
    }

    suspend fun addChallengeFromSource(username: String, offset: Int = 5) {
        source.getChallenges(username, offset).forEach {
            createChallenge(username ,it)
        }
    }

}

