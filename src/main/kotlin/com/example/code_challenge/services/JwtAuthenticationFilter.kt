package com.example.code_challenge.services


import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter: OncePerRequestFilter() {

    @Autowired
    lateinit var tokenService: JwtService

    @Autowired
    lateinit var userService: UserService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization")
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE")

        val authHeader: String? = request.getHeader("authorization")

        if (authHeader.doesNotContainBearerToken()) {
            filterChain.doFilter(request, response)
            return
        }
        val jwtToken = authHeader!!.extractTokenValue()
        if (!tokenService.verifyAccessToken(jwtToken)) {
            println("не подтвердили токен")
            filterChain.doFilter(request, response)
            return
        }

        val email = tokenService.getEmail(jwtToken)
        println(email)

        if (transaction { userService.isExists(email) }) {
            println(transaction { userService.isExists(email) })
            updateContext(email, request)
            filterChain.doFilter(request, response)
        }
    }

    fun String?.doesNotContainBearerToken() =
        this == null || !this.startsWith("Bearer ")

    fun String.extractTokenValue() =
        this.substringAfter("Bearer ")

    fun updateContext(email: String, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(email, null, null)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
        println(authToken)
    }

}