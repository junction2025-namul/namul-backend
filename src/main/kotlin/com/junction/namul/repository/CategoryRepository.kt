package com.junction.namul.repository

import com.junction.namul.model.document.Category
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class CategoryRepository(
    private val mongoTemplate: MongoTemplate
) {
    fun findById(categoryId: ObjectId): Category? {
        return mongoTemplate.findById(categoryId, Category::class.java)
    }
}