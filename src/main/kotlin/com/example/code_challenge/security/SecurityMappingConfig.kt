package com.example.code_challenge.security

import com.example.code_challenge.controllers.Mapping
import com.example.code_challenge.security.filters.CustomCorsFilter
import com.example.code_challenge.security.filters.JwtAuthenticationFilter
import jakarta.servlet.DispatcherType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityMappingConfig {

    @Autowired
    lateinit var jwtFilter: JwtAuthenticationFilter

    @Autowired
    lateinit var corsFilter: CustomCorsFilter

    @Bean
    @Order(1)
    fun securityFilterChain(
        http: HttpSecurity
    ): DefaultSecurityFilterChain {
        http
            .cors(Customizer.withDefaults())
            .csrf { it.disable() }
            .securityMatcher("${Mapping.USER}/**", "${Mapping.CHALLENGE}/**")
            .authorizeHttpRequests {
                it
                    .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()
                    .requestMatchers(HttpMethod.POST, "${Mapping.AUTH}/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "${Mapping.USER}/**").permitAll()
                    .requestMatchers("${Mapping.USER}/**").authenticated()
                    .requestMatchers("${Mapping.CHALLENGE}/**").authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(corsFilter, BasicAuthenticationFilter::class.java)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    @Order(2)
    fun oAuthFilterChain2(http: HttpSecurity): SecurityFilterChain{
        return http
            .cors(Customizer.withDefaults())
            .csrf { it.disable() }
            .securityMatcher("${Mapping.AUTH}/login/oauth", "/oauth2/**", "/login/**")
            .oauth2Login {

            }
            .authorizeHttpRequests {
                it.requestMatchers("/oauth2/**").permitAll()
                it.requestMatchers("${Mapping.AUTH}/login/oauth").authenticated()
            }
            .addFilterBefore(corsFilter, BasicAuthenticationFilter::class.java)
            .build()
    }

}