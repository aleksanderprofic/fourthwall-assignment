package com.fourthwall.assignment.persistence

import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface UsersTestMapper {
    @Insert(
        """
        insert into fourthwall_assignment.users (user_id, username, password, role) 
        values (#{user.userId}, #{user.username}, #{user.password}, #{user.role})
        """
    )
    fun insert(@Param("user") user: User)
}

@Mapper
interface MoviesTestMapper {
    @Insert(
        """
        insert into fourthwall_assignment.movies (movie_id, imdb_id, title)
        values (#{movie.movieId}, #{movie.imdbId}, #{movie.title})
        """
    )
    fun insert(@Param("movie") movie: Movie)

    @Delete("delete from fourthwall_assignment.movies")
    fun deleteAll()
}
