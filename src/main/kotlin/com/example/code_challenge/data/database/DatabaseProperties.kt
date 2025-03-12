package com.example.code_challenge.data.database

import org.springframework.stereotype.Component

@Component
class DatabaseProperties {
    val url = "jdbc:postgresql://localhost:5432/"
    val user = "postgres"
    val password = "123"
    val prodDbName = "code_challenge"
    val testDbName = "code_challenge_test"
}