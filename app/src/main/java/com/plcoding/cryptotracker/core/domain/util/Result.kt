package com.plcoding.cryptotracker.core.domain.util

typealias DomainError = Error

sealed interface Result<out D, out E: Error> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E: DomainError>(val error: E): Result<Nothing, E>
}

// map one type of result to another
inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

// convert to empty data result
fun <T, E: Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {  }
}

// actions if result is success
inline fun <T, E: Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

// actions if result is error
inline fun <T, E: Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}

typealias EmptyResult<E> = Result<Unit, E>