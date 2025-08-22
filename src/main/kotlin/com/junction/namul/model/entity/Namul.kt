package com.junction.namul.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "namul")
data class Namul(
    @Id
    val id: String? = null,
    val name: String,
    val description: String,
)