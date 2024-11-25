package com.fourthwall.assignment.services

import assertk.all
import assertk.assertFailure
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import com.fourthwall.assignment.clients.OMDbClient
import com.fourthwall.assignment.exceptions.RecordNotFoundException
import com.fourthwall.assignment.persistence.Movie
import com.fourthwall.assignment.persistence.MoviesMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.util.UUID

class MoviesServiceTest {
    private val omdbClient = mock<OMDbClient> { }
    private val moviesMapper = mock<MoviesMapper> { }
    private val moviesService = MoviesService(moviesMapper, omdbClient)

    @Test
    fun `getDetailsByMovieId should throw exception when movie does not exist`() {
        val movieId = UUID.randomUUID()
        whenever(moviesMapper.getByMovieId(movieId)).thenReturn(null)

        assertFailure {
            moviesService.getDetailsByMovieId(movieId)
        }.all {
            hasClass(RecordNotFoundException::class)
            hasMessage("Movie: $movieId, was not found!")
        }
    }

    @Test
    fun `getDetailsByMovieId should call omdbClient when movie is found in db`() {
        val movieId = UUID.randomUUID()
        val imdbId = "imdbId"
        whenever(moviesMapper.getByMovieId(movieId)).thenReturn(Movie(movieId, imdbId, "title"))

        moviesService.getDetailsByMovieId(movieId)

        verify(omdbClient).fetchMovieDetails(imdbId)
        verifyNoMoreInteractions(omdbClient)
    }
}
