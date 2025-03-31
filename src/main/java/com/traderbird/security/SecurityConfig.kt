package com.traderbird.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtTokenFilter: JwtTokenFilter): SecurityFilterChain {
        http
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/api/users/login", "/api/users/register-stage-1",
                        "/api/users/register-stage-2", "/api/users/logout",
                        "/api/users/reset-password-stage-1/**", "/api/users/reset-password-stage-2",
                    ).permitAll()
                    .requestMatchers("/api/users/get_details").permitAll()
                    .requestMatchers("/api/test/**").permitAll()
                    .requestMatchers("/*").permitAll()
                    .requestMatchers(
                        HttpMethod.OPTIONS
                    ).permitAll()
                    .requestMatchers("/apidictionary/**").hasAnyRole("ADMIN", "DEVELOPER")
                    .requestMatchers("/static/**").permitAll()
                    .requestMatchers("/index.html").permitAll()
                    .requestMatchers("/v2/**").permitAll()
                    .anyRequest().authenticated()
            }
            .csrf { it.disable() }
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}
