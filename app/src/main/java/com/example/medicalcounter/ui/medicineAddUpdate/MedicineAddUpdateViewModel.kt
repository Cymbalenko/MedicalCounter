package com.example.medicalcounter.ui.medicineAddUpdate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medicalcounter.R
import com.example.medicalcounter.model.db.dataClass.Medicine
import com.example.medicalcounter.model.db.dataClass.Organization
import com.example.medicalcounter.model.repository.RoomRepository
import com.example.medicalcounter.ui.medicine.common.MedicineUiEvent
import com.example.medicalcounter.ui.organization.common.OrganizationUiEvent
import com.example.medicalcounter.util.EventLiveData
import com.example.medicalcounter.util.EventMutableLiveData
import com.example.medicalcounter.util.call
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MedicineAddUpdateViewModel  : ViewModel() {

    private var _medicine =  MutableLiveData< Medicine> ()
    private var _organization =  MutableLiveData< List<Organization>> ()
    private val _showEvent = EventMutableLiveData<MedicineUiEvent>()
    var medicine: LiveData<Medicine> = _medicine
    var organization: LiveData<List<Organization>> = _organization
    var medicineId: Int = -1
    var pause: Boolean = false
    var selectPositionId: Int = -1
    var selectPositionName: String = "-1"
    var organizationList= listOf<String>();
    var isNew: Boolean = true
    val showEvent: EventLiveData<MedicineUiEvent> = _showEvent

    fun createMedicine(medicine: Medicine){
        Log.e("MainViewModeltoken_", "medicine Save\n" +medicine )
        if(isNew){
            RoomRepository.insertMedicine(medicine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    Log.e("MainViewModeltoken_", "Success Save\n" + it )
                }, { exception ->
                    println("error: ${exception.message}")
                })
        }else{
            RoomRepository.updateMedicine(medicine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    Log.e("MainViewModeltoken_", "Success Update\n"  )
                }, { exception ->
                    println("error: ${exception.message}")
                })
        }

    }
    fun getAllOrganization(){
        Log.e("MainViewModeltoken_", "getAllOrganization " )
        RoomRepository.getOrganizationAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _organization.value=it
                organizationList=it.map { a->a.title }
                Log.e("MainViewModeltoken_1", "getAllOrganization " + _organization.value )
                Log.e("MainViewModeltoken_1", "getAllOrganization " + organizationList )
                _showEvent.call(MedicineUiEvent.Loading(true))
            }, { exception ->
                println("error: ${exception.message}")
            })
    }
    fun loadMedicine()  {
        Log.e("MainViewModeltoken_", "loadmedicine " + medicineId )
        if(medicineId==-1) {
            return
        }
        RoomRepository.getMedicine(medicineId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let { o->
                    _medicine.value=o
                    Log.e("MainViewModeltoken_", "_medicine.value " + o )
                    selectPositionId=o.organizationId
                    selectPositionName=o.organizationName
                    _showEvent.call(MedicineUiEvent.Confirm(true))
                }

            }, { exception ->
                println("error: ${exception.message}")
            })
    }
    fun deleteMedicine( ){
        _medicine.value?.let {
            RoomRepository.deleteMedicine(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, { exception ->
                    _showEvent.call(MedicineUiEvent.Error( R.string.organization_delete_error))
                    println("error: ${exception.message}")
                })
        }

    }
}