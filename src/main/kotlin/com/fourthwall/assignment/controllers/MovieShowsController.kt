package com.fourthwall.assignment.controllers

import com.fourthwall.assignment.exceptions.RecordNotFoundException
import com.fourthwall.assignment.persistence.MovieShow
import com.fourthwall.assignment.services.MovieShowsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@RestController
@RequestMapping("/v1/movie-shows")
class MovieShowsController(
    private val movieShowsService: MovieShowsService
) {
    @GetMapping("/movies/{movieId}")
    fun getMovieShows(@PathVariable("movieId") movieId: UUID): GetMovieShowsResponse {
        val movieShows = movieShowsService.getByMovieId(movieId).map { MovieShowDTO(it) }

        return GetMovieShowsResponse(result = movieShows)
    }

    @PostMapping
    fun createMovieShow(@RequestBody request: CreateMovieShowRequest): CreateMovieShowResponse {
        // TODO: more validation e.g. if movie actually exists
        val createdMovieShow = movieShowsService.createMovieShow(
            movieId = request.movieId,
            time = request.startsAt,
            price = request.price,
        )

        return CreateMovieShowResponse(result = MovieShowDTO(createdMovieShow))
    }

    @PutMapping("/{movieShowId}")
    fun updateMovieShow(
        @PathVariable("movieShowId") movieShowId: UUID,
        @RequestBody request: UpdateMovieShowRequest
    ): Any {
        val updatedMovieShow = try {
            // TODO: validation if movie actually exists
            movieShowsService.updateMovieShow(movieShowId, request.movieId, request.startsAt, request.price)
        } catch (e: RecordNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }

        return UpdateMovieShowResponse(
            result = MovieShowDTO(updatedMovieShow)
        )
    }
}

data class GetMovieShowsResponse(
    val result: Collection<MovieShowDTO>
)

data class CreateMovieShowRequest(
    val movieId: UUID,
    val startsAt: OffsetDateTime,
    val price: BigDecimal,
)

data class CreateMovieShowResponse(
    val result: MovieShowDTO
)

data class UpdateMovieShowRequest(
    val movieId: UUID,
    val startsAt: OffsetDateTime,
    val price: BigDecimal
)

data class UpdateMovieShowResponse(
    val result: MovieShowDTO
)

data class MovieShowDTO(
    val movieShowId: UUID,
    val movieId: UUID,
    val startsAt: OffsetDateTime,
    val price: BigDecimal,
) {
    constructor(movieShow: MovieShow) : this(
        movieShowId = movieShow.movieShowId,
        movieId = movieShow.movieId,
        startsAt = movieShow.startsAt,
        price = movieShow.price
    )
}
