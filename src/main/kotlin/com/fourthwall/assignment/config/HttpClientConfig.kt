package com.fourthwall.assignment.config

import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class HttpClientConfig {
    @Bean
    fun restTemplate(configurer: RestTemplateBuilderConfigurer): RestTemplate {
        return configurer
            .configure(RestTemplateBuilder())
            // TODO: Add error handler
            .build()
    }
}
