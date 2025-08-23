plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.junction"
version = "0.0.1-SNAPSHOT"
description = "namul"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot 설정 프로퍼티 애노테이션 프로세서
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    // Spring MVC 웹 스타터
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Swagger UI 및 OpenAPI 문서 자동 생성용 라이브러리
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
    // MongoDB 연동 스타터
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    // Bean Validation 지원 스타터
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Kotlin Reflection API
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // SLF4J 로깅 (Spring Boot 기본 로깅 지원, 필요시 명시)
    implementation("org.springframework.boot:spring-boot-starter-logging")
    // spring ai
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter:1.0.0-M6")
    // WebFlux WebClient for HTTP calls
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    // Jackson Kotlin support
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // 개발 편의성용 핫 리로딩 도구
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // 통합 테스트 스타터 (JUnit, Mockito 포함)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // Kotlin용 JUnit5 테스트 라이브러리
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    // JUnit 플랫폼 런처
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:1.0.0-M6")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
