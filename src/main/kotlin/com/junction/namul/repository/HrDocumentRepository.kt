package com.junction.namul.repository

import com.junction.namul.model.document.Document
import com.junction.namul.model.document.File
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
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
            Query.query(
                Criteria.where("documentId").`is`(documentId)
            ),
            File::class.java
        )
    }
    
    fun updateDocumentParsing(documentId: ObjectId, html: String, markdown: String) {
        val query = Query.query(Criteria.where("id").`is`(documentId))
        val update = Update()
            .set("html", html)
            .set("markdown", markdown)
        
        mongoTemplate.updateFirst(query, update, Document::class.java)
    }
}