package com.junction.namul.model.document

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "categories")
data class Category(
    val id: ObjectId? = ObjectId(),
    val name: String,
)
