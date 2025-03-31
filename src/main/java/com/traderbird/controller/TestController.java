package com.traderbird.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test/empty")
    public int handleEmptyRequest() {
        System.out.println("Received an empty request");
        return 0;
    }

    @GetMapping("/test/random")
    public int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(100); // Returns a random number between 0 and 99
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/test/post")
    public String handlePostRequest(@RequestBody String body) {
        System.out.println("Received a POST request with body: " + body);
        return "Received a POST request with body: " + body;
    }
}