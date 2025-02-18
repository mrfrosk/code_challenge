package com.example.code_challenge.data.challenge_sources.codewars.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShortRankDto(
    val id: Int,
    val name: String,
    val color: String
)