package com.example.code_challenge

import com.example.code_challenge.controllers.Mapping
import com.example.code_challenge.services.JwtAuthenticationFilter
import jakarta.servlet.DispatcherType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityMappingConfig {

    @Autowired
    lateinit var filter: JwtAuthenticationFilter

    @Bean
    fun securityFilterChain(
        http: HttpSecurity
    ): DefaultSecurityFilterChain {
        http

            .cors(Customizer.withDefaults())
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()
                    .requestMatchers(HttpMethod.POST, "${Mapping.AUTH}/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "${Mapping.USER}/**").authenticated()
            }.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

}