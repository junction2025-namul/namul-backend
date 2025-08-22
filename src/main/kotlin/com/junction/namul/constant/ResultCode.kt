package com.junction.namul.constant

enum class ResultCode(val code: String, val msg: String) {
    // 공통
    OK("0000", "OK"),
    INVALID_REQUEST("400", "잘못된 요청입니다."),
    ACCESS_DENIED("403", "접근이 거부되었습니다."),
    INVALID_HISTORY_PRAMS("400", "관리자에게 문의하세요"),
    UNKNOWN_ERROR("9999", "알 수 없는 시스템 에러가 발생했습니다."),
    SERVICE_TYPE_NOT_FOUND("404", "존재하지 않는 서비스 타입입니다."),
}