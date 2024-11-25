package com.fourthwall.assignment.services

import assertk.all
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.fourthwall.assignment.persistence.User
import com.fourthwall.assignment.persistence.UsersMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.UUID

class UsersServiceTest {
    private val usersMapper = mock<UsersMapper> { }
    private val usersService = UsersService(usersMapper)

    @Test
    fun `loadUserByUsername should throw exception when user does not exist`() {
        val username = "username"
        whenever(usersMapper.getByUsername(username)).thenReturn(null)

        assertFailure {
            usersService.loadUserByUsername(username)
        }.all {
            hasClass(UsernameNotFoundException::class)
            hasMessage("Username: $username, was not found!")
        }
    }

    @Test
    fun `loadUserByUsername should return user object when user exists`() {
        val username = "username"
        val password = "password"
        whenever(usersMapper.getByUsername(username)).thenReturn(User(UUID.randomUUID(), username, password, "ADMIN"))

        val user = usersService.loadUserByUsername(username)

        assertThat(user.username).isEqualTo(username)
        assertThat(user.password).isEqualTo(password)
        assertThat(user.authorities.map { it.authority }).isEqualTo(listOf("ROLE_ADMIN"))
        assertThat(user.isEnabled).isTrue()
    }
}
