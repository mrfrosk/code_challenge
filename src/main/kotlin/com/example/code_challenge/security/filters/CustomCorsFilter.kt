package com.example.code_challenge.security.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomCorsFilter: OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization")
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE")
        filterChain.doFilter(request, response)
    }
}