package com.junction.namul.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.junction.namul.repository.FileRepository
import com.junction.namul.repository.HrDocumentRepository
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.openai.OpenAiChatModel
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
    private val objectMapper: ObjectMapper,
    private val chatModel: OpenAiChatModel
) {
    private val logger = LoggerFactory.getLogger(ParsingService::class.java)
    private val chatClient = ChatClient.create(chatModel)

    @Value("\${upstage.api.key}")
    private lateinit var upstageApiKey: String

    fun documentParsing(documentIds: List<String>): Boolean {
        logger.info("=== Starting document parsing for ${documentIds.size} documents ===")
        val files = fileRepository.findByIds(documentIds.map { id -> ObjectId(id) })
        logger.info("Found ${files.size} files to process")

        // 순차적으로 각 파일 처리
        files.forEach { file ->
            logger.info("Processing file: ${file.filePath} for document: ${file.documentId}")
            file.documentId?.let { processFile(file.filePath, it) }
        }

        logger.info("=== Document parsing completed ===")
        return true
    }

    private fun processFile(filePath: String, documentId: ObjectId) {
        try {
            logger.info(">>> Starting API call for file: $filePath")
            val formData: MultiValueMap<String, Any> = LinkedMultiValueMap()

            // 파일 추가
            val file = FileSystemResource(Paths.get(filePath))
            logger.info("File exists: ${file.exists()}, File size: ${if (file.exists()) file.contentLength() else "N/A"}")

            formData.add("document", file)
            formData.add("model", "document-parse-250618")
            formData.add("ocr", "auto")
            formData.add("chart_recognition", "true")
            formData.add("output_formats", "[\"html\",\"markdown\"]")
            formData.add("base64_encoding", "[\"figure\"]")
            formData.add("coordinates", "true")

            logger.info(">>> Calling Upstage API...")
            val response = webClient.post()
                .uri("https://api.upstage.ai/v1/document-digitization")
                .header("Authorization", "Bearer $upstageApiKey")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
            logger.info(">>> API response received, length: ${response?.length ?: 0}")

            // JSON에서 content만 추출
            if (response != null) {
                val jsonNode = objectMapper.readTree(response)
                val content = jsonNode.get("content")

                logger.info("=== Parsing Content for file: $filePath ===")
                logger.info("=== End of Content ===")
                val htmlContent = content?.get("html")?.asText() ?: ""
                val markdownContent = content?.get("markdown")?.asText() ?: ""

                val analysisText = analyzingDocument(htmlContent, markdownContent)
                val cleanJsonString = extractJsonFromText(analysisText)
                // Document 업데이트
                hrDocumentRepository.updateDocumentParsing(documentId, htmlContent, markdownContent, cleanJsonString)
                logger.info("Document $documentId updated with parsing results")

                // Chat API로 문서 분석

            }

        } catch (e: Exception) {
            logger.error("Error processing file $filePath: ${e.message}", e)
        }
    }

    private fun analyzingDocument(htmlContent: String, markdownContent: String): String {
        return try {
            val content = "HTML:\n$htmlContent\n\nMarkdown:\n$markdownContent"

            val response = chatClient.prompt()
                .system(
                    """
    당신은 업무 문서를 분석하여 읽기용 정보와 할 일을 구분하는 전문가입니다. 
    주어진 HTML 또는 마크다운 문서를 다음과 같이 분석하세요: 
    
    1. Reading Part: 정보성 내용을 **올바른 마크다운 문법**으로 정리
    2. Todo Part: 실행 가능한 할 일들을 구조화하여 추출
    
    응답 형식:
    [
      {
        "title": "섹션 제목", 
        "markdown": "마크다운", 
        "todo": ["할 일 1", "할 일 2",,,]
      }
    ]
    
    분석 기준: 
    - Reading은 설명/가이드/참고사항 
    - Todo는 체크리스트/신청/설치/완료 작업
    
    마크다운 작성 규칙:
    - 제목은 반드시 `#`, `##`, `###` 등 올바른 Heading 문법 사용
    - 목록은 `-` 또는 `1.` 으로 작성 (혼합 금지)
    - 체크박스는 `- [ ]` 또는 `- [x]` 형식 사용
    - 표는 `| 헤더 | ... |` + `| --- | --- |` + 행 데이터 형태로 작성
    - 불필요한 HTML 태그 사용 금지 (`<br>`, `<div>` 등 제거)
    - 줄바꿈은 **빈 줄 1줄**만 허용
    """
                )
                .user(content)
                .call()
                .content()

            response ?: ""

        } catch (e: Exception) {
            logger.error("Error analyzing document: ${e.message}", e)
            ""
        }
    }

    private fun extractJsonFromText(text: String): String {
        return try {
            // 첫 번째 { 부터 마지막 } 까지 찾기
            val startIndex = text.indexOf('{')
            val endIndex = text.lastIndexOf('}')

            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                val jsonString = text.substring(startIndex, endIndex + 1)
                // JSON 유효성 검사
                objectMapper.readTree(jsonString)
                jsonString
            } else {
                logger.warn("No valid JSON found in text")
                text
            }
        } catch (e: Exception) {
            logger.warn("Error extracting JSON from text: ${e.message}")
            text
        }
    }
}