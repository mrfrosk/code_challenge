package com.example.code_challenge.services

import com.example.code_challenge.data.database.dto.LoginDto
import com.example.code_challenge.data.database.dto.UserDto
import com.example.code_challenge.data.database.entities.UserEntity
import com.example.code_challenge.data.database.tables.UsersTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.springframework.stereotype.Service

@Service
class UserService {

    fun getUser(username: String): UserDto {
        return findUser(username).toDto()
    }

    fun isExists(loginDto: LoginDto): Boolean {
        return UserEntity.find {
            (UsersTable.email eq loginDto.email) and (UsersTable.password eq loginDto.password)
        }.firstOrNull() != null
    }

    fun createUser(userDto: UserDto): UserDto {
        return UserEntity.new {
            email = userDto.email
            username = userDto.username
            password = userDto.password
        }.toDto()
    }

    fun updateUser(username: String, userDto: UserDto) {
        val user = findUser(username)
        user.email = userDto.email
        user.username = userDto.username
        user.password = userDto.password
    }

    fun deleteUser(username: String) {
        val user = findUser(username)
        user.delete()
    }

    fun isExists(email: String) = UserEntity.find {
        UsersTable.email eq email
    }.firstOrNull() != null

    fun isExists(email: String, username: String) = UserEntity.find {
        (UsersTable.email eq email) or (UsersTable.username eq username)
    }.firstOrNull() != null

    private fun findUser(username: String): UserEntity {
        return UserEntity.find {
            UsersTable.username eq username
        }.first()
    }
}