package com.example.code_challenge.data.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable

object UsersTable: UUIDTable() {
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255).uniqueIndex()
    val password = varchar("password", 255)
}