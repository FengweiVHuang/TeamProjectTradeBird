package com.traderbird.model

import jakarta.persistence.*

@Entity(name = "comments")
class Comment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "author")
    var author: User? = null,

    @Column(name = "content", nullable = false)
    @Lob
    var content: String = "",

    @ManyToOne
    @JoinColumn(name = "parent_comment")
    var parentComment: Comment? = null,

    @OneToMany(mappedBy = "parentComment", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("created_at DESC")
    var comments: List<Comment> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "parent_post")
    var parentPost: Post? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @Column(name = "is_public", nullable = false)
    var isPublic: Boolean = true
)

data class CommentBuilder(
    var id: Long = 0,
    var author: User? = null,
    var content: String = "",
    var parentComment: Comment? = null,
    var comments: List<Comment> = mutableListOf(),
    var parentPost: Post? = null,
    var createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),
    var isPublic: Boolean = true
) {
    fun id(id: Long) = apply { this.id = id }
    fun author(author: User) = apply { this.author = author }
    fun content(content: String) = apply { this.content = content }
    fun parentComment(parentComment: Comment) = apply { this.parentComment = parentComment }
    fun parentPost(parentPost: Post) = apply { this.parentPost = parentPost }
    fun isPublic(isPublic: Boolean) = apply { this.isPublic = isPublic }

    fun build() = Comment(id, author, content, parentComment, comments, parentPost, createdAt, isPublic)
}

data class CommentInformationAbstracted(
    val id: Long,
    val authorName: String,
    val content: String,
    val createdAt: java.time.LocalDateTime,
    val isPubic: Boolean
)

fun Comment.abstract(): CommentInformationAbstracted {
    return CommentInformationAbstracted(id, author!!.username, content, createdAt, isPublic)
}

data class AbstractedRecursiveCommentInformation(
    val information: CommentInformationAbstracted,
    val subComments: List<AbstractedRecursiveCommentInformation>
) {
    constructor(comment: Comment) : this(
        comment.abstract(),
        comment.comments.map { AbstractedRecursiveCommentInformation(it) })
}


fun getAbsCommRecInfo(comment: Comment, accessContextId: Long)
        : AbstractedRecursiveCommentInformation? {
    if (comment.isPublic || comment.author!!.id == accessContextId ||
        (comment.parentPost?.author?.id ?: -1) == accessContextId || (comment.parentComment?.author?.id
            ?: -1) == accessContextId
    ) {
        return AbstractedRecursiveCommentInformation(
            comment.abstract(),
            comment.comments.mapNotNull {
                getAbsCommRecInfo(it, accessContextId)
            }
        )
    }
    return null
}

fun Comment.commentsAbstractedRecursive(
    accessContextId: Long
): List<AbstractedRecursiveCommentInformation> {
    return comments.mapNotNull { getAbsCommRecInfo(it, accessContextId) }
}

fun Post.commentsAbstractedRecursive(
    accessContextId: Long
): List<AbstractedRecursiveCommentInformation> {
    return comments.mapNotNull { getAbsCommRecInfo(it, accessContextId) }
}