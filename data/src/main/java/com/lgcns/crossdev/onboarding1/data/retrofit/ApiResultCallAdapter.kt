package com.lgcns.crossdev.onboarding1.data.retrofit

import com.lgcns.crossdev.onboarding1.data.dto.ApiError
import com.lgcns.crossdev.onboarding1.data.dto.ApiResult
import com.lgcns.crossdev.onboarding1.data.mapper.toApiError
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class ApiResultCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Call<ApiResult<T>>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Call<ApiResult<T>> = ApiResultCall(call)

    class Factory : CallAdapter.Factory() {

        override fun get(
            returnType: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ): CallAdapter<*, *>? {

            if (getRawType(returnType) != Call::class.java) return null

            check(returnType is ParameterizedType) {
                "Return type must be parameterized as Call<T>"
            }

            val responseType = getParameterUpperBound(0, returnType)

            if (getRawType(responseType) != ApiResult::class.java) return null

            check(responseType is ParameterizedType) {
                "Response type must be parameterized as Call<T>"
            }

            val successType = getParameterUpperBound(0, responseType)

            return ApiResultCallAdapter<Any>(successType)
        }
    }
}

private class ApiResultCall<T> constructor(
    private val delegate: Call<T>
) : Call<ApiResult<T>> {

    override fun enqueue(callback: Callback<ApiResult<T>>) =
        delegate.enqueue(
            // 항상 성공 응답 해야함
            ApiResultCallback {
                callback.onResponse(this, Response.success(it))
            }
        )

    override fun execute(): Response<ApiResult<T>> =
        throw java.lang.UnsupportedOperationException()

    override fun clone(): Call<ApiResult<T>> = ApiResultCall(delegate.clone())

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}

private class ApiResultCallback<T>(
    private val emitResult: (ApiResult<T>) -> Unit,
) : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        val result = when {
            response.isSuccessful -> {
                val body = response.body()
                if (body != null) {
                    ApiResult.Success(body)
                } else {
                    val error = ApiError.EmptyBody()
                    ApiResult.Failure(error)
                }
            }

            else -> {
                // 필요시 response.errorBody() 를 파싱 처리를 해야함
                val error = ApiError.Http(
                    code = response.code(), message = response.message()
                )
                ApiResult.Failure(error)
            }
        }
        emitResult(result)
    }

    override fun onFailure(call: Call<T>, cause: Throwable) {
        emitResult(ApiResult.Failure(cause.toApiError()))
    }
}

