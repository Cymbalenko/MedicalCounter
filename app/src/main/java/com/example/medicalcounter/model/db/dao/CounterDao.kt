package com.example.medicalcounter.model.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medicalcounter.model.db.dataClass.Counter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface CounterDao {
    @Insert
    fun insert(counters: Counter):Single<Long>
    @Update
    fun update(counters: Counter):Completable
    @Delete
    fun delete (counters: Counter):Completable
    @Query("SELECT * FROM counter where id = :idCounter")
    fun get(idCounter: Int):LiveData<Counter>
    @Query("SELECT * FROM counter where periodId = :idPeriod")
    fun getByPeriodId(idPeriod: Int):Single<List<Counter>>
    @Query("SELECT * FROM counter where periodId = :idPeriod AND medicineId= :medicineId  ")
    fun getByPeriodIdMedicine(idPeriod: Int,medicineId:Int ):Single< Counter>

}