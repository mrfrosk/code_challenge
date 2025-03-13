package com.example.code_challenge.services

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class JwtServiceMyTest {

    @Autowired
    lateinit var jwtService: JwtService


    @Test
    fun verifyTokens(){
        val email = "test"
        val accessToken = jwtService.generateAccessToken(email)
        val refreshToken = jwtService.generateRefreshToken(email)

        assertEquals(true, jwtService.verifyAccessToken(accessToken))
        assertEquals(true, jwtService.verifyRefreshToken(refreshToken))
        assertEquals(false, jwtService.verifyRefreshToken(accessToken))
        assertEquals(false, jwtService.verifyAccessToken(refreshToken))

    }
}