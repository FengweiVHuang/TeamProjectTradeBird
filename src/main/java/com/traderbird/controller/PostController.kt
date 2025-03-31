package com.traderbird.controller

import com.traderbird.model.PostBuilder
import com.traderbird.model.PostUtils
import com.traderbird.model.PostUtils.abstract
import com.traderbird.model.UserUtils
import com.traderbird.service.FavoriteService
import com.traderbird.service.PostService
import com.traderbird.service.UserService
import com.traderbird.util.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.nio.file.Paths

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService,
    private val userService: UserService,
    private val favoriteService: FavoriteService
) {
    @PostMapping("/create", consumes = ["multipart/form-data"])
    fun savePost(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("itemName") itemName: String,
        @RequestParam("price") price: Double,
        @RequestParam("description") description: String,
        @RequestParam("tags") tags: String
    ): StandardSimpleResponse {
        val user = UserUtils.getNowAuthenticatedUser()!!

        val builder = PostBuilder()

        builder
            .title(itemName)
            .author(user)
            .price(price)
            .tags(parseJsonSet(tags))
            .content(description)
            .image(file)

        try {
            builder.buildAndSave()
        } catch (ignored: Exception) {
            return StandardSimpleResponseBadRequest
        }

        return StandardSimpleResponseOk
    }

    // All fields are optional
    data class RetrievePostsRestBody(
        val title: String?,           // Contains, match all if null
        val minPrice: Double?,        // Greater than or equal to, 0.0 if null
        val maxPrice: Double?,        // Less than or equal to, Double.MAX_VALUE if null
        val tags: Set<Long>?,         // Frontend should send a JSON array of tag IDs, match all if null
        val limit: Int?,              // 0 means no limit, null means no limit
        val allMatch: Boolean?        // true means all criteria must match, false means any criteria can match, null -> true
    )

    @PostMapping("/retrieve")
    fun retrievePosts(@RequestBody body: RetrievePostsRestBody): StandardResponse {
        var (title, minPrice, maxPrice, tags, limit, allMatch) = body

        if ((minPrice.isNull() || maxPrice.isNull()) && !(minPrice.isNull() && maxPrice.isNull())) {
            minPrice = minPrice.orDefault { 0.0 }
            maxPrice = maxPrice.orDefault { Double.MAX_VALUE }
        }

        val result = postService.findPostsByCombinedCriteria(
            title,
            minPrice,
            maxPrice,
            tags,
            limit,
            allMatch
        ).map { it.abstract() }

        return StandardResponseEntityOk(
            "posts" to result,
            "count" to result.size
        )
    }

    @GetMapping("/get_post_details/id={id}")
    fun getPostDetails(@PathVariable id: Long): StandardResponse {
        val post = postService.findPostById(id) ?: return StandardResponseEntityBadRequest("reason" to "Post not found")

        return StandardResponseEntityOk(
            "post" to post.abstract()
        )
    }

    @GetMapping("/get_post_image/id={id}")
    fun getPostImage(@PathVariable id: Long): ResponseEntity<Any> {
        val fileName = "$id.jpeg"
        val filePath: Path = Paths.get("file_storage/post_pictures").resolve(fileName).normalize()

        return responseToImage(filePath, fileName)
    }

    @GetMapping("/get_post_of_user/id={id}")
    fun getPostOfUser(@PathVariable id: Long): StandardResponse {
        val posts = postService.findPostsByAuthorId(id).map { it.abstract() }

        return StandardResponseEntityOk(
            "posts" to posts,
            "count" to posts.size
        )
    }

    data class FavoritePostRestBody(
        val postId: Long
    )

    @PostMapping("/favorite")
    fun favoritePost(@RequestBody body: FavoritePostRestBody): StandardSimpleResponse {
        val user = UserUtils.getNowAuthenticatedUser() ?: return StandardSimpleResponseBadRequest
        val post = postService.findPostById(body.postId) ?: return StandardSimpleResponseBadRequest

        favoriteService.addFavorite(user, post)
        return StandardSimpleResponseOk
    }

    @GetMapping("/get_post_favorite_user_ids/id={id}")
    fun getFavoriteUserIds(@PathVariable id: Long): StandardResponse {
        val favorites =
            postService.findPostById(id)?.let { favoriteService.getByPost(it) }
                ?: return StandardResponseEntityBadRequest("reason" to "Post not found")
        val ids = favorites.map { it.user.id }

        return StandardResponseEntityOk(
            "userIds" to ids,
            "count" to ids.size
        )
    }

    @GetMapping("/get_user_favorite_posts/id={id}")
    fun getFavoritePostIds(@PathVariable id: Long): StandardResponse {
        val user = userService.getUserById(id) ?: return StandardResponseEntityBadRequest("reason" to "User not found")
        val favorites = favoriteService.getByUser(user)
        val posts = favorites.mapNotNull {
            it.post.id.let {
                postService.findPostById(it)?.abstract()
            }
        }

        return StandardResponseEntityOk(
            "posts" to posts,
            "count" to posts.size
        )
    }

    data class CancelFavoritePostRestBody(
        val postId: Long
    )

    @PostMapping("/cancel_favorite")
    fun cancelFavorite(@RequestBody body: CancelFavoritePostRestBody): StandardSimpleResponse {
        val user = UserUtils.getNowAuthenticatedUser() ?: return StandardSimpleResponseBadRequest
        val post = postService.findPostById(body.postId) ?: return StandardSimpleResponseBadRequest

        val favorites = favoriteService.getByUser(user)
        favorites.find {
            it.post.id == post.id
        } ?: return StandardSimpleResponseBadRequest

        favoriteService.removeFavorite(user, post)

        return StandardSimpleResponseOk
    }

    @PostMapping("/delete/id={id}")
    fun deletePost(@PathVariable id: Long): StandardSimpleResponse {
        val post = postService.findPostById(id) ?: return StandardSimpleResponseBadRequest
        val user = UserUtils.getNowAuthenticatedUser() ?: return StandardSimpleResponseBadRequest

        if (post.author!!.id != user.id) {
            return StandardSimpleResponseForbidden
        }

        PostUtils.onPostDelete(post)
        postService.deletePostById(post.id)
        return StandardSimpleResponseOk
    }
}