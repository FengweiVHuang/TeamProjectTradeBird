package com.traderbird.model

import jakarta.persistence.*

@Entity
@Table(name = "dictionary")
data class Dictionary(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "word", nullable = false, unique = true)
    val word: String
) {
    constructor() : this(0, "")
}