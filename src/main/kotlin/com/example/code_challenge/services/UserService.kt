package com.example.code_challenge.services

import com.example.code_challenge.data.database.dto.LoginDto
import com.example.code_challenge.data.database.dto.UserDto
import com.example.code_challenge.data.database.entities.UserEntity
import com.example.code_challenge.data.database.tables.UsersTable
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Service

@Service
class UserService {

    fun getUser(username: String): UserDto {
        return getUserIfExists(username)?.toDto() ?: throw Exception("user not exists")
    }

    fun authUser(loginDto: LoginDto): Boolean {
        return UserEntity.find {
            (UsersTable.username eq loginDto.username) and (UsersTable.password eq loginDto.password)
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
        val user = getUserIfExists(username)
        if (user != null) {
            user.email = userDto.email
            user.username = userDto.username
            user.password = userDto.password
        } else {
            throw Exception("user not exists")
        }
    }

    fun deleteUser(username: String){
        val user = getUserIfExists(username)
        user?.delete()
    }

    private fun getUserIfExists(username: String): UserEntity? {
        return UserEntity.find {
            UsersTable.username eq username
        }.firstOrNull()
    }
}