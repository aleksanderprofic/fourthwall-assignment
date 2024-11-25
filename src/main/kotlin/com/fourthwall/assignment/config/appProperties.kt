package com.fourthwall.assignment.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(value = [OMDbAPIProperties::class])
class PropertiesFactory

@ConfigurationProperties(prefix = "omdb.api")
data class OMDbAPIProperties(
    val url: String,
    val apiKey: String
)
