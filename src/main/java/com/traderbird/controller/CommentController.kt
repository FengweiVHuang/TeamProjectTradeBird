package com.traderbird.controller

import com.traderbird.model.*
import com.traderbird.service.CommentService
import com.traderbird.service.PostService
import com.traderbird.service.UserService
import com.traderbird.util.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/comments")
class CommentController(
    private val commentService: CommentService,
    private val postService: PostService
) {
    data class AddCommentToPostRequestData(
        val postId: Long,
        val content: String,
        val isPublic : Boolean = true
    )

    @PostMapping("/add_to_post")
    fun addCommentToPost(
        @RequestBody requestData: AddCommentToPostRequestData
    ): StandardSimpleResponse {
        val (postId, content, isPublic) = requestData
        val author = UserUtils.getNowAuthenticatedUser() ?: return StandardSimpleResponseBadRequest
        val comment = CommentBuilder()
            .parentPost(postService.findPostById(postId) ?: return StandardSimpleResponseBadRequest)
            .author(author)
            .content(content)
            .isPublic(isPublic)
            .build()

        commentService.save(comment)
        return StandardSimpleResponseOk
    }

    data class AddCommentToCommentRequestData(
        val commentId: Long,
        val content: String,
        val isPublic : Boolean = true
    )

    @PostMapping("/add_to_comment")
    fun addCommentToComment(
        @RequestBody requestData: AddCommentToCommentRequestData
    ): StandardSimpleResponse {
        val (commentId, content, isPublic) = requestData
        val author = UserUtils.getNowAuthenticatedUser() ?: return StandardSimpleResponseBadRequest
        val parentComment = commentService.getById(commentId) ?: return StandardSimpleResponseBadRequest
        val comment = CommentBuilder()
            .parentComment(parentComment)
            .author(author)
            .content(content)
            .isPublic(isPublic)
            .build()

        commentService.save(comment)
        return StandardSimpleResponseOk
    }

    @GetMapping("/get_comments_of_post_id/id={id}")
    fun getCommentsOfPostId(@PathVariable id: Long): StandardResponse {
        val commentRecursive = postService.findPostById(id)?.commentsAbstractedRecursive(
            UserUtils.getNowAuthenticatedUser()?.id?: 0
        ) ?: emptyList()
        return StandardResponseEntityOk("comments" to commentRecursive)
    }

    @GetMapping("/get_comments_of_comment_id/id={id}")
    fun getCommentsOfCommentId(@PathVariable id: Long): StandardResponse {
        val comment =
            commentService.getById(id) ?: return StandardResponseEntityBadRequest("reason" to "comment not found")
        val commentRecursive = comment.commentsAbstractedRecursive(
            UserUtils.getNowAuthenticatedUser()?.id?: 0
        )
        return StandardResponseEntityOk("comments" to commentRecursive)
    }

    @GetMapping("/get_comment_recursive/id={id}")
    fun getCommentRecursive(@PathVariable id: Long): StandardResponse {
        val comment =
            commentService.getById(id) ?: return StandardResponseEntityBadRequest("reason" to "comment not found")
        return StandardResponseEntityOk("comment" to AbstractedRecursiveCommentInformation(comment))
    }

    @GetMapping("/get_sub_comments_id_of_post_id/id={id}")
    fun getSubCommentsIdOfPostId(@PathVariable id: Long): StandardResponse {
        val post = postService.findPostById(id)?.comments?.map { it.id } ?: emptyList()
        return StandardResponseEntityOk("comments" to post, "count" to post.size)
    }

    @GetMapping("/get_sub_comments_id_of_comment_id/id={id}")
    fun getSubCommentsIdOfCommentId(@PathVariable id: Long): StandardResponse {
        val comment = commentService.getById(id)?.comments?.map { it.id } ?: emptyList()
        return StandardResponseEntityOk("comments" to comment, "count" to comment.size)
    }
}