package com.traderbird.security

import com.traderbird.service.UserService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.security.SignatureException
import java.util.*

/*@Component
class JwtTokenFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // get JWT token from http request

        val token = getTokenFromRequest(request)

        // validate token
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token!!)) {
            // get username from token

            val username: String = jwtTokenProvider.getUsername(token)

            // load the user associated with token
            val userDetails = userDetailsService.loadUserByUsername(username)

            val authenticationToken = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )

            authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authenticationToken
        }

        filterChain.doFilter(request, response)
    }
}*/

@Component
class JwtTokenFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userDetailsService: UserDetailsService,
    private val userService: UserService
) : OncePerRequestFilter(

) {
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = getTokenFromCookies(request)

            if (token == null || !validateToken(token)) {
                filterChain.doFilter(request, response)
                return
            }

            val username: String? = jwtTokenProvider.getUsername(token)

            if (username == null || username == "anonymousUser") {
                filterChain.doFilter(request, response)
                return
            }

            if (userService.existsByUsername(username)) {
                // load the user associated with token
                val userDetails = userDetailsService.loadUserByUsername(username)

                val authenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )

                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authenticationToken
            }
        } catch (_: Exception) {
        }

        filterChain.doFilter(request, response)
    }

    private fun validateToken(token: String): Boolean {
        return try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.toByteArray())
                .build()
                .parseClaimsJws(token)
                .body
            !claims.expiration.before(Date())
        } catch (_: Exception) {
            false
        }
    }

    private fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        val claims: Claims = Jwts.parserBuilder()
            .setSigningKey(secretKey.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body

        val username = claims.subject
        val authorities = (claims["roles"] as? List<*>)?.map { SimpleGrantedAuthority(it.toString()) } ?: emptyList()
        val grantedAuthorities = authorities.map { SimpleGrantedAuthority(it.toString()) }

        return UsernamePasswordAuthenticationToken(username, null, grantedAuthorities)
    }

    fun getTokenFromCookies(request: HttpServletRequest): String? {
        val cookies: Array<Cookie>? = request.cookies
        cookies?.forEach { cookie ->
            if (cookie.name == "token") {
                return cookie.value
            }
        }
        return null
    }
}
