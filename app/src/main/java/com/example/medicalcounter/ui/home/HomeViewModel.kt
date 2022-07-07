package com.example.medicalcounter.ui.home

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medicalcounter.R
import com.example.medicalcounter.model.db.dataClass.Counter
import com.example.medicalcounter.model.db.dataClass.Period
import com.example.medicalcounter.model.repository.RoomRepository
import com.example.medicalcounter.ui.home.common.HomeUiEvent
import com.example.medicalcounter.ui.medicine.common.MedicineUiEvent
import com.example.medicalcounter.util.EventLiveData
import com.example.medicalcounter.util.EventMutableLiveData
import com.example.medicalcounter.util.call
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    private var _countersFilter =  MutableLiveData<List<Counter>>()
    private var _counters =  MutableLiveData<List<Counter>>()
    private var _period =  MutableLiveData< Period> ()
    private val _showEvent = EventMutableLiveData<HomeUiEvent>()
    var countersFilter: LiveData<List<Counter>> = _countersFilter
    var counters: LiveData<List<Counter>> = _counters
    var period: LiveData< Period>  = _period
    var periodId:Int = -1
    var periodName:String =  ""
    var filterString:String =  ""
    val showEvent: EventLiveData<HomeUiEvent> = _showEvent
    var selectMounth: Int=-1
    var selectSort: Int=1
    var selectYear: Int=-1
    var initial: Boolean=false

    fun loadLastCounter(){
        Log.e("loadAddCounters", " - " +selectYear + "      "+ selectMounth )
        RoomRepository.getPeriodY(selectYear,selectMounth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _period.value =  it
                period.value?.let {
                    _period.value=it
                    periodId=it.id
                    periodName=it.title
                }
                loadAddCounters()
                Log.e("loadAddCounters", "Yes\n"  + it)
            }, { exception ->
                Log.e("loadAddCounters", "No\n" + exception.message)
                var periodItem=Period("1",selectYear,selectMounth)

                Log.e("loadAddCounters", "Period\n" + periodItem)
                RoomRepository.insertPeriod(periodItem)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.e("loadAddCounters", "InsertYes\n"+selectYear + "      "+ selectMounth  )

                        RoomRepository.getPeriodY(selectYear,selectMounth)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                _period.value =  it
                                period.value?.let {
                                    _period.value=it
                                    periodId=it.id
                                    periodName=it.title
                                    Log.e("loadAddCounters", "RetryOk\n"  )
                                    loadAddCounters()
                                }
                            }, { exception ->
                                Log.e("loadAddCounters", "RetryNo\n" + exception.message)

                            })
                    }, { exception ->
                        Log.e("loadAddCounters", "InsertNo\n" + exception.message)
                        println("error: ${exception.message}")
                    })
            })
    }
    fun loadAddCounters(){
        if(periodId==-1) return
        Log.e("loadAddCounters", "loadAddCounters\n")
        Log.e("loadAddCounters", "periodId\n"+periodId)
        Log.e("loadAddCounters", "selectMounth\n"+selectMounth)
        RoomRepository.getMedicineAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ medArr->
                var j=0
                while (  medArr.size>j  ){
                     var i = medArr[j]
                    RoomRepository.getByPeriodIdMedicine(periodId,i.id )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ s->
                            if(s.organizationName!=i.organizationName||s.organizationId!=i.organizationId||s.medicineTitle!=i.title){
                                s.organizationName=i.organizationName
                                s.organizationId=i.organizationId
                                s.medicineTitle=i.title
                                RoomRepository.updateCounter (s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                    }, { exception ->

                                    })
                            }
                            if(j==medArr.size){
                                RoomRepository.getCounterPeriodId (periodId)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({ medArr->
                                        _counters.value=medArr
                                        _countersFilter.value=medArr
                                        sorting()
                                        initial=true
                                    }, { exception ->

                                    })
                            }
                        }, { exception ->
                            var counter = Counter(0,i.id,i.title,periodId,periodName,i.organizationId,i.organizationName)

                            Log.e("insertCounter", "insertCounter\n"+counter)
                            RoomRepository.insertCounter (counter)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ md->
                                    Log.e("insertCounter", "insertCounterаааааа\n"+counter)
                                    if(j==medArr.size){

                                        RoomRepository.getCounterPeriodId (periodId)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({ medArr->
                                                _counters.value=medArr
                                                _countersFilter.value=medArr
                                                sorting()
                                                initial=true
                                            }, { exception ->

                                            })
                                    }
                                }, { exception ->

                                })

                        })
                    j++
                }
                _showEvent.call(HomeUiEvent.Confirm(true))

            }, { exception ->
                RoomRepository.getCounterPeriodId (periodId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ medArr->
                        _counters.value=medArr
                        _countersFilter.value=medArr
                        sorting()
                    }, { exception ->

                    })
            })
    }
    fun filter(str:String){
        _countersFilter.value = _counters.value?.filter { a->a.medicineTitle.contains(str) }
    }

    fun filterReady(){
        _countersFilter.value = _counters.value
    }
    fun load(){
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://transloc-api-1-2.p.rapidapi.com/agencies.json?callback=call&geo_area=35.80176%2C-78.64347%7C35.78061%2C-78.68218&agencies=12")
            .get()
            .addHeader("X-RapidAPI-Key", "fb0a66a2f5msh21c2715323e8bb2p145804jsn9e775bb60058")
            .addHeader("X-RapidAPI-Host", "transloc-api-1-2.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
        Log.e("load",response.message)
        Log.e("load",response.toString())
    }
    fun updateCounters(counter:Counter){
        RoomRepository.updateCounter(counter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _showEvent.call(HomeUiEvent.Update(true))
                sorting()
            }, { exception ->
            })
    }
    fun sorting(){
        Log.e("selectSort", "selectMounth -  "+selectMounth)
        Log.e("selectSort", "selectYear -  "+selectYear)
        Log.e("selectSort", "periodId -  "+periodId)
        Log.e("selectSort", "selectSort -  "+selectSort)
        Log.e("selectSort", "_counters -  "+_countersFilter.value)
        if(periodId==-1) return
        when(selectSort){
            1->{
                _counters.value?.let{
                    _countersFilter.value=it.sortedBy { a->a.medicineTitle }
                }
            }
            2->{
                _counters.value?.let{
                    _countersFilter.value=it.sortedBy { a->a.medicineTitle }
                    _countersFilter.value =_countersFilter.value?.reversed()
                }
            }
            3->{
                _counters.value?.let{
                    _countersFilter.value=it.sortedWith(Comparator{ p1: Counter, p2: Counter -> p1.coun - p2.coun })
                    _countersFilter.value =_countersFilter.value?.reversed()
                }
            }
            4->{
                _counters.value?.let{
                    _countersFilter.value=it.sortedWith(Comparator{ p1: Counter, p2: Counter -> p1.coun - p2.coun })
                }
            }
        }
        Log.e("selectSort", "_counters -  "+_countersFilter.value)
    }
}