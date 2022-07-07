package com.example.medicalcounter.ui.organizationAddUpdate.common

import androidx.annotation.StringRes
import com.example.medicalcounter.util.LoadingState

sealed  class OrganizationAddUpdateUiEvent {
    class Error() : OrganizationAddUpdateUiEvent()
    class Loading(val isLoading: Boolean) : OrganizationAddUpdateUiEvent()
    class Update(val isUpdate: Boolean) : OrganizationAddUpdateUiEvent()
    class Confirm(val isOrder: Boolean) : OrganizationAddUpdateUiEvent()
    class DisplayLoadingState(val loadingState: LoadingState) : OrganizationAddUpdateUiEvent()
}