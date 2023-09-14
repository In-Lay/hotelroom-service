package com.inlay.hotelroomservice.presentation.models

sealed class AppResult<out T, out R> {
    data class Success<out T>(val data: T) : AppResult<T, Nothing>()
    data class Error<out R>(val error: Int) : AppResult<Nothing, R>()
}
