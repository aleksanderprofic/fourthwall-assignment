package com.fourthwall.assignment.persistence

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.fourthwall.assignment.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class MoviesMapperIT @Autowired constructor(
    private val moviesMapper: MoviesMapper,
    private val moviesTestMapper: MoviesTestMapper
) : AbstractIntegrationTest() {
    @Test
    fun `getAll should return empty response when no movies in database`() {
        moviesTestMapper.deleteAll()

        val result = moviesMapper.getAll()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getAll should return all movies from database`() {
        moviesTestMapper.deleteAll()
        moviesTestMapper.insert(Movie(UUID.randomUUID(), "imdbId1", "title1"))
        moviesTestMapper.insert(Movie(UUID.randomUUID(), "imdbId2", "title2"))
        moviesTestMapper.insert(Movie(UUID.randomUUID(), "imdbId3", "title3"))

        val result = moviesMapper.getAll()

        assertThat(result).hasSize(3)
    }

    @Test
    fun `getByMovieId should return movie when it exists`() {
        val movieId = UUID.randomUUID()
        val imdbId = "imdbId"
        val title = "testTitle"
        moviesTestMapper.insert(Movie(movieId, imdbId, title))

        val result = moviesMapper.getByMovieId(movieId)

        assertThat(result).isNotNull().given {
            assertThat(it.movieId).isEqualTo(movieId)
            assertThat(it.imdbId).isEqualTo(imdbId)
            assertThat(it.title).isEqualTo(title)
        }
    }

    @Test
    fun `getByMovieId should return null when user does not exist`() {
        val result = moviesMapper.getByMovieId(UUID.randomUUID())

        assertThat(result).isNull()
    }
}
