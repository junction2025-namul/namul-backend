package com.junction.namul.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/hello")
class HelloController {

    @GetMapping
    fun getHello(): ResponseEntity<HelloResponse> {
        return ResponseEntity.ok(HelloResponse("Hello, World!", LocalDateTime.now()))
    }

    @GetMapping("/{name}")
    fun getHelloWithName(@PathVariable name: String): ResponseEntity<HelloResponse> {
        return ResponseEntity.ok(HelloResponse("Hello, $name!", LocalDateTime.now()))
    }

    @PostMapping
    fun postHello(@RequestBody request: HelloRequest): ResponseEntity<HelloResponse> {
        return ResponseEntity.ok(HelloResponse("Hello, ${request.name}!", LocalDateTime.now()))
    }
}

data class HelloResponse(
    val message: String,
    val timestamp: LocalDateTime
)

data class HelloRequest(
    val name: String
)