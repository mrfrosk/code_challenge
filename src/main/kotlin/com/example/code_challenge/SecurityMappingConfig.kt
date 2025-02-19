package com.example.code_challenge

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityMappingConfig {

    @Autowired
    lateinit var filter: CustomCorsFilter
    @Bean
    fun securityFilterChain(
        http: HttpSecurity
    ): DefaultSecurityFilterChain {
        http
            .headers {
                it.contentSecurityPolicy {
                    contentSecurityPolicyConfig -> contentSecurityPolicyConfig.policyDirectives("default-src 'self'")
                }
            }
            .cors(Customizer.withDefaults())
            .csrf { it.disable()}
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }.oauth2Login {}.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.NEVER)
            }
            .addFilterBefore(filter, BasicAuthenticationFilter::class.java)
        return http.build()
    }

}