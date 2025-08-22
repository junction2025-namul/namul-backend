package com.junction.namul.exception

import com.junction.namul.constant.ResultCode

class CustomException : RuntimeException {
    val errorCode: ResultCode?
    private var customMessage: String? = null

    constructor(errorCode: ResultCode) : super(errorCode.msg) {this.errorCode = errorCode}
    constructor(errorCode: ResultCode, customMessage: String) : super(customMessage) {
        this.errorCode = errorCode
        this.customMessage = customMessage
    }
}