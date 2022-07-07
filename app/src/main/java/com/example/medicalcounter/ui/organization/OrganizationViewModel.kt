package com.example.medicalcounter.ui.organization

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medicalcounter.MedicalCounterApplication
import com.example.medicalcounter.model.db.dataClass.Medicine
import com.example.medicalcounter.model.db.dataClass.Organization
import com.example.medicalcounter.model.repository.RoomRepository
import com.example.medicalcounter.ui.organization.common.OrganizationUiEvent
import com.example.medicalcounter.util.EventLiveData
import com.example.medicalcounter.util.EventMutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class OrganizationViewModel  : ViewModel() {

    private var _organization =  MutableLiveData<List<Organization>>()
    private val _showEvent = EventMutableLiveData<OrganizationUiEvent>()
    var organization: LiveData<List<Organization>> = _organization
    val showEvent: EventLiveData<OrganizationUiEvent> = _showEvent

    private var _organizationFilter =  MutableLiveData<List<Organization>>()
    var organizationFilter: LiveData<List<Organization>> = _organizationFilter
    var filterString:String =  ""

    fun getAll(){
        Log.e("MainViewModeltoken", "Success\n" + organization.value)
         RoomRepository.getOrganizationAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                Log.e("MainViewModeltoken", "Success List\n" + it )
                _organization.value=it
                _organizationFilter.value=it
            }, { exception ->
                println("error: ${exception.message}")
            })
        Log.e("MainViewModeltoken", "Success\n" + organization.value)
    }
    fun OnClickListenerDialPhone(order: Organization){
        if (order!=null) {
            val phone_no = order.phone
            phone_no.let {

                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone_no"))
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MedicalCounterApplication.instance?.let{
                    it.startActivity(callIntent)
                }
            }
        }
    }
    fun filter(str:String){
        _organizationFilter.value = _organization.value?.filter { a->a.title.contains(str) }
    }

    fun filterReady(){
        _organizationFilter.value = _organization.value
    }
}
