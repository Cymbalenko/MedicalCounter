package com.example.medicalcounter.ui.organization.common

import androidx.annotation.StringRes

sealed class OrganizationUiEvent {
    class Error(@StringRes val errorStringId: Int) : OrganizationUiEvent()
    class Loading(val isLoading: Boolean) : OrganizationUiEvent()
    class Update(val isUpdate: Boolean) : OrganizationUiEvent()
    class Confirm(val isOrder: Boolean) : OrganizationUiEvent()
}