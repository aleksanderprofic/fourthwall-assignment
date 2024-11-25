package com.fourthwall.assignment.persistence

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@Mapper
interface MovieShowsMapper {
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    @Select(
        """
        insert into fourthwall_assignment.movie_shows (movie_show_id, movie_id, starts_at, price) 
        values (#{movieShow.movieShowId}, #{movieShow.movieId}, #{movieShow.startsAt}, #{movieShow.price})
        returning movie_show_id, movie_id, starts_at, price
        """
    )
    fun insert(@Param("movieShow") movieShow: MovieShow): MovieShow

    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    @Select(
        """
        update fourthwall_assignment.movie_shows
        set movie_id = #{movieId}, starts_at = #{startsAt}, price = #{price}
        where movie_show_id = #{movieShowId}
        returning movie_show_id, movie_id, starts_at, price
        """
    )
    fun update(
        @Param("movieShowId") movieShowId: UUID,
        @Param("movieId") movieId: UUID,
        @Param("startsAt") startsAt: OffsetDateTime,
        @Param("price") price: BigDecimal,
    ): MovieShow?

    @Select(
        """
        select movie_show_id, movie_id, starts_at, price
        from fourthwall_assignment.movie_shows
        where movie_id = #{movieId}
        """
    )
    fun getByMovieId(@Param("movieId") movieId: UUID): Collection<MovieShow>
}

data class MovieShow(
    val movieShowId: UUID,
    val movieId: UUID,
    val startsAt: OffsetDateTime,
    val price: BigDecimal,
)
