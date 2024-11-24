package com.fourthwall.assignment.persistence

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import java.time.OffsetDateTime
import java.util.UUID

@Mapper
interface MoviesMapper {
    @Select(
        """
        select movie_id, imdb_id, title, created_at
        from fourthwall_assignment.movies 
        """
    )
    fun getAll(): Collection<Movie>

    @Select(
        """
        select movie_id, imdb_id, title, created_at
        from fourthwall_assignment.movies 
        where movie_id = #{movieId}
        """
    )
    fun getByMovieId(@Param("movieId") movieId: UUID): Movie?
}

data class Movie(
    val movieId: UUID,
    val imdbId: String,
    val title: String,
    val createdAt: OffsetDateTime,
)
