package com.junction.namul.service

import com.junction.namul.repository.CategoryRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    fun getCategoryName(categoryId: ObjectId?): String {
        if (categoryId == null) return "기타"
        
        return categoryRepository.findById(categoryId)?.name ?: "기타"
    }
}