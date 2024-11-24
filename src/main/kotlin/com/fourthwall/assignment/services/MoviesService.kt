package com.fourthwall.assignment.services

import com.fourthwall.assignment.exceptions.MovieNotFoundException
import com.fourthwall.assignment.persistence.Movie
import com.fourthwall.assignment.persistence.MoviesMapper
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MoviesService(private val moviesMapper: MoviesMapper) {
    fun getAll(): Collection<Movie> {
        return moviesMapper.getAll()
    }

    fun getByMovieId(movieId: UUID): Movie {
        return moviesMapper.getByMovieId(movieId) ?: throw MovieNotFoundException(movieId)
    }
}
