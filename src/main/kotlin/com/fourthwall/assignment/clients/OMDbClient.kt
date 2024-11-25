package com.fourthwall.assignment.clients

import com.fasterxml.jackson.annotation.JsonProperty
import com.fourthwall.assignment.config.OMDbAPIProperties
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class OMDbClient(private val omdbTemplate: RestTemplate, config: OMDbAPIProperties) {
    companion object {
        private const val API_KEY_PARAM = "apikey"
        private const val IMDB_ID_PARAM = "i"
    }

    private val fetchMovieUrl = "${config.url}?$API_KEY_PARAM=${config.apiKey}&$IMDB_ID_PARAM={imdbId}"

    fun fetchMovieDetails(imdbId: String): OMDbMovie {
        return omdbTemplate.getForEntity(fetchMovieUrl, OMDbMovie::class.java, imdbId).body
            ?: throw RuntimeException("Missing response body")
    }
}


data class OMDbMovie(
    @JsonProperty("Title")
    val title: String,
    @JsonProperty("Runtime")
    val runtime: String,
    @JsonProperty("Plot")
    val plot: String,
    @JsonProperty("Released")
    val releaseDate: String,
    val imdbRating: String
)
