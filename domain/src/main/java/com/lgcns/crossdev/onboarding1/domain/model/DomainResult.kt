package com.lgcns.crossdev.onboarding1.domain.model


sealed class DomainResult<out T> {
    data class Success<out T>(val data: T) : DomainResult<T>()
    data class Failure(val error: DomainError) : DomainResult<Nothing>()

    inline fun <R> map(transform: (T) -> R): DomainResult<R> {
        return when (this) {
            is Success -> Success(transform(this.data))
            is Failure -> this
        }
    }

    inline fun <R> flatMap(transform: (T) -> DomainResult<R>): DomainResult<R> {
        return when (this) {
            is Success -> transform(this.data)
            is Failure -> this
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun getOrNull(): T? =
        when (this) {
            is Success -> data
            is Failure -> null
        }

    @Suppress("NOTHING_TO_INLINE")
    inline fun errorOrNull(): DomainError? =
        when (this) {
            is Success -> null
            is Failure -> error
        }
}

inline fun <T, R> DomainResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (DomainError) -> R
): R {
    return when (this) {
        is DomainResult.Success -> onSuccess(data)
        is DomainResult.Failure -> onFailure(error)
    }
}

inline fun <T> DomainResult<T>.onSuccess(action: (T) -> Unit): DomainResult<T> {
    if (this is DomainResult.Success) action(data)
    return this
}

inline fun <T> DomainResult<T>.onFailure(action: (DomainError) -> Unit): DomainResult<T> {
    if (this is DomainResult.Failure) action(error)
    return this
}
