package com.junction.namul.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class SwaggerConfig(
    private val environment: Environment
) {
    
    @Bean
    fun openAPI(): OpenAPI {
        val servers = if (environment.activeProfiles.contains("local")) {
            listOf(Server().url("http://localhost:8080").description("Local server"))
        } else {
            listOf(Server().url("https://namul-backend-production-e240.up.railway.app").description("Production server"))
        }
        
        return OpenAPI()
            .info(
                Info()
                    .title("Namul API")
                    .description("Namul API Documentation")
                    .version("1.0.0")
            )
            .servers(servers)
    }
}