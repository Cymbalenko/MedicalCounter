package com.example.medicalcounter.ui.medicine

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medicalcounter.model.db.dataClass.Medicine
import com.example.medicalcounter.model.repository.RoomRepository
import com.example.medicalcounter.ui.medicine.common.MedicineUiEvent
import com.example.medicalcounter.util.EventLiveData
import com.example.medicalcounter.util.EventMutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MedicineViewModel : ViewModel() {

    private var _medicine =  MutableLiveData<List<Medicine>>()
    private val _showEvent = EventMutableLiveData<MedicineUiEvent>()
    var medicine: LiveData<List<Medicine>> = _medicine


    private var _medicineFilter =  MutableLiveData<List<Medicine>>()
    var medicineFilter: LiveData<List<Medicine>> = _medicineFilter
    var filterString:String =  ""


    val showEvent: EventLiveData<MedicineUiEvent> = _showEvent

    fun getAll(){
        RoomRepository.getMedicineAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                _medicine.value=it
                _medicineFilter.value=it
            }, { exception ->
                println("error: ${exception.message}")
            })
    }
    fun filter(str:String){
        _medicineFilter.value = _medicine.value?.filter { a->a.title.contains(str) }
    }

    fun filterReady(){
        _medicineFilter.value = _medicine.value
    }
}