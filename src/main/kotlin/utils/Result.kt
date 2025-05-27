package org.daazay.utils

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(message: String, data: T? = null) : Result<T>(data, message)
}

inline fun<reified T> withResult(
    onError: (() -> Unit) = { },
    inner: () -> T,
): Result<T> = try {
    Result.Success(inner())
} catch (e: Exception) {
    onError()
    Result.Error(e.localizedMessage)
}
