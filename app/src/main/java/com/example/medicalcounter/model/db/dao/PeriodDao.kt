package com.example.medicalcounter.model.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medicalcounter.model.db.dataClass.Period
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface PeriodDao {
    @Insert
    fun insert(period: Period):Single<Long>
    @Update
    fun update(period: Period):Completable
    @Delete
    fun delete(period: Period):Completable
    @Query("SELECT * FROM period WHERE id = :id")
    fun get(id:Int):LiveData<Period>
    @Query("SELECT * FROM period ORDER BY id DESC LIMIT 1 ")
    fun getLast():Single<Period>
    @Query("SELECT * FROM period WHERE month=:m AND year=:y")
    fun getP(m:Int,y:Int):Single< Period>
}