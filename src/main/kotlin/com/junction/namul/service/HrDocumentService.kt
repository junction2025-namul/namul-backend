package com.junction.namul.service

import com.junction.namul.model.document.Document
import com.junction.namul.model.document.File
import com.junction.namul.model.dto.CategoryAndHDocument
import com.junction.namul.model.dto.DocumentInfo
import com.junction.namul.model.dto.UploadRequest
import com.junction.namul.repository.HrDocumentRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class HrDocumentService(
    private val hrDocumentRepository: HrDocumentRepository,
    private val categoryService: CategoryService
) {
    
    @Value("\${file.upload.directory:uploads}")
    private lateinit var uploadDirectory: String

    /**
     * 문서 생성
     */
    fun createDocument(req: UploadRequest): Document {
        val document = Document(
            categoryId = ObjectId(req.categoryId),
            title = null,
            isNewbieDoc = req.isNewbieDoc,
            createdAt = LocalDateTime.now()
        )
        return hrDocumentRepository.saveDocument(document)
    }
    
    fun uploadFile(documentId: ObjectId, file: MultipartFile, uploadedBy: String): File {
        validateFile(file)
        
        val uploadPath = createUploadDirectory()
        val fileName = this.generateUniqueFileName(file.originalFilename ?: "unknown")
        val filePath = uploadPath.resolve(fileName)
        
        try {
            Files.copy(file.inputStream, filePath)
        } catch (e: IOException) {
            throw RuntimeException("파일 저장 중 오류가 발생했습니다: ${e.message}")
        }
        
        val fileEntity = File(
            documentId = documentId,
            filename = fileName,
            filePath = filePath.toString(),
            fileSize = file.size,
            mimeType = file.contentType ?: "application/octet-stream",
            uploadDate = LocalDateTime.now(),
            uploadedBy = uploadedBy
        )
        
        return hrDocumentRepository.saveFile(fileEntity)
    }

    /**
     * 파일업로드
     */
    fun updateDocumentTitleWithFileName(documentId: ObjectId, originalFileName: String) {
        // 파일 확장자 제거하고 title로 사용
        val title = originalFileName.substringBeforeLast(".")
        hrDocumentRepository.updateDocumentTitle(documentId, title)
    }
    
    private fun validateFile(file: MultipartFile) {
        if (file.isEmpty) {
            throw IllegalArgumentException("파일이 비어있습니다")
        }
        
        if (file.contentType != "application/pdf") {
            throw IllegalArgumentException("PDF 파일만 업로드 가능합니다")
        }
        
        val maxSize = 10 * 1024 * 1024 // 10MB
        if (file.size > maxSize) {
            throw IllegalArgumentException("파일 크기는 10MB를 초과할 수 없습니다")
        }
    }
    
    private fun createUploadDirectory(): Path {
        val path = Paths.get(uploadDirectory)
        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }
        return path
    }
    
    fun findAllDocumentsGroupedByCategory(): List<CategoryAndHDocument> {
        val documents = hrDocumentRepository.findAllDocumentsGroupedByCategory()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        
        return documents
            .groupBy { it.categoryId }
            .map { (categoryId, docs) ->
                CategoryAndHDocument(
                    categoryId = categoryId.toHexString(),
                    category = categoryService.getCategoryName(categoryId),
                    documentInfo = docs.map { doc ->
                        DocumentInfo(
                            docId = doc.id.toHexString(),
                            title = doc.title ?: "제목 없음",
                            isNewbieDoc = doc.isNewbieDoc,
                            uploadDate = doc.createdAt.format(formatter) ?: ""
                        )
                    }
                )
            }
    }

    private fun generateUniqueFileName(originalFilename: String): String {
        val extension = originalFilename.substringAfterLast(".", "")
        val nameWithoutExtension = originalFilename.substringBeforeLast(".")
        val timestamp = System.currentTimeMillis()
        val uuid = UUID.randomUUID().toString().substring(0, 8)

        return "${nameWithoutExtension}_${timestamp}_${uuid}.${extension}"
    }
    
}