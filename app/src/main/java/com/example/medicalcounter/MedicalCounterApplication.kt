package com.example.medicalcounter

import android.app.Application
import android.util.Log.e
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import timber.log.Timber

class MedicalCounterApplication :Application(){
    override fun onCreate(){
        super.onCreate()
        RxJavaPlugins.setErrorHandler(Timber::e)
        instance=this
    }
    companion object{
        var instance:MedicalCounterApplication?=null
    }
}