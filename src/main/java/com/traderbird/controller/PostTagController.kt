package com.traderbird.controller

import com.traderbird.service.PostTagService
import com.traderbird.util.StandardResponse
import com.traderbird.util.StandardResponseEntityBadRequest
import com.traderbird.util.StandardResponseEntityOk
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/post-tag")
class PostTagController(
    private val postTagService: PostTagService
) {
    data class PostTagRequestLongString(
        val id: Long,
        val name: String
    )

    private var getAllCache: StandardResponse? = null

    @PostMapping("/add")
    fun addPostTag(@RequestBody postTagRequest: PostTagRequestLongString): StandardResponse {
        getAllCache = null
        return StandardResponseEntityOk(
            "success" to true,
            "id" to postTagService.setPostTag(postTagRequest.id, postTagRequest.name)
        )
    }

    data class DeletePostTagRequest(
        val name: String
    )

    @PostMapping("/delete")
    fun deletePostTag(@RequestBody deletePostTagRequest: DeletePostTagRequest): StandardResponse {
        getAllCache = null
        return if (postTagService.removePostTag(deletePostTagRequest.name)) {
            StandardResponseEntityOk("success" to true)
        } else {
            StandardResponseEntityBadRequest("success" to false, "message" to "Post tag not found")
        }
    }

    @GetMapping("/get-all")
    fun getAllPostTags(): StandardResponse {
        if (getAllCache != null) {
            return getAllCache!!
        }
        val stringToLongMap = postTagService.getStringToLongMap()
        getAllCache = StandardResponseEntityOk(
            "postTags" to stringToLongMap,
            "size" to stringToLongMap.size
        )
        return getAllCache!!
    }
}