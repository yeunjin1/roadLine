package com.lgcns.crossdev.onboarding1.domain.model

sealed class DomainError(
    override val message: String,
    cause: Throwable? = null,
) : Exception(message, cause)