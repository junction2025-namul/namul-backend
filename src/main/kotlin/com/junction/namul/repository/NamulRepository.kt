package com.junction.namul.repository

import com.junction.namul.model.document.Namul
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface NamulRepository : MongoRepository<Namul, String>