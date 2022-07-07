package com.example.medicalcounter.ui.home.common

import androidx.annotation.StringRes

sealed class HomeUiEvent {

    class Error(@StringRes val errorStringId: Int) : HomeUiEvent()
    class Loading(val isLoading: Boolean) : HomeUiEvent()
    class Update(val isUpdate: Boolean) : HomeUiEvent()
    class Confirm(val isOrder: Boolean) : HomeUiEvent()
}