package com.junction.namul.model.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "file")
data class File(
    @Id
    val id: ObjectId? = ObjectId(),
    val documentId: ObjectId? = ObjectId(),
    val filename: String,
    val filePath: String,
    val fileSize: Long,
    val mimeType: String,
    val uploadDate: LocalDateTime,
    val uploadedBy: String,
)