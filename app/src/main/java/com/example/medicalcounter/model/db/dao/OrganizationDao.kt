package com.example.medicalcounter.model.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medicalcounter.model.db.dataClass.Organization
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface OrganizationDao{
    @Insert
    fun insert(organization: Organization):Single<Long>
    @Update
    fun update(organization: Organization):Completable
    @Delete
    fun delete (organization: Organization):Completable
    @Query("SELECT * FROM organization WHERE id = :id")
    fun get (id:Int):Single<Organization>
    @Query ("SELECT * FROM organization")
    fun getAll():Single< List<Organization>>
}