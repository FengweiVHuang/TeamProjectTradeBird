package com.traderbird.repository

import com.traderbird.model.Favorite
import com.traderbird.model.Post
import com.traderbird.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface FavoriteRepository : JpaRepository<Favorite, Long> {
    fun findByUserId(userId: Long): List<Favorite>
    fun findByPostId(postId: Long): List<Favorite>
    fun removeByUserId(userId: Long)
    fun removeByPostId(postId: Long)

    fun existsByUserAndPost(user: User, post: Post): Boolean

    fun removeByUserAndPost(user: User, post: Post)
}