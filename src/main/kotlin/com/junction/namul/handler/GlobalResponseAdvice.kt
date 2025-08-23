package com.junction.namul.handler

import com.junction.namul.annotation.RowBody
import com.junction.namul.model.dto.CommonResponse
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice(basePackages = ["com.junction.namul.controller"])
class GlobalResponseAdvice: ResponseBodyAdvice<Any> {
    // @RowBody 어노테이션이 메서드나 클래스에 있는 경우 변환하지 않음
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean =
        ! (returnType.hasMethodAnnotation(RowBody::class.java)
                || returnType.declaringClass.isAnnotationPresent(RowBody::class.java))


    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any = CommonResponse(body)
}