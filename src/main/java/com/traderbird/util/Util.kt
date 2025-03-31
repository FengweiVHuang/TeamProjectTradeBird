package com.traderbird.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.traderbird.config.ApplicationContextProvider
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.nio.file.Files
import java.nio.file.Path
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun <T> GetSpringBean(clazz: Class<T>): T {
    return ApplicationContextProvider.getApplicationContext().getBean(clazz)
}

@OptIn(ExperimentalContracts::class)
fun <T> T?.isNull(): Boolean {
    contract {
        returns(true) implies (this@isNull == null)
        returns(false) implies (this@isNull != null)
    }
    return this == null
}

@OptIn(ExperimentalContracts::class)
fun <T> T?.notNull(): Boolean {
    contract {
        returns(true) implies (this@notNull != null)
        returns(false) implies (this@notNull == null)
    }
    return this != null
}

fun <T> T?.orDefault(provider: () -> T): T {
    return this ?: provider()
}

inline fun nonThrow(block: () -> Unit): Boolean {
    return try {
        block()
        true
    } catch (e: Exception) {
        false
    }
}

fun <T> T.asAny(): Any {
    return this as Any
}

typealias StandardResponse = ResponseEntity<Map<String, Any>>

fun StandardResponseEntityOk(vararg pairs: Pair<String, Any>): StandardResponse {
    return ResponseEntity.ok(mapOf(*pairs))
}

fun StandardResponseEntityBadRequest(vararg pairs: Pair<String, Any>): StandardResponse {
    return ResponseEntity.badRequest().body(mapOf(*pairs))
}

fun StandardResponseEntityNotFound(vararg pairs: Pair<String, Any>): StandardResponse {
    return ResponseEntity.notFound().build()
}

fun StandardResponseEntityUnauthorized(vararg pairs: Pair<String, Any>): StandardResponse {
    return ResponseEntity.status(401).body(mapOf(*pairs))
}

fun StandardResponseEntityForbidden(vararg pairs: Pair<String, Any>): StandardResponse {
    return ResponseEntity.status(403).body(mapOf(*pairs))
}

fun StandardResponseEntityInternalServerError(vararg pairs: Pair<String, Any>): StandardResponse {
    return ResponseEntity.status(500).body(mapOf(*pairs))
}

fun StandardResponseEntityServiceUnavailable(vararg pairs: Pair<String, Any>): StandardResponse {
    return ResponseEntity.status(503).body(mapOf(*pairs))
}

fun StandardResponseEntityNotImplemented(vararg pairs: Pair<String, Any>): StandardResponse {
    return ResponseEntity.status(501).body(mapOf(*pairs))
}

typealias StandardSimpleResponse = ResponseEntity<Unit>

val StandardSimpleResponseOk = ResponseEntity.ok().build<Unit>()

val StandardSimpleResponseBadRequest = ResponseEntity.badRequest().build<Unit>()

val StandardSimpleResponseNotFound = ResponseEntity.notFound().build<Unit>()

val StandardSimpleResponseUnauthorized = ResponseEntity.status(401).build<Unit>()

val StandardSimpleResponseForbidden = ResponseEntity.status(403).build<Unit>()

inline fun <reified T> parseJsonToList(json: String): List<T> {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(json, mapper.typeFactory.constructCollectionType(List::class.java, T::class.java))
}

inline fun <reified T> parseJsonSet(json: String): Set<T> {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(json, mapper.typeFactory.constructCollectionType(Set::class.java, T::class.java))
}

inline fun <reified T> parseJsonToMap(json: String): Map<String, T> {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(
        json,
        mapper.typeFactory.constructMapType(Map::class.java, String::class.java, T::class.java)
    )
}

fun responseToImage(filePath : Path, fileName : String) : ResponseEntity<Any> {
    return if (Files.exists(filePath) && Files.isReadable(filePath)) {
        val fileContent = Files.readAllBytes(filePath)
        ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"$fileName\"")
            .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
            .body(fileContent)
    } else {
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
    }
}

fun Regex.notMatches(input: CharSequence): Boolean {
    return !matches(input)
}