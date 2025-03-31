package com.traderbird.repository

import com.traderbird.model.Dictionary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DictionaryRepository : JpaRepository<Dictionary, Long> {
    fun findByWord(word: String): Dictionary?
}