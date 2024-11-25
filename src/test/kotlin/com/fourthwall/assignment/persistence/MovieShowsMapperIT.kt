package com.fourthwall.assignment.persistence

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualByComparingTo
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.fourthwall.assignment.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.UUID

class MovieShowsMapperIT @Autowired constructor(
    private val movieShowsMapper: MovieShowsMapper,
) : AbstractIntegrationTest() {

    @Test
    fun `insert should create movie show`() {
        val movieShowId = UUID.randomUUID()
        val movieId = UUID.randomUUID()
        val startsAt = OffsetDateTime.now(ZoneId.of("UTC"))
        val price = BigDecimal.TEN

        movieShowsMapper.insert(MovieShow(movieShowId, movieId, startsAt, price))

        val createdMovieShow = movieShowsMapper.getByMovieId(movieId).single()
        assertThat(createdMovieShow.movieShowId).isEqualTo(movieShowId)
        assertThat(createdMovieShow.movieId).isEqualTo(movieId)
        assertThat(createdMovieShow.startsAt).isEqualTo(startsAt)
        assertThat(createdMovieShow.price).isEqualTo(price)
    }

    @Test
    fun `insert should return created movie show`() {
        val movieShowId = UUID.randomUUID()
        val movieId = UUID.randomUUID()
        val startsAt = OffsetDateTime.now(ZoneId.of("UTC"))
        val price = BigDecimal.TEN

        val createdMovieShow = movieShowsMapper.insert(MovieShow(movieShowId, movieId, startsAt, price))

        assertThat(createdMovieShow.movieShowId).isEqualTo(movieShowId)
        assertThat(createdMovieShow.movieId).isEqualTo(movieId)
        assertThat(createdMovieShow.startsAt).isEqualByComparingTo(startsAt)
        assertThat(createdMovieShow.price).isEqualTo(price)
    }

    @Test
    fun `update should replace movie show`() {
        val movieShowId = UUID.randomUUID()
        val movieId = UUID.randomUUID()
        val startsAt = OffsetDateTime.now(ZoneId.of("UTC"))
        val price = BigDecimal.TEN
        movieShowsMapper.insert(MovieShow(movieShowId, UUID.randomUUID(), startsAt.minusDays(1), BigDecimal.ONE))

        movieShowsMapper.update(movieShowId, movieId, startsAt, price)

        val updatedMovieShow = movieShowsMapper.getByMovieId(movieId).single()
        assertThat(updatedMovieShow.movieShowId).isEqualTo(movieShowId)
        assertThat(updatedMovieShow.movieId).isEqualTo(movieId)
        assertThat(updatedMovieShow.startsAt).isEqualTo(startsAt)
        assertThat(updatedMovieShow.price).isEqualTo(price)
    }

    @Test
    fun `update should return updated movie show`() {
        val movieShowId = UUID.randomUUID()
        val movieId = UUID.randomUUID()
        val startsAt = OffsetDateTime.now(ZoneId.of("UTC"))
        val price = BigDecimal.TEN
        movieShowsMapper.insert(MovieShow(movieShowId, UUID.randomUUID(), startsAt.minusDays(1), BigDecimal.ONE))

        val updatedMovieShow = movieShowsMapper.update(movieShowId, movieId, startsAt, price)

        assertThat(updatedMovieShow).isNotNull().given {
            assertThat(it.movieShowId).isEqualTo(movieShowId)
            assertThat(it.movieId).isEqualTo(movieId)
            assertThat(it.startsAt).isEqualTo(startsAt)
            assertThat(it.price).isEqualTo(price)
        }
    }

    @Test
    fun `update should return null when movie show does not exist`() {
        val result = movieShowsMapper.update(UUID.randomUUID(), UUID.randomUUID(), OffsetDateTime.now(), BigDecimal.TEN)

        assertThat(result).isNull()
    }

    @Test
    fun `getByMovieId should return movie shows when they exist`() {
        val startsAt = OffsetDateTime.now()
        val movieId = UUID.randomUUID()
        movieShowsMapper.insert(MovieShow(UUID.randomUUID(), UUID.randomUUID(), startsAt, BigDecimal.TEN))
        movieShowsMapper.insert(MovieShow(UUID.randomUUID(), movieId, startsAt, BigDecimal.ZERO))
        movieShowsMapper.insert(MovieShow(UUID.randomUUID(), UUID.randomUUID(), startsAt, BigDecimal.ONE))
        movieShowsMapper.insert(MovieShow(UUID.randomUUID(), movieId, startsAt, BigDecimal.ONE))

        val result = movieShowsMapper.getByMovieId(movieId)

        assertThat(result).hasSize(2)
    }

    @Test
    fun `getByMovieId should return empty response when movie shows do not exist for movie`() {
        val startsAt = OffsetDateTime.now()
        movieShowsMapper.insert(MovieShow(UUID.randomUUID(), UUID.randomUUID(), startsAt, BigDecimal.TEN))
        movieShowsMapper.insert(MovieShow(UUID.randomUUID(), UUID.randomUUID(), startsAt, BigDecimal.ZERO))

        val result = movieShowsMapper.getByMovieId(UUID.randomUUID())

        assertThat(result).isEmpty()
    }
}
