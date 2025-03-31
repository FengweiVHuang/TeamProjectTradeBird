package com.traderbird.repository

import com.traderbird.model.Post
import com.traderbird.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findByTitleContaining(title: String): List<Post>
    fun findByPriceBetween(minPrice: Double, maxPrice: Double): List<Post>
    fun findByTagsIn(tags: MutableCollection<MutableSet<Long>>): List<Post>
    fun findByAuthor(author: User): List<Post>
}