package com.junction.namul.repository

import com.junction.namul.model.document.File
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class FileRepository(
    private val mongoTemplate: MongoTemplate,
) {
    fun findByIds(documentIds: List<ObjectId>): List<File> {
        val query = Query(Criteria.where("documentId").`in`(documentIds))
        return mongoTemplate.find(query, File::class.java)
    }
}