package com.traderbird.service

import com.traderbird.model.Comment
import com.traderbird.repository.CommentRepository
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository
) {
    fun save(comment: Comment) {
        commentRepository.save(comment)
    }

    fun getById(id: Long): Comment? {
        return commentRepository.findById(id).orElse(null)
    }
}