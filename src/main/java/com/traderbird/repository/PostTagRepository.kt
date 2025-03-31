package com.traderbird.repository

import com.traderbird.model.PostTag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostTagRepository : JpaRepository<PostTag, Long> {
    fun findByName(name: String): PostTag?

    fun existsByName(name: String): Boolean

    fun removeByName(name: String)
}