package com.lgcns.crossdev.onboarding1.data.mapper

import com.lgcns.crossdev.onboarding1.data.dto.ApiError
import com.lgcns.crossdev.onboarding1.data.exception.CurrencyDataException
import kotlinx.serialization.SerializationException
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException

// 필요시 에러 분류를 추가
fun Throwable.toApiError() =
    when (this) {
        is IOException -> ApiError.Connection(this)
        is HttpException -> ApiError.Http(code = this.code(), message = this.message())
        is SerializationException -> ApiError.InvalidResponse(this)
        is JSONException -> ApiError.InvalidResponse(this)
        is CurrencyDataException ->
            ApiError.CurrencyErrorResponse(code = this.code.failCode, message = this.message)
        else -> ApiError.Unknown(this)
    }