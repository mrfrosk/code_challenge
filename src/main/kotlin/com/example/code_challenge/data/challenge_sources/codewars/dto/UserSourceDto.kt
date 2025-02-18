package com.example.code_challenge.data.challenge_sources.codewars.dto

import com.example.code_challenge.data.challenge_sources.codewars.dto.RanksDto
import kotlinx.serialization.Serializable

@Serializable
data class UserSourceDto(
    val id: String,
    val username: String,
    val name: String?,
    val honor: Int,
    val clan: String?,
    val leaderboardPosition: Int?,
    val skills: List<String>?,
    val ranks: RanksDto,
    val codeChallenges: Map<String, Int>
)