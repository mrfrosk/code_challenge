package com.example.code_challenge.data.challenge_sources

import com.example.code_challenge.data.database.dto.CodeChallengeDto
import com.example.code_challenge.data.database.dto.UserDto

interface IChallengeSource {
    suspend fun getUser(username: String): UserDto
    suspend fun getChallenges(username: String, offset: Int): List<CodeChallengeDto>
    suspend fun getChallenge(id: String): CodeChallengeDto
}