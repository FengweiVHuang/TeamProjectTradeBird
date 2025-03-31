package com.traderbird.controller

import com.traderbird.wrappers.RestString
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/home")
class HomeController {
    @PostMapping("/testpost")
    fun testPost(@RequestBody data: RestString): String {
        println("Received data: $data")
        return "Hello, $data!"
    }

    @GetMapping("/testget")
    fun testGet(): String {
        return "Hello, World!"
    }
}