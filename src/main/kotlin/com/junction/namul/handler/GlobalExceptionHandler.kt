package com.junction.namul.handler

import com.junction.namul.constant.ResultCode
import com.junction.namul.model.dto.CommonResponse
import com.junction.namul.exception.CustomException
import com.junction.namul.factory.logger
import jakarta.servlet.ServletException
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.UnknownHostException

/**
 * java 의 RestControllerAdvice 구현체
 * 전역의 Exception 처리를 진행
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    val log = logger()

    /**
     * 특정 error message 를 handling 하기 위해 분기처리. 이때, 분기문 안에 들어가는 특정 문자열을 companion object 에서 상수로 관리
     */
    companion object{
        const val PROBLEM_COLON = "problem:"
        const val JSON_PARSE_ERROR = "JSON parse error"
        const val JSON_PARSE_ERROR_MESSAGE = "필수값을 확인하세요."
    }


    /**
     * HttpMessageNotReadableException 예외 처리 : 400 (HttpMessageConverter.JsonParsingException)
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<Any> {
        log.info(ex.message, ex)

        val httpStatus = BAD_REQUEST

        if(ex.message?.contains(PROBLEM_COLON) == true){
            return ResponseEntity(
                CommonResponse(false, httpStatus.value().toString(), ex.message?.substringAfter("problem:")
                    ?.trim() ?: httpStatus.reasonPhrase, null), httpStatus)
        }

        if(ex.message?.contains(JSON_PARSE_ERROR) == true){
            return ResponseEntity(CommonResponse(false, httpStatus.value().toString(), JSON_PARSE_ERROR_MESSAGE, null), httpStatus)
        }

        return ResponseEntity(CommonResponse(false, httpStatus.value().toString(), ex.message ?: httpStatus.reasonPhrase, null), httpStatus)
    }

    /**
     * MethodArgumentNotValidException 예외 처리 : 400 (RequestBody Validation Error)
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
        log.info(ex.message, ex)

        var message = ""

        val bindingResult = ex.bindingResult
        if (bindingResult.hasErrors() && bindingResult.fieldError != null) {
            message = bindingResult.fieldError!!.defaultMessage.toString()
        }

        val httpStatus = BAD_REQUEST
        return ResponseEntity(CommonResponse(false, httpStatus.value().toString(), message, null), httpStatus)
    }

    /**
     * NoSuchElementException 예외 처리 : 404 (ex. 요청된 리소스를 서버에서 찾을 수 없는 경우)
     */
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(ex: NoSuchElementException): ResponseEntity<Any> {
        log.info(ex.message, ex)

        val httpStatus = NOT_FOUND
        return ResponseEntity(CommonResponse(false, httpStatus.value().toString(), ex.message ?: httpStatus.reasonPhrase, null), httpStatus)
    }

    /**
     * HttpRequestMethodNotSupportedException 예외 처리 : 405
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(ex: HttpRequestMethodNotSupportedException): ResponseEntity<Any> {
        log.info(ex.message, ex)

        val httpStatus = METHOD_NOT_ALLOWED
        return ResponseEntity(CommonResponse(false, httpStatus.value().toString(), ex.message ?: httpStatus.reasonPhrase, null), httpStatus)
    }

    /**
     * Server Error 예외 처리 : 500
     */
    @ExceptionHandler(RuntimeException::class)
    fun handleException(ex: RuntimeException): ResponseEntity<Any> {
        log.info(ex.message, ex)

        val httpStatus = INTERNAL_SERVER_ERROR
        return ResponseEntity(CommonResponse(false, httpStatus.value().toString(), ex.message ?: httpStatus.reasonPhrase, null), httpStatus)
    }

    /**
     * 예상 가능한 예외에 대한 처리 : 200
     * 서버 응답은 OK 를 반환하며, 예상 가능한 실패에 대한 실패 코드를 추가로 전달한다.
     */
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(ex: CustomException): ResponseEntity<Any> {
        log.info(ex.message, ex)

        return ResponseEntity(
            CommonResponse(
                false,
                ex.errorCode?.code ?: ResultCode.UNKNOWN_ERROR.code,
                ex.message ?: ResultCode.UNKNOWN_ERROR.msg,
                null
            ),
            OK
        )
    }

    /**
     * ServletException 예외 처리 : 404 (ex. 요청된 리소스를 서버에서 찾을 수 없는 경우)
     * tomcat 오류 같은 케이스에 대해 /error 페이지를 이동하려 하지만 페이지 설정이 없어서 NOT_FOUND 가 발생
     * 위와 같은 케이스에 대한 핸들러 지정
     */
    @ExceptionHandler(ServletException::class)
    fun handleServletException(ex: ServletException): ResponseEntity<Any> {
        log.error(ex.message, ex)

        val httpStatus = NOT_FOUND
        return ResponseEntity(CommonResponse(false, httpStatus.value().toString(), ex.message ?: httpStatus.reasonPhrase, null), httpStatus)
    }

    /**
     * 예외 처리 로직이 동일한 Exception 공통화
     * - IllegalArgumentException
     * - NullPointerException
     * - MissingServletRequestParameterException
     */
    @ExceptionHandler(IllegalArgumentException::class, NullPointerException::class, MissingServletRequestParameterException::class)
    fun handleApplicationExceptions(ex: Exception): ResponseEntity<Any> {
        log.error(ex.message, ex)

        val httpStatus = BAD_REQUEST
        return ResponseEntity(CommonResponse(false, httpStatus.value().toString(), ex.message ?: httpStatus.reasonPhrase, null), httpStatus)
    }

    /**
     * UnknownHostException 예외 처리 : 502
     */
    @ExceptionHandler(UnknownHostException::class)
    fun handleUnknownHostExceptions(ex: UnknownHostException): ResponseEntity<Any> {
        log.error(ex.message, ex)

        val httpStatus = BAD_GATEWAY
        return ResponseEntity(CommonResponse(false, httpStatus.value().toString(), ex.message ?: httpStatus.reasonPhrase, null), httpStatus)
    }

}
