package com.junction.namul.controller

import com.junction.namul.model.dto.CategoryAndHDocument
import com.junction.namul.model.dto.DocumentInfo
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/hr-documents")
@Tag(name = "Management Document", description = "hr 매니저 문서 관리")
class HrDocumentController {

    @GetMapping
    fun findHrDocuments() :List<CategoryAndHDocument>{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val now = LocalDateTime.now()
        
        return listOf(
            CategoryAndHDocument(
                categoryId = "66e5f12a4b2c1a001f123456",
                category = "공통",
                documentInfo = listOf(
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f123457",
                        title = "신입사원 온보딩 가이드",
                        isNewbieDoc = true,
                        uploadDate = now.minusDays(30).format(formatter)
                    ),
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f123458",
                        title = "회사 규정 및 정책",
                        isNewbieDoc = true,
                        uploadDate = now.minusDays(25).format(formatter)
                    ),
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f123459",
                        title = "보안 가이드라인",
                        isNewbieDoc = false,
                        uploadDate = now.minusDays(20).format(formatter)
                    ),
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f12345a",
                        title = "원격근무 정책",
                        isNewbieDoc = false,
                        uploadDate = now.minusDays(15).format(formatter)
                    )
                )
            ),
            CategoryAndHDocument(
                categoryId = "66e5f12a4b2c1a001f12345b",
                category = "프론트엔드",
                documentInfo = listOf(
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f12345c",
                        title = "React 개발 가이드",
                        isNewbieDoc = true,
                        uploadDate = now.minusDays(28).format(formatter)
                    ),
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f12345d",
                        title = "TypeScript 컨벤션",
                        isNewbieDoc = true,
                        uploadDate = now.minusDays(22).format(formatter)
                    ),
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f12345e",
                        title = "UI/UX 디자인 시스템",
                        isNewbieDoc = false,
                        uploadDate = now.minusDays(18).format(formatter)
                    ),
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f12345f",
                        title = "성능 최적화 가이드",
                        isNewbieDoc = false,
                        uploadDate = now.minusDays(12).format(formatter)
                    ),
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f123460",
                        title = "웹 접근성 체크리스트",
                        isNewbieDoc = false,
                        uploadDate = now.minusDays(8).format(formatter)
                    )
                )
            ),
            CategoryAndHDocument(
                categoryId = "66e5f12a4b2c1a001f123461",
                category = "백엔드",
                documentInfo = listOf(
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f123462",
                        title = "Spring Boot 개발 가이드",
                        isNewbieDoc = true,
                        uploadDate = now.minusDays(27).format(formatter)
                    ),
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f123463",
                        title = "API 설계 원칙",
                        isNewbieDoc = true,
                        uploadDate = now.minusDays(24).format(formatter)
                    ),
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f123464",
                        title = "데이터베이스 스키마 관리",
                        isNewbieDoc = false,
                        uploadDate = now.minusDays(19).format(formatter)
                    ),
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f123465",
                        title = "Docker 컨테이너 가이드",
                        isNewbieDoc = false,
                        uploadDate = now.minusDays(14).format(formatter)
                    ),
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f123466",
                        title = "모니터링 및 로깅",
                        isNewbieDoc = false,
                        uploadDate = now.minusDays(10).format(formatter)
                    ),
                    DocumentInfo(
                        docId = "66e5f12a4b2c1a001f123467",
                        title = "테스트 작성 가이드",
                        isNewbieDoc = false,
                        uploadDate = now.minusDays(5).format(formatter)
                    )
                )
            )
        )
    }
}