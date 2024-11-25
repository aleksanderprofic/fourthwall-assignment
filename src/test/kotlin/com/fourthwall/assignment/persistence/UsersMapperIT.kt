package com.fourthwall.assignment.persistence

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.fourthwall.assignment.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class UsersMapperIT @Autowired constructor(
    private val usersMapper: UsersMapper,
    private val usersTestMapper: UsersTestMapper
) : AbstractIntegrationTest() {
    @Test
    fun `getByUsername should get user when it exists`() {
        val userId = UUID.randomUUID()
        val username = "testUsername"
        val testPassword = "testPassword"
        val role = "ADMIN"
        usersTestMapper.insert(User(userId, username, testPassword, role))

        val result = usersMapper.getByUsername(username)

        assertThat(result).isNotNull().given {
            assertThat(it.userId).isEqualTo(userId)
            assertThat(it.username).isEqualTo(username)
            assertThat(it.password).isEqualTo(testPassword)
            assertThat(it.role).isEqualTo(role)
        }
    }

    @Test
    fun `getByUsername should return null when user does not exist`() {
        val result = usersMapper.getByUsername("nonExistingUsername")

        assertThat(result).isNull()
    }
}
