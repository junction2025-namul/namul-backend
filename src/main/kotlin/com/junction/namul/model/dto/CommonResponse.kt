package com.junction.namul.model.dto

import com.junction.namul.constant.ResultCode

/**
 * 공통 응답 DTO
 */
data class CommonResponse<T>(
    val result: Boolean,
    val code: String,
    val message: String,
    val data: T?
) {

    // API 성공 시 응답 (data 존재 O)
    constructor(data: T) : this(true, ResultCode.OK.code, ResultCode.OK.msg, data)

    // API 성공 시 응답 (data 존재 X)
    constructor(result: Boolean) : this(result, ResultCode.OK.code, ResultCode.OK.msg, null)

    companion object {
        fun<T> ok(data: T) = CommonResponse(data)
        fun ok() = CommonResponse(data = Unit)
    }
}