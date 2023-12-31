package com.aditya.appstoryaditya.util

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val error: String) : Resource<T>()

}