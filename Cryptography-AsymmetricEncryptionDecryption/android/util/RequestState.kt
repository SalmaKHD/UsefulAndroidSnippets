package com.salmakhd.android.cryptography.util

sealed class RequestState <out T> {
    data object Idle: RequestState<Nothing>()
    data object Loading: RequestState<Nothing>()
    data class Success<T>(val data: T): RequestState<T>()
    data class Error(val message:String): RequestState<Nothing>() {
        fun parseError(): String {
            return if(message.contains("failed to connect to")) "Failed to Connect to Server"
            else if(message.contains("timeout")) "Connection Time Our"
            else message
        }
    }


    fun isLoading() = this is Loading
    fun isSuccess() = this is Success
    fun isError() = this is Error
}