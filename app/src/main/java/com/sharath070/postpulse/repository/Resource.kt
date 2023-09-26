package com.sharath070.postpulse.repository

sealed class Resource<T>(val data: T? = null, val errorMsg: String? = null) {

    class Loading<T> : Resource<T>()
    class Success<T>(data: T? = null): Resource<T>(data = data)
    class Error<T>(errorMsg: String): Resource<T>(errorMsg = errorMsg)

}