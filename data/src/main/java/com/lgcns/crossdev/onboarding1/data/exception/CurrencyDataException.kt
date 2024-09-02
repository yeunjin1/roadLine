package com.lgcns.crossdev.onboarding1.data.exception

class CurrencyDataException(
    val code: ErrorCode,
    override val message: String,
) : Exception() {
    enum class ErrorCode(
        val failCode: Int,
    ) {
        // 1 : 성공, 2 : DATA코드 오류, 3 : 인증코드 오류, 4 : 일일제한횟수 마감
        SUCCESS(1),
        DATA_CODE_ERROR(2),
        AUTH_ERROR(3),
        DAY_MAX_ACCESS_ERROR(4),
    }
}