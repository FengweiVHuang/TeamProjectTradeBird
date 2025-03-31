package com.traderbird.service

import com.traderbird.model.Favorite
import com.traderbird.model.Post
import com.traderbird.model.User
import com.traderbird.repository.FavoriteRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class FavoriteService(
    private val favoriteRepository: FavoriteRepository
) {
    fun addFavorite(user: User, post: Post) {
        if (favoriteRepository.existsByUserAndPost(user, post)) {
            return
        }
        favoriteRepository.save(Favorite(user = user, post = post))
    }

    fun getByUser(user: User): List<Favorite> {
        return favoriteRepository.findByUserId(user.id)
    }

    fun getByPost(post: Post): List<Favorite> {
        return favoriteRepository.findByPostId(post.id)
    }

    @Transactional
    fun removeFavorite(user: User, post: Post) {
        favoriteRepository.removeByUserAndPost(user, post)
    }
}