package com.fourthwall.assignment.controllers

import com.fourthwall.assignment.clients.OMDbMovie
import com.fourthwall.assignment.exceptions.RecordNotFoundException
import com.fourthwall.assignment.persistence.Movie
import com.fourthwall.assignment.services.MoviesService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun getMovieDetails(@PathVariable("movieId") movieId: UUID): Any {
        val movieDetails = try {
            moviesService.getDetailsByMovieId(movieId)
        } catch (e: RecordNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }

        return GetMovieDetailsResponse(
            result = MovieDetailsDTO(movieDetails)
        )
    }
}

data class GetMoviesResponse(
    val result: Collection<MovieDTO>
)

data class GetMovieDetailsResponse(
    val result: MovieDetailsDTO
)

data class MovieDTO(
    val movieId: UUID,
    val imdbId: String,
    val title: String
) {
    constructor(movie: Movie) : this(movie.movieId, movie.imdbId, movie.title)
}

data class MovieDetailsDTO(
    val title: String,
    val runtime: String,
    val plot: String,
    val releaseDate: String,
    val imdbRating: String
) {
    constructor(movie: OMDbMovie) : this(movie.title, movie.runtime, movie.plot, movie.releaseDate, movie.imdbRating)
}
