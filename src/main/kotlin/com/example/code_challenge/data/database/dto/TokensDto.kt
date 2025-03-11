package com.example.code_challenge.data.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokensDto(
    @SerialName("access-token")
    val accessToken: String,
    @SerialName("refresh-token")
    val refreshToken: String
)
