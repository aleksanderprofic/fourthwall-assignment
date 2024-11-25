package com.fourthwall.assignment.config

import com.fourthwall.assignment.services.UsersService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Date
import javax.crypto.SecretKey


@Configuration
@EnableMethodSecurity
class SecurityConfig(private val usersService: UsersService) {
    @Bean
    fun jwtUtil(@Value("\${application.security.jwt.secret-key}") secretKey: String): JwtUtil {
        return JwtUtil(Keys.hmacShaKeyFor(secretKey.toByteArray()))
    }

    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
        return configuration.authenticationManager
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtAuthFilter: JwtAuthFilter): SecurityFilterChain {
        return http
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .sessionManagement { configurer: SessionManagementConfigurer<HttpSecurity?> ->
                configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    private fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider(PasswordEncoderFactories.createDelegatingPasswordEncoder())
        provider.setUserDetailsService(usersService)
        return provider
    }
}

class JwtUtil(private val secretKey: SecretKey) {
    fun generate(username: String?, ttlInMs: Int): String {
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + ttlInMs))
            .signWith(secretKey)
            .compact()
    }

    fun extractUsername(token: String): String {
        return extractClaim(token) { it.subject }
    }

    fun extractExpirationDate(token: String): Date {
        return extractClaim(token) { it.expiration }
    }

    fun extractCreatedAt(token: String): Date {
        return extractClaim(token) { it.issuedAt }
    }

    fun isTokenValid(token: String, userName: String): Boolean {
        return !isTokenExpired(token) && extractUsername(token) == userName
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpirationDate(token).before(Date())
    }

    private fun <T> extractClaim(token: String, claimsResolvers: (Claims) -> T): T {
        val claims = extractClaims(token)
        return claimsResolvers(claims)
    }

    private fun extractClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}


@Component
class JwtAuthFilter(
    private val userDetailsService: UserDetailsService,
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {
    companion object {
        private const val AUTH_HEADER = "Authorization"
        private const val AUTH_TYPE = "Bearer"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = extractAuthorizationHeader(request)
        if (token == null) {
            filterChain.doFilter(request, response)
            return
        }

        if (SecurityContextHolder.getContext().authentication == null) {
            val tokenUser = jwtUtil.extractUsername(token)

            val userDetails = userDetailsService.loadUserByUsername(tokenUser)
            if (!jwtUtil.isTokenValid(token, tokenUser)) {
                throw UsernameNotFoundException("Failed to authenticate with access token")
            }
            val context = SecurityContextHolder.createEmptyContext()
            val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
            context.authentication = authToken
            SecurityContextHolder.setContext(context)
        }

        filterChain.doFilter(request, response)
    }

    private fun extractAuthorizationHeader(request: HttpServletRequest): String? {
        val headerValue = request.getHeader(AUTH_HEADER)
        return if (headerValue == null || !headerValue.startsWith(AUTH_TYPE)) {
            null
        } else headerValue.substring(AUTH_TYPE.length).trim { it <= ' ' }
    }
}
