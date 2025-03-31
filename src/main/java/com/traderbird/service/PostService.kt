package com.traderbird.service

import com.traderbird.model.Post
import com.traderbird.repository.PostRepository
import com.traderbird.util.notNull
import com.traderbird.util.orDefault
import org.springframework.stereotype.Service
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import jakarta.transaction.Transactional
import kotlin.collections.remove

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userService: UserService
) {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun findPostsByTitle(title: String, limit: Int): List<Post> {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder
        val cq: CriteriaQuery<Post> = cb.createQuery(Post::class.java)
        val root: Root<Post> = cq.from(Post::class.java)

        val predicates = mutableListOf<Predicate>()
        if (title.isNotEmpty()) {
            predicates.add(cb.like(root.get("title"), "%$title%"))
        }

        cq.where(cb.and(*predicates.toTypedArray()))
        val query = entityManager.createQuery(cq)
        if (limit > 0) {
            query.maxResults = limit
        }
        return query.resultList
    }

    fun findPostsByPriceRange(minPrice: Double, maxPrice: Double): List<Post> {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder
        val cq: CriteriaQuery<Post> = cb.createQuery(Post::class.java)
        val root: Root<Post> = cq.from(Post::class.java)

        cq.where(cb.between(root.get("price"), minPrice, maxPrice))
        return entityManager.createQuery(cq).resultList
    }

    fun findPostsByTags(tags: Set<Long>): List<Post> {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder
        val cq: CriteriaQuery<Post> = cb.createQuery(Post::class.java)
        val root: Root<Post> = cq.from(Post::class.java)

        val predicates = tags.map { tag -> cb.isMember(tag, root.get<Set<Long>>("tags")) }
        cq.where(cb.or(*predicates.toTypedArray()))
        return entityManager.createQuery(cq).resultList
    }

    fun findPostsByCombinedCriteria(
        title: String?,
        minPrice: Double?,
        maxPrice: Double?,
        tags: Set<Long>?,
        limit: Int?,
        allMatch: Boolean?, // true for all match, false for any match
    ): List<Post> {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder
        val cq: CriteriaQuery<Post> = cb.createQuery(Post::class.java)
        val root: Root<Post> = cq.from(Post::class.java)

        val predicates = mutableListOf<Predicate>()
        if (!title.isNullOrEmpty()) {
            predicates.add(cb.like(root.get("title"), "%$title%"))
        }
        if (minPrice.notNull() && maxPrice.notNull() && minPrice < maxPrice) {
            predicates.add(cb.between(root.get("price"), minPrice, maxPrice))
        }
        if (!tags.isNullOrEmpty()) {
            val tagPredicates = tags.map { tag -> cb.isMember(tag, root.get<Set<Long>>("tags")) }
            predicates.add(cb.or(*tagPredicates.toTypedArray()))
        }

        cq.where(if (allMatch.orDefault { true }) cb.and(*predicates.toTypedArray()) else cb.or(*predicates.toTypedArray()))
        val query = entityManager.createQuery(cq)
        if (limit.notNull() && limit > 0) {
            query.maxResults = limit
        }
        return query.resultList
    }

    fun save(post: Post) {
        postRepository.save(post)
    }

    fun findPostsByAuthorId(authorId: Long): List<Post> {
        return userService.getUserById(authorId)?.let { postRepository.findByAuthor(it) } ?: emptyList()
    }

    fun findPostById(id: Long): Post? {
        return postRepository.findById(id).orElse(null)
    }

    @Transactional
    fun deletePostById(id: Long) {
        val post = postRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Post not found") }
        entityManager.remove(
            if (entityManager.contains(post)) post
            else entityManager.merge(post)
        )
    }
}

