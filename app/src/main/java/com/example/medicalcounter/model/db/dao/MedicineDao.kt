package com.example.medicalcounter.model.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medicalcounter.model.db.dataClass.Counter
import com.example.medicalcounter.model.db.dataClass.Medicine
import com.example.medicalcounter.model.db.dataClass.Organization
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface MedicineDao {
    @Insert
    fun insert(medicine: Medicine): Single<Long>
    @Update
    fun update(medicine: Medicine): Completable
    @Delete
    fun delete (medicine: Medicine): Completable
    @Query("SELECT * FROM medicine where id = :id")
    fun get(id: Int): Single<Medicine>
    @Query("SELECT * FROM medicine where organizationId = :idOrganization")
    fun getByOrganizationId(idOrganization: Int): LiveData<List<Medicine>>
    @Query ("SELECT * FROM medicine")
    fun getAll():Single<List<Medicine>>
}