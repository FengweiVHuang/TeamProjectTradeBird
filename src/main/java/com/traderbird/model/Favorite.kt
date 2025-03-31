package com.traderbird.model

import jakarta.persistence.*

@Entity(name = "favorites")
class Favorite(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    var user: User = User(),

    @ManyToOne
    @JoinColumn(name = "post", nullable = false)
    var post: Post = Post()
) {
}