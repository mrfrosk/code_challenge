package com.example.code_challenge.data.database.dto

import com.example.code_challenge.data.challenge_sources.ChallengeSources
import kotlinx.serialization.Serializable

@Serializable
data class CodeChallengeDto(
    val name: String,
    val description: String,
    val challengeSource: ChallengeSources,
    val difficult: String,
    val solution: String,
)