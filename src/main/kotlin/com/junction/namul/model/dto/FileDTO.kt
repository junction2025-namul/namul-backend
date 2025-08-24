package com.junction.namul.model.dto

data class FileUploadResponseDTO(
    val documentId: String,
    val fileId: String,
    val fileName: String,
    val originalName: String?,
    val fileSize: Long
)