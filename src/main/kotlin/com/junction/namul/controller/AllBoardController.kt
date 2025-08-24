package com.junction.namul.controller

import com.junction.namul.model.dto.DocumentDetail
import com.junction.namul.model.dto.DocumentDetailWithId
import com.junction.namul.service.AllBoardService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/all-board")
@Tag(name = "Management Document For Newbie", description = "사용자(Hr 제외 한 재직자)")
class AllBoardController(
    private val allBoardService: AllBoardService
) {

    @GetMapping("/newbie-documents")
    @Operation(summary = "신규 입사자용 문서 조회", description = "isNewbie가 true인 문서들을 조회합니다")
    fun getNewbieDocuments(): List<DocumentDetailWithId>? {
        return allBoardService.getNewbieDocuments()
    }
}