package com.fourthwall.assignment.controllers

import com.fourthwall.assignment.persistence.MovieDAO
import com.fourthwall.assignment.services.MoviesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/movies")
class MoviesController(
    private val moviesService: MoviesService
) {
    @GetMapping
    fun getAllMovies(): GetMoviesResponse {
        return GetMoviesResponse(
            result = listOf()
        )
    }

    @GetMapping("/{movieId}")
    fun getMovieById(@PathVariable("movieId") movieId: UUID): GetMovieResponse {
        return GetMovieResponse(
            result = moviesService.getByMovieId(movieId)
        )
    }
}

data class GetMoviesResponse(
    val result: Collection<MovieDAO>
)

data class GetMovieResponse(
    val result: MovieDAO
)
