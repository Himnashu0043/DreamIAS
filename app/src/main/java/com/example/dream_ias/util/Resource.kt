package com.example.dream_ias.util

import okhttp3.ResponseBody

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(val throwable: Throwable) : Resource<Nothing>()
    data class Error(val throwable: ResponseBody?) : Resource<Nothing>()
}

data class ErrorModel(val status: Int, val message: Any?)