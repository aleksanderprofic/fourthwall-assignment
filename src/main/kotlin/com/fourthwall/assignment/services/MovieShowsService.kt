package com.fourthwall.assignment.services

import com.fourthwall.assignment.persistence.MovieShow
import com.fourthwall.assignment.persistence.MovieShowsMapper
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@Service
class MovieShowsService(
    private val movieShowsMapper: MovieShowsMapper,
) {
    fun getByMovieId(movieId: UUID): Collection<MovieShow> = movieShowsMapper.getByMovieId(movieId)

    fun createMovieShow(movieId: UUID, time: OffsetDateTime, price: BigDecimal): MovieShow {
        val createdAt = OffsetDateTime.now()

        return movieShowsMapper.insert(
            MovieShow(
                movieShowId = UUID.randomUUID(),
                movieId = movieId,
                startsAt = time,
                price = price,
                createdAt = createdAt,
                lastUpdatedAt = createdAt
            )
        )
    }

    fun updateMovieShow(movieShowId: UUID, movieId: UUID, startsAt: OffsetDateTime, price: BigDecimal): MovieShow {
        return movieShowsMapper.update(movieShowId = movieShowId, movieId = movieId, startsAt = startsAt, price = price)
    }
}
