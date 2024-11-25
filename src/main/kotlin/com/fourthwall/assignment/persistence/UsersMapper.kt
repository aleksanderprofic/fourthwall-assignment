package com.fourthwall.assignment.persistence

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import java.util.UUID

@Mapper
interface UsersMapper {
    @Select(
        """
        select user_id, username, password, role
        from fourthwall_assignment.users 
        where username = #{username}
        """
    )
    fun getByUsername(@Param("username") username: String): User?
}

data class User(
    val userId: UUID,
    val username: String,
    val password: String,
    val role: String,
)
