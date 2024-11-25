package com.fourthwall.assignment.controllers

import com.fourthwall.assignment.config.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.Date


@RestController
class LoginController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    @Value("\${application.security.jwt-ttl:300000}") private val jwtTtl: Int
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): LoginResponse {
        val authToken = UsernamePasswordAuthenticationToken.unauthenticated(request.username, request.password)
        val authentication = authenticationManager.authenticate(authToken)
        if (!authentication.isAuthenticated) {
            throw UsernameNotFoundException("Failed to authenticate")
        }

        val token = jwtUtil.generate(request.username, jwtTtl)
        return LoginResponse(
            token,
            jwtUtil.extractCreatedAt(token),
            jwtUtil.extractExpirationDate(token)
        )
    }
}


data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val createdAt: Date, val expiresAt: Date)
