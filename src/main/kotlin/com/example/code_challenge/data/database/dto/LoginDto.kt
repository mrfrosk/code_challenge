package com.example.code_challenge.data.database.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(val email: String, val password: String)
