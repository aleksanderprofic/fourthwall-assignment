package com.fourthwall.assignment.controllers

import assertk.assertThat
import assertk.assertions.isNotEmpty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fourthwall.assignment.AbstractIntegrationTest
import com.fourthwall.assignment.persistence.User
import com.fourthwall.assignment.persistence.UsersTestMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.UUID

class LoginControllerIT @Autowired constructor(
    private val mockMvc: MockMvc,
    private val usersTestMapper: UsersTestMapper,
    private val objectMapper: ObjectMapper
) : AbstractIntegrationTest() {

    companion object {
        private const val LOGIN_URL = "/login"
    }

    @Test
    fun `login should return 400 when request body is incorrect`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `login should return 403 when user does not exist`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"username\", \"password\": \"password\"}")
        ).andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `login should return 200 and jwt token when user exist`() {
        usersTestMapper.insert(
            User(
                UUID.randomUUID(),
                "username",
                "{bcrypt}$2a$10$${"rCvcR6k5ABxye1"}/f1zwlMOGEsviqFG/vPO0/bVvD2zfzKzTo.Xyc2",
                "ADMIN"
            )
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"username\", \"password\": \"password\"}")
        ).andExpect(MockMvcResultMatchers.status().isOk).andReturn()

        val loginResponse = objectMapper.readValue(result.response.contentAsString, LoginResponse::class.java)
        assertThat(loginResponse.token).isNotEmpty()
    }
}
