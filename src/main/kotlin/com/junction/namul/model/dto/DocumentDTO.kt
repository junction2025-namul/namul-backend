package com.junction.namul.model.dto

data class CategoryAndHDocument (
    val categoryId: String,
    val category: String,
    val documentInfo: List<DocumentInfo>
)
data class DocumentInfo(
    val docId: String,
    val title: String,
    val isNewbieDoc: Boolean,
    val uploadDate: String,
)