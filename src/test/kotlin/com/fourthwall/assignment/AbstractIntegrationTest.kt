package com.fourthwall.assignment

import com.fourthwall.assignment.clients.OMDbClient
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest
@AutoConfigureMockMvc
abstract class AbstractIntegrationTest {
    @MockitoBean
    lateinit var omdbClient: OMDbClient
}
