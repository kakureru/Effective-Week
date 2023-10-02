package com.example.utils

sealed class RequestState<T> {
    class Idle<T> : RequestState<T>()
    class Loading<T> : RequestState<T>()
    class Error<T>(val error: String) : RequestState<T>()
    class Success<T>(val data: T) : RequestState<T>()
}