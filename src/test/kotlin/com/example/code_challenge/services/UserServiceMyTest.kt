package com.example.code_challenge.services

import com.example.code_challenge.data.database.dto.LoginDto
import com.example.code_challenge.data.database.dto.UserDto
import com.example.code_challenge.data.database.entities.UserEntity
import com.example.code_challenge.data.database.tables.UsersTable
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@SpringBootTest
class UserServiceMyTest {

    @Autowired
    lateinit var userService: UserService

    val user1 = UserDto("test@mail.ru", "testUsername", "123")
    val user2 = UserDto("test@mail.ru1", "testUsername1", "123")

    @BeforeEach
    fun init() {
        transaction {
            UsersTable.deleteAll()
            UserEntity.new {
                email = user1.email
                username = user1.username
                password = user1.password
            }
        }
    }

    @Test
    fun createUser() {
        val dbUser = transaction {
            userService.createUser(user2)
        }
        assertEquals(user2, dbUser)
    }

    @Test
    fun getUser() {
        val user = transaction { userService.getUser(user1.username) }
        assertEquals(user1, user)
    }

    @Test
    fun updateUser(){
        val updateData = UserDto("test3@mail.ru", "testUsername3", "123")
        transaction { userService.updateUser(user1.username, updateData) }
        val user = transaction { UserEntity.find { UsersTable.email eq updateData.email }.first().toDto() }
        assertNotEquals(user1, user)
        assertEquals(updateData, user)
    }

    @Test
    fun deleteUser(){
        val isNull = transaction {
            userService.deleteUser(user1.username)
            UserEntity.find { UsersTable.email eq user1.email }.firstOrNull()
        }

        assertEquals(null, isNull)
    }

    @Test
    fun isExistsByMail(){
        val mustExists = transaction { userService.isExists(user1.email) }
        val mustNotExists  = transaction { userService.isExists("") }
        assertEquals(true, mustExists)
        assertEquals(false, mustNotExists)
    }

    @Test
    fun isExistsByDto(){
        val mustExists = transaction { userService.isExists(LoginDto(user1.email, user1.password)) }
        val mustNotExists = transaction {userService.isExists(LoginDto("", ""))}
        assertEquals(true, mustExists)
        assertEquals(false, mustNotExists)
    }


    @AfterEach
    fun clear() {
        transaction {
            UsersTable.deleteAll()
        }
    }


}