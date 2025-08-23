package com.junction.namul.model.document

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
    val createdAt: LocalDateTime,
)
