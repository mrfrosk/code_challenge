package com.example.code_challenge.data.challenge_sources.codewars.dto

import com.example.code_challenge.services.utils.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ChallengeDto(
    val id: String,
    val name: String,
    val slug: String,
    val url: String,
    val category: String,
    val description: String,
    val tags: List<String>,
    val languages: List<String>,
    val rank: ShortRankDto,
    @Serializable(with = LocalDateTimeSerializer::class)
    val publishedAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val approvedAt: LocalDateTime?,
    @Serializable(with= LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val createdBy: Map<String, String>?=null,
    val approvedBy: Map<String, String>?=null,
    val totalAttempts: Int,
    val totalCompleted: Int,
    val totalStars: Int,
    val voteScore: Int,
    val contributorsWanted: Boolean,
    val unresolved: Map<String, Int>,
    )
