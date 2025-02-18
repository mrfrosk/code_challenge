package com.example.code_challenge.data.challenge_sources.codewars.dto

import com.example.code_challenge.services.utils.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ShortChallengeDto(
    val id: String,
    val name: String,
    val slug: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val completedAt: LocalDateTime,
    val completedLanguages: List<String>
)