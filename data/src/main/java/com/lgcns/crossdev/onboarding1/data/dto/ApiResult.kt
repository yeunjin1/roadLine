package com.lgcns.crossdev.onboarding1.data.dto

sealed class ApiResult<out DATA> {
    data class Success<DATA>(val value: DATA) : ApiResult<DATA>()

    data class Failure(val cause: ApiError) : ApiResult<Nothing>()

    inline fun <R> map(transform: (DATA) -> R): ApiResult<R> {
        return when (this) {
            is Success -> Success(transform(this.value))
            is Failure -> this
        }
    }

    fun getOrNull(): DATA? {
        return if (this is Success) value else null
    }

    fun errorOrNull(): ApiError? {
        return if (this is Failure) cause else null
    }
}