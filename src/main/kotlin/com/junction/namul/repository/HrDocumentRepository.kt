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
    
    fun updateDocumentParsing(documentId: ObjectId, html: String, markdown: String, analysisJson: String) {
        val query = Query.query(Criteria.where("id").`is`(documentId))
        val update = Update()
            .set("html", html)
            .set("markdown", markdown)
            .set("analysisJson", analysisJson)

        mongoTemplate.updateFirst(query, update, Document::class.java)
    }
    
    fun updateDocumentTitle(documentId: ObjectId, title: String) {
        val query = Query.query(Criteria.where("id").`is`(documentId))
        val update = Update().set("title", title)
        
        mongoTemplate.updateFirst(query, update, Document::class.java)
    }
    
    fun findDocumentByIds(documentIds: List<ObjectId>): List<Document> {
        val query = Query.query(Criteria.where("id").`in`(documentIds))
        return mongoTemplate.find(query, Document::class.java)
    }
    
    fun findAllDocumentsGroupedByCategory(): List<Document> {
        return mongoTemplate.findAll(Document::class.java)
    }
}