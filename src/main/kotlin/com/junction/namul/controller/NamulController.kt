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
    fun save(@RequestBody namul: namulRequest): ResponseEntity<Namul> {
        val savedNamul = namulService.save(Namul(
            name = namul.name,
            description = namul.description,
        ))
        return ResponseEntity.ok(savedNamul)
    }
}