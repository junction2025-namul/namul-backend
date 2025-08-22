package com.junction.namul.controller

import com.junction.namul.model.Namul
import com.junction.namul.model.dto.namulRequest
import com.junction.namul.service.NamulService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/namul")
class NamulController(
    private val namulService: NamulService
) {
    
    @PostMapping
    fun save(@RequestBody namul: Namul): ResponseEntity<Namul> {
        val savedNamul = namulService.save(namul)
        return ResponseEntity.ok(savedNamul)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<Namul>> {
        val namuls = namulService.findAll()
        return ResponseEntity.ok(namuls)
    }
}