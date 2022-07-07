package com.example.medicalcounter.ui.organizationAddUpdate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medicalcounter.R
import com.example.medicalcounter.model.db.dataClass.Organization
import com.example.medicalcounter.model.repository.RoomRepository
import com.example.medicalcounter.ui.base.BaseViewModel
import com.example.medicalcounter.ui.organization.common.OrganizationUiEvent
import com.example.medicalcounter.ui.organizationAddUpdate.common.OrganizationAddUpdateUiEvent
import com.example.medicalcounter.util.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class OrganizationAddUpdateViewModel : BaseViewModel() {

    private var _organization =  MutableLiveData< Organization> ()
    var organization: LiveData< Organization> = _organization
    var organizationId: Int = -1
    var isNew: Boolean = true
    var pause: Boolean = false
    private val _showEvent = EventMutableLiveData<OrganizationAddUpdateUiEvent>()
    val showEvent: EventLiveData<OrganizationAddUpdateUiEvent> = _showEvent

    fun createOrganization(organization: Organization ){
        Log.e("OrganizationAdd", "organization\n" + organization)
        if(isNew){
            RoomRepository.insertOrganization(organization)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    Log.e("OrganizationAdd", "Success Save\n" + it )
                }, { exception ->
                    Log.e("OrganizationAdd", "exception Save\n" + exception.message )
                    println("error: ${exception.message}")
                })
        }else{
            RoomRepository.updateOrganization(organization)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    Log.e("OrganizationAdd", "Success Update\n"  )
                }, { exception ->
                    println("error: ${exception.message}")
                })
        }

    }
    fun loadOrganization()  {
        Log.e("OrganizationAdd", "loadOrganization " + organizationId )
        if(organizationId==-1) {
            return
        }
        RoomRepository.getOrganization(organizationId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let { o->
                    _organization.value=o
                    _showEvent.value = Event(OrganizationAddUpdateUiEvent.Confirm(true))
                    Log.e("OrganizationAdd", "_organization.value " + o )
                }

            }, { exception ->
                println("error: ${exception.message}")
            })
    }
    fun deleteOrganization( ){
        _showEvent.value = Event(OrganizationAddUpdateUiEvent.Error( ))
        _organization.value?.let {
            it.id=organizationId
            Log.e("deleteOrganization", "value "  )
            disposeOnCleared(
                RoomRepository.deleteOrganization(it),{
                    Log.e("deleteOrganization", "disposeOnCleared " )

                }) { exception ->
                _showEvent.value = Event(OrganizationAddUpdateUiEvent.Error( ))
                Log.e("deleteOrganization", "exception " )
                    println("error: ${exception.message}")
                }
        }

    }
}