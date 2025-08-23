package com.junction.namul.service

import com.junction.namul.model.document.Namul
import com.junction.namul.repository.NamulRepository
import org.springframework.stereotype.Service

@Service
class NamulService(
    private val namulRepository: NamulRepository
) {
    
    fun save(namul: Namul): Namul {
        return namulRepository.save(namul)
    }
    
    fun findAll(): List<Namul> {
        return namulRepository.findAll()
    }
}