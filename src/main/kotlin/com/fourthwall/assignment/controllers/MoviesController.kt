package com.fourthwall.assignment.controllers

import com.fourthwall.assignment.persistence.Movie
import com.fourthwall.assignment.services.MoviesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/movies")
class MoviesController(
    private val moviesService: MoviesService,
) {
    @GetMapping
    fun getAllMovies(): GetMoviesResponse {
        return GetMoviesResponse(
            result = moviesService.getAll().map { MovieDTO(it) }
        )
    }

    @GetMapping("/{movieId}")
    fun getMovie(@PathVariable("movieId") movieId: UUID): GetMovieResponse {
        return GetMovieResponse(
            result = MovieDTO(moviesService.getByMovieId(movieId))
        )
    }
}

data class GetMoviesResponse(
    val result: Collection<MovieDTO>
)

data class GetMovieResponse(
    val result: MovieDTO
)

data class MovieDTO(
    val movieId: UUID,
    val imdbId: String,
    val title: String
) {
    constructor(movie: Movie) : this(movie.movieId, movie.imdbId, movie.title)
}
