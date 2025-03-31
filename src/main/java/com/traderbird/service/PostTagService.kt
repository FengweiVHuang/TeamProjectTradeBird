package com.traderbird.service

import com.traderbird.model.PostTag
import com.traderbird.repository.PostTagRepository
import com.traderbird.util.StandardResponse
import com.traderbird.util.nonThrow
import org.springframework.stereotype.Service

@Service
class PostTagService(
    private val postTagRepository: PostTagRepository
) {
    private var longToStringMap: Map<Long, String>? = null

    fun getStringToLongMap(): Map<String, Long> {
        return postTagRepository.findAll().associate { it.name to it.id }
    }

    fun getLongToStringMap(): Map<Long, String> {
        if (longToStringMap == null) {
            longToStringMap = postTagRepository.findAll().associate { it.id to it.name }
        }

        return longToStringMap!!
    }

    fun getPostTagId(name: String): Long? {
        return postTagRepository.findByName(name)?.id
    }

    fun getPostTagName(id: Long): String? {
        return postTagRepository.findById(id).orElse(null)?.name
    }

    fun setPostTag(id: Long, name: String): Long {
        try {
            return postTagRepository.save(PostTag(id, name)).id
        } catch (_: Exception) {
        } finally {
            longToStringMap = null
        }
        return createPostTag(name)
    }

    fun createPostTag(name: String): Long {
        if (postTagRepository.existsByName(name)) {
            return postTagRepository.findByName(name)!!.id
        }
        try {
            val postTag = PostTag(name = name)
            return postTagRepository.save(postTag).id
        } finally {
            longToStringMap = null
        }
    }

    fun removePostTag(name: String): Boolean {
        if (!postTagRepository.existsByName(name)) {
            return false
        }

        try {
            postTagRepository.removeByName(name)
            return true
        } finally {
            longToStringMap = null
        }
    }

}