package com.junction.namul.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.junction.namul.model.dto.DocumentParsing
import com.junction.namul.repository.FileRepository
import com.junction.namul.repository.HrDocumentRepository
import kotlinx.coroutines.*
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import java.nio.file.Paths

@Service
class ParsingService(
    private val fileRepository: FileRepository,
    private val hrDocumentRepository: HrDocumentRepository,
    private val webClient: WebClient,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(ParsingService::class.java)
    
    @Value("\${upstage.api.key}")
    private lateinit var upstageApiKey: String
    
    fun documentParsing(documentIds: List<String>): Boolean {
        val files = fileRepository.findByIds(documentIds.map { id -> ObjectId(id) })
        
        // 비동기로 각 파일 처리
        runBlocking {
            files.map { file ->
                async {
                    file.documentId?.let { processFile(file.filePath, it) }
                }
            }.awaitAll()
        }
        
        return true
    }
    
    private suspend fun processFile(filePath: String, documentId: ObjectId) {
        try {
            val formData: MultiValueMap<String, Any> = LinkedMultiValueMap()
            
            // 파일 추가
            val file = FileSystemResource(Paths.get(filePath))
            formData.add("document", file)
            formData.add("model", "document-parse-250618")
            formData.add("ocr", "auto")
            formData.add("chart_recognition", "true")
            formData.add("output_formats", "[\"html\",\"markdown\"]")
            formData.add("base64_encoding", "[\"figure\"]")
            formData.add("coordinates", "true")
            
            val response = webClient.post()
                .uri("https://api.upstage.ai/v1/document-digitization")
                .header("Authorization", "Bearer $upstageApiKey")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
            
            // JSON에서 content만 추출
            if (response != null) {
                val jsonNode = objectMapper.readTree(response)
                val content = jsonNode.get("content")
                
                logger.info("=== Parsing Content for file: $filePath ===")
                logger.info("HTML: ${content?.get("html")?.asText()?.take(200)}...")
                logger.info("Markdown: ${content?.get("markdown")?.asText()?.take(200)}...")
                logger.info("=== End of Content ===")
                val htmlContent = content?.get("html")?.asText() ?: ""
                val markdownContent = content?.get("markdown")?.asText() ?: ""
                
                val parsing = DocumentParsing(
                    html = htmlContent,
                    markdown = markdownContent
                )

                // Document 업데이트
                hrDocumentRepository.updateDocumentParsing(documentId, htmlContent, markdownContent)
                logger.info("Document $documentId updated with parsing results")
            }
            
        } catch (e: Exception) {
            logger.error("Error processing file $filePath: ${e.message}", e)
        }
    }
}