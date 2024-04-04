package com.lgcns.crossdev.onboarding1.data.dto

sealed class ApiError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {

    class EmptyBody : ApiError()

    // no connection, unknown host, timeout
    class Connection(override val cause: Throwable) : ApiError(cause = cause)

    class Http(val code: Int, override val message: String) : ApiError(message = message)

    // parsing
    class InvalidResponse(override val cause: Throwable) : ApiError(cause = cause)

    class SolResponse(
        val code: String,
        val defaultMessages: List<String>, // not blank
        val userMessages: List<String>, // not blank
        val additionalMessage: String?, // not blank
    ) : ApiError()

    class SCertResponse(
        val code: String,
        override val message: String,
    ) : ApiError()

    class CurrencyErrorResponse (
        val code: Int,
        override val message: String,
    ) : ApiError()

    class Unknown(override val cause: Throwable) : ApiError(cause = cause)
}