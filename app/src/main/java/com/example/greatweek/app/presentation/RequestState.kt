package com.example.greatweek.app.presentation

sealed class RequestState<T> {
    class Idle<T> : RequestState<T>()
    class Loading<T> : RequestState<T>()
    class Error<T>(val error: String) : RequestState<T>()
    class Success<T>(val data: T) : RequestState<T>()
}