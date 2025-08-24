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

data class UploadRequest(
    val categoryId: String,
    val isNewbieDoc: Boolean,
    val uploadedBy: String
)

data class DocumentParsing(
    val html: String,
    val markdown: String,
)

data class DocumentDetail(
    val title: String,
    val markdown: String,
    val todo: List<String>
)
data class DocumentDetailJson(
    val id: String,
    val analysisJson: String? = null,
)

data class DocumentDetailWithId(
    val id: String,
    val title: String,
    val markdown: String,
    val todo: List<String>
)

data class DocumentAnalysis(
    val markdown: String,
    val detail: List<DocumentDetail>
)