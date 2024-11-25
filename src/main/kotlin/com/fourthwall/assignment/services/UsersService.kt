package com.fourthwall.assignment.services

import com.fourthwall.assignment.persistence.UsersMapper
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UsersService(
    private val usersMapper: UsersMapper,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return usersMapper.getByUsername(username)?.let { user ->
            User.withUsername(user.username)
                .password(user.password)
                .roles(user.role)
                .disabled(false)
                .build()
        } ?: throw UsernameNotFoundException("Username: $username, was not found!")
    }
}
