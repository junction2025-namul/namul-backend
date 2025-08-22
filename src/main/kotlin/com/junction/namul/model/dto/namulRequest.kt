package com.junction.namul.model.dto

import java.time.LocalDateTime

data class namulRequest (
    val id: String? = null,
    val name: String,
    val description: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)