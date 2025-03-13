package com.example.code_challenge.services.utils.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourceProperty(
    val url: String,
    @SerialName("driver-class-name")
    val driverClassName: String,
    val username: String,
    val password: String
)
