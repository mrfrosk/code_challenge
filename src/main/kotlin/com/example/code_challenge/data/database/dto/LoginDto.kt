package com.example.code_challenge.data.database.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(val username: String, val password: String)
