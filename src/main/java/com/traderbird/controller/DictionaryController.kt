package com.traderbird.controller

import com.traderbird.model.Dictionary
import com.traderbird.service.DictionaryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dictionary")
class DictionaryController(
    private val dictionaryService: DictionaryService
) {
    @PostMapping("/add")
    fun addEntry(@RequestBody dictionary: Dictionary): ResponseEntity<Dictionary> {
        return dictionaryService.entryOf(dictionary.id)?.let {
            ResponseEntity.badRequest()
                .body(it)
        } ?: ResponseEntity.ok(dictionaryService.addEntry(dictionary))
    }
}