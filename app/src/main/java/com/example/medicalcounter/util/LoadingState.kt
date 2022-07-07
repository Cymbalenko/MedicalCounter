package com.example.medicalcounter.util

sealed class LoadingState {
    object Loading : LoadingState()
    object Success : LoadingState()
    class Error(val throwable: Throwable): LoadingState()
}