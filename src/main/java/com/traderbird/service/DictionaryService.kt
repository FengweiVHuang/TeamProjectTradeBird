package com.traderbird.service

import com.traderbird.config.ApplicationContextProvider
import com.traderbird.model.Dictionary
import com.traderbird.repository.DictionaryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DictionaryService(
    private val dictionaryRepository: DictionaryRepository
) {
    fun idOf(word: String): Long? {
        val entry = dictionaryRepository.findByWord(word)
        return entry?.id
    }

    fun wordOf(id: Long): String? {
        val entry = dictionaryRepository.findById(id)
        return if (entry.isPresent) entry.get().word else null
    }

    fun addWord(word: String): Dictionary {
        val entry = Dictionary(0, word)
        return dictionaryRepository.save(entry)
    }

    fun removeWord(word: String) {
        val entry = dictionaryRepository.findByWord(word)
        if (entry != null) {
            dictionaryRepository.delete(entry)
        }
    }

    fun removeId(id: Long) {
        dictionaryRepository.deleteById(id)
    }

    fun addEntry(entry: Dictionary): Dictionary {
        return dictionaryRepository.save(entry)
    }

    fun entryOf(word: String): Dictionary? {
        return dictionaryRepository.findByWord(word)
    }

    fun entryOf(id: Long): Dictionary? {
        return dictionaryRepository.findById(id).orElse(null)
    }
}