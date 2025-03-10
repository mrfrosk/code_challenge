package com.example.code_challenge.data.database.dto

import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
class JwtProperties {
    @Autowired
    @Value("\${jwt.access.secret}")
    private lateinit var accessSecret: String

    @Autowired
    @Value("\${jwt.refresh.secret}")
    private lateinit var refreshSecret: String

    @Autowired
    @Value("\${jwt.access.expiration}")
    private lateinit var accessExpiration: String

    @Autowired
    @Value("\${jwt.refresh.expiration}")
    private lateinit var refreshExpiration: String

    fun getAccessKey(): SecretKey = Keys.hmacShaKeyFor(accessSecret.toByteArray())
    fun getRefreshKey(): SecretKey = Keys.hmacShaKeyFor(refreshSecret.toByteArray())
    fun getAccessExpiration() = accessExpiration.toLong()
    fun getRefreshExpiration() = refreshExpiration.toLong()

}