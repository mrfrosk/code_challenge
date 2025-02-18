package com.example.code_challenge.data.database.entities

import com.example.code_challenge.data.database.dto.CodeChallengeDto
import com.example.code_challenge.data.database.tables.CodeChallengesTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class CodeChallengeEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CodeChallengeEntity>(CodeChallengesTable)

    var name by CodeChallengesTable.name
    var description by CodeChallengesTable.description
    var challengeSource by CodeChallengesTable.challengeSource
    var difficult by CodeChallengesTable.difficult
    var solution by CodeChallengesTable.solution
    var userEntity by UserEntity referencedOn CodeChallengesTable.userId

    fun toDto() = CodeChallengeDto(
        name,
        description,
        challengeSource,
        difficult,
        solution
    )

    fun updateFromDto(updateData: CodeChallengeDto) {
        name = updateData.name
        description = updateData.description
        challengeSource = updateData.challengeSource
        difficult = updateData.difficult
        solution = updateData.solution
    }
}