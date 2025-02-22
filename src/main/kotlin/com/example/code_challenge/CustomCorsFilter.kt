package com.example.code_challenge

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomCorsFilter: OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
//        response.setHeader("Access-Control-Allow-Origin", "*")
//        response.setHeader("Access-Control-Allow-Headers", "Content-type")
        filterChain.doFilter(request, response)
    }
}