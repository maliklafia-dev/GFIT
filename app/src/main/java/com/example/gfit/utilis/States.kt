package com.example.gfit.utilis

sealed class States<T> {
    data class Loading<T>(val text: String) : States<T>()
    data class Success<T>(val data : T) : States<T>()
    data class Failed<T>(val message: String) : States<T>()

    companion object {
        fun <T> loading(text: String) = Loading<T>(text)
        fun <T> success(data: T) = Success<T>(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}