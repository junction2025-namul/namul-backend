package com.junction.namul.controller

import com.junction.namul.model.dto.CategoryAndHDocument
import com.junction.namul.model.dto.DocumentInfo
import com.junction.namul.model.dto.FileUploadResponseDTO
import com.junction.namul.model.dto.UploadRequest
import com.junction.namul.service.HrDocumentService
import com.junction.namul.service.ParsingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/hr-documents")
@Tag(name = "Management Document", description = "hr 매니저 문서 관리")
class HrDocumentController(
    private val hrDocumentService: HrDocumentService,
    private val parsingService: ParsingService
) {

    @GetMapping
    fun findHrDocuments(): List<CategoryAndHDocument> {
        return hrDocumentService.findAllDocumentsGroupedByCategory()
    }

    @PostMapping("/upload")
    @Operation(summary = "PDF 파일 업로드", description = "문서를 먼저 저장한 후 PDF 파일을 업로드합니다")
    fun fileUpload(
        @RequestPart("file") file: MultipartFile,
        @RequestPart request: UploadRequest,
    ): FileUploadResponseDTO {
        val document = hrDocumentService.createDocument(request)
        val fileEntity = hrDocumentService.uploadFile(document.id, file, "test")

        // 파일 원본 이름으로 Document title 업데이트
        file.originalFilename?.let { originalName ->
            hrDocumentService.updateDocumentTitleWithFileName(document.id, originalName)
        }

        val responseData = FileUploadResponseDTO(
            documentId = document.id.toString(),
            fileId = fileEntity.id.toString(),
            fileName = fileEntity.filename,
            originalName = file.originalFilename,
            fileSize = fileEntity.fileSize
        )

        return responseData
    }

    @PutMapping("/parsing")
    fun documentParsing(
        @RequestBody documentIds: List<String>
    ): Boolean = parsingService.documentParsing(documentIds)

}