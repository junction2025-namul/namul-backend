package com.junction.namul.controller

import com.junction.namul.model.document.Namul
import com.junction.namul.service.NamulService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/namul")
@Tag(name = "Namul", description = "Namul 팀 데이터 관리 API")
class NamulController(
    private val namulService: NamulService
) {
    
    @PostMapping
    @Operation(summary = "데이터 저장", description = "새로운 데이터를 저장합니다")
    fun save(@RequestBody namul: Namul): ResponseEntity<Namul> {
        val savedNamul = namulService.save(namul)
        return ResponseEntity.ok(savedNamul)
    }

    @GetMapping
    @Operation(summary = "데이터 조회", description = "모든 데이터를 조회합니다")
    fun findAll(): ResponseEntity<List<Namul>> {
        val namuls = namulService.findAll()
        return ResponseEntity.ok(namuls)
    }
}