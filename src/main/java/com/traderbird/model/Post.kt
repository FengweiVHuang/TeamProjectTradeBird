package com.traderbird.model

import com.traderbird.ksp.CollectFunction
import com.traderbird.service.FileService
import com.traderbird.service.ImageService
import com.traderbird.service.PostService
import com.traderbird.service.PostTagService
import com.traderbird.util.GetSpringBean
import com.traderbird.util.isNull
import jakarta.persistence.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Entity(name = "posts")
class Post(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "title", nullable = false)
    var title: String = "",

    @ManyToOne
    @JoinColumn(name = "author")
    var author: User? = null,

    @Column(name = "price", nullable = false)
    var price: Double = 0.0,

    @ElementCollection
    @Column(name = "tags", nullable = false)
    var tags: Set<Long> = mutableSetOf(),

    @Column(name = "content", nullable = false) @Lob
    var content: String = "",

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("created_at DESC")
    @JoinColumn(name = "parent_post")
    var comments: List<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "post", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var favorites: List<Favorite> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    var createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),
) {

}

class PostBuilder {
    var title: String? = null
    var author: User? = null
    var price: Double? = null
    var tags: Set<Long>? = null
    var content: String? = null
    var image: MultipartFile? = null

    fun title(title: String?) = apply { this.title = title }
    fun author(author: User?) = apply { this.author = author }
    fun price(price: Double?) = apply { this.price = price }
    fun tags(tags: Set<Long>?) = apply { this.tags = tags }
    fun content(content: String?) = apply { this.content = content }
    fun image(image: MultipartFile?) = apply { this.image = image }

    @Throws(IllegalStateException::class)
    fun build(): Post {
        if (author.isNull()) throw IllegalStateException("Author is required")
        val post = Post(0, title ?: "", author!!, price ?: 0.0, tags ?: mutableSetOf(), content ?: "")


        return post
    }

    fun buildAndSave(): Post {
        val post = build()
        if (image.isNull()) return post

        GetSpringBean(PostService::class.java).save(post)

        val fileService = GetSpringBean(FileService::class.java)
        fileService.convertMultipartFileToJpegAndSave("post_pictures", "${post.id}", image!!)

        return post
    }
}

object PostUtils {
    @JvmStatic
    val OnPostDeleteAction = mutableListOf<(Post) -> Unit>()

    @JvmStatic
    fun onPostDelete(post: Post) {
        OnPostDeleteAction.forEach { it -> it(post) }
    }

    @JvmStatic
    @CollectFunction(targetClass = PostUtils::class, targetField = "OnPostDeleteAction")
    fun deletePostPicture(post: Post) {
        val id = post.id

        GetSpringBean(FileService::class.java).deleteIfExists("post_pictures", "$id.jpeg")
    }

    data class PostInformationAbstracted(
        val id: Long,
        val title: String,
        val author: String,
        val price: Double,
        val tags: List<String>,
        val content: String,
        val createdAt: LocalDateTime
    )

    fun Post.abstract(): PostInformationAbstracted {
        return PostInformationAbstracted(id, title, author!!.username, price, tags.toList().mapNotNull {
            GetSpringBean(PostTagService::class.java).getLongToStringMap()[it]
        }, content, createdAt)
    }
}