package com.example.code_challenge.data.database.tables

import com.example.code_challenge.data.challenge_sources.ChallengeSources
import org.jetbrains.exposed.dao.id.UUIDTable

object CodeChallengesTable : UUIDTable() {
    val name = varchar("name", 255)
    val description = text("description")
    val challengeSource = enumeration("source", ChallengeSources::class)
    val difficult = varchar("difficult", 20)
    val solution = text("solution")
    val userId = reference("user_id", UsersTable.id)
}