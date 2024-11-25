package com.fourthwall.assignment.services

import com.fourthwall.assignment.clients.OMDbClient
import com.fourthwall.assignment.clients.OMDbMovie
import com.fourthwall.assignment.exceptions.RecordNotFoundException
import com.fourthwall.assignment.persistence.Movie
import com.fourthwall.assignment.persistence.MoviesMapper
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MoviesService(
    private val moviesMapper: MoviesMapper,
    private val omDbClient: OMDbClient
) {
    fun getAll(): Collection<Movie> {
        return moviesMapper.getAll()
    }

    fun getDetailsByMovieId(movieId: UUID): OMDbMovie {
        val movie = moviesMapper.getByMovieId(movieId)
            ?: throw RecordNotFoundException("Movie: $movieId, was not found!")

        return omDbClient.fetchMovieDetails(movie.imdbId)
    }
}
