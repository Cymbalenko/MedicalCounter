package com.example.medicalcounter.ui.medicine.common

import androidx.annotation.StringRes

 sealed class MedicineUiEvent {
    class Error(@StringRes val errorStringId: Int) : MedicineUiEvent()
    class Loading(val isLoading: Boolean) : MedicineUiEvent()
    class Update(val isUpdate: Boolean) : MedicineUiEvent()
    class Confirm(val isOrder: Boolean) : MedicineUiEvent()
}