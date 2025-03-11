package com.example.code_challenge

import com.example.code_challenge.services.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class JwtTest {

    @Autowired
    lateinit var jwtService: JwtService

    private final val email = "test"

    @Test
    fun test(){
        val refreshToken = jwtService.generateRefreshToken(email)

        assertEquals(true, jwtService.verifyRefreshToken(refreshToken))
    }
}