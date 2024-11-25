package com.fourthwall.assignment.services

import assertk.all
import assertk.assertFailure
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import com.fourthwall.assignment.exceptions.RecordNotFoundException
import com.fourthwall.assignment.persistence.MovieShowsMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

class MovieShowsServiceTest {
    private val movieShowsMapper = mock<MovieShowsMapper> { }
    private val movieShowsService = MovieShowsService(movieShowsMapper)

    @Test
    fun `updateMovieShow should throw exception when movie show does not exist`() {
        val movieShowId = UUID.randomUUID()
        whenever(movieShowsMapper.update(any(), any(), any(), any())).thenReturn(null)

        assertFailure {
            movieShowsService.updateMovieShow(movieShowId, UUID.randomUUID(), OffsetDateTime.now(), BigDecimal.TEN)
        }.all {
            hasClass(RecordNotFoundException::class)
            hasMessage("Movie show: $movieShowId, was not found!")
        }
    }
}
