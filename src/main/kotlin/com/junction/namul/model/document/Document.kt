package com.junction.namul.model.document

import com.junction.namul.model.dto.DocumentAnalysis
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime


@Document(collection = "document")
data class Document(
    @Id
    val id: ObjectId = ObjectId(),
    val categoryId: ObjectId = ObjectId(),
    val title: String? = null,
    val isNewbieDoc: Boolean,
    val html: String? = null,
    val markdown: String? = null,
    val analysisJson: DocumentAnalysis? = null,
    val createdAt: LocalDateTime,
)
