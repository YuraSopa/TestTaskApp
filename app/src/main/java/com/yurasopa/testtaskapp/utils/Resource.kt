package com.yurasopa.testtaskapp.utils

sealed class Resource<T>(
    val data: T? = null,
    val error: String? = null,
    val statusCode: Int? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(error: String?, data: T? = null, statusCode: Int? = null) :
        Resource<T>(data, error, statusCode)

    class Loading<T> : Resource<T>()
}
