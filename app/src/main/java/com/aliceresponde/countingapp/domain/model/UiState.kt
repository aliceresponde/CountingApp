package com.aliceresponde.countingapp.domain.model


sealed class UiState<T>(
    val data: T? = null,
    val message: String? = null
) {
    companion object{
        const val EMPTY_DATA_MESSAGE = "No Data Available to show"
    }
}
class SuccessState<T>(data: T) : UiState<T>(data)
class ErrorState<T>(message: String, data: T? = null) : UiState<T>(data, message)