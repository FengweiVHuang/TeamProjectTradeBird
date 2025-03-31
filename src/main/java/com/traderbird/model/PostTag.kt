package com.traderbird.model

import jakarta.persistence.*

@Entity
class PostTag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, unique = true)
    var name: String = ""
) {
}