package com.junction.namul.repository

import com.junction.namul.model.document.Document
import com.junction.namul.model.document.File
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class HrDocumentRepository(
    private val mongoTemplate: MongoTemplate,
) {

    fun saveDocument(document: Document): Document {
        return mongoTemplate.save(document)
    }
    
    fun saveFile(file: File): File {
        return mongoTemplate.save(file)
    }
    
    fun findDocumentById(id: String): Document? {
        return mongoTemplate.findById(id, Document::class.java)
    }
    
    fun findFilesByDocumentId(documentId: String): List<File> {
        return mongoTemplate.find(
            org.springframework.data.mongodb.core.query.Query.query(
                org.springframework.data.mongodb.core.query.Criteria.where("documentId").`is`(documentId)
            ),
            File::class.java
        )
    }
}