package com.fourthwall.assignment.services

import com.fourthwall.assignment.exceptions.MovieNotFoundException
import com.fourthwall.assignment.persistence.MoviesMapper
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MoviesService(private val moviesMapper: MoviesMapper) {
    fun getByMovieId(movieId: UUID) = moviesMapper.getByMovieId(movieId) ?: throw MovieNotFoundException(movieId)
}
