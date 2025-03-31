package com.traderbird.repository

import com.traderbird.model.Comment
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByAuthorId(authorId: Long): List<Comment>

    fun findByParentCommentId(parentCommentId: Long): List<Comment>
}