package com.example.medicalcounter.model.repository

import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.medicalcounter.MedicalCounterApplication
import com.example.medicalcounter.model.db.MedicalDatabase
import com.example.medicalcounter.model.db.dataClass.Counter
import com.example.medicalcounter.model.db.dataClass.Medicine
import com.example.medicalcounter.model.db.dataClass.Organization
import com.example.medicalcounter.model.db.dataClass.Period
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

object RoomRepository {
    private val db:MedicalDatabase = createDatabase()


    //Period
    fun insertPeriod(period: Period): Single<Long> {
        return db.periodDao.insert(period)
    }
    fun updatePeriod(period: Period): Completable {
        return db.periodDao.update(period)
    }
    fun deletePeriod(period:Period): Completable {
        return db.periodDao.delete(period)
    }
    fun getPeriod(id:Int): LiveData<Period> {
        return db.periodDao.get(id)
    }
    fun getPeriodLast(): Single<Period> {
        return db.periodDao.getLast()
    }
    fun getPeriodY(year:Int, month:Int): Single< Period> {
        return db.periodDao.getP(month,year)
    }

    //Organization
    fun insertOrganization(organization: Organization): Single<Long> {
        return db.organizationDao.insert(organization)
    }
    fun updateOrganization(organization: Organization): Completable {
        return db.organizationDao.update(organization)
    }
    fun deleteOrganization(organization: Organization): Completable {
        return db.organizationDao.delete(organization)
    }
    fun getOrganization(id:Int): Single<Organization> {
        return db.organizationDao.get(id)
    }
    fun getOrganizationAll(): Single<List<Organization>> {
        return db.organizationDao.getAll()
    }

    //Counter
    fun insertCounter(counter: Counter): Single<Long> {
        return db.counterDao.insert(counter)
    }
    fun updateCounter(counter: Counter): Completable {
        return db.counterDao.update(counter)
    }
    fun deleteCounter(counter: Counter): Completable {
        return db.counterDao.delete(counter)
    }
    fun getCounter(id:Int): LiveData<Counter> {
        return db.counterDao.get(id)
    }
    fun getCounterPeriodId(id:Int): Single<List<Counter>> {
        return db.counterDao.getByPeriodId(id)
    }
    fun getByPeriodIdMedicine(periodId:Int,medicineId:Int ): Single<Counter> {
        return db.counterDao.getByPeriodIdMedicine(periodId,medicineId )
    }

    //Medicine
    fun insertMedicine(medicine: Medicine): Single<Long> {
        return db.medicineDao.insert(medicine)
    }
    fun updateMedicine(medicine: Medicine): Completable {
        return db.medicineDao.update(medicine)
    }
    fun deleteMedicine(medicine: Medicine): Completable {
        return db.medicineDao.delete(medicine)
    }
    fun getMedicine(id:Int): Single<Medicine> {
        return db.medicineDao.get(id)
    }
    fun getMedicineByOrganizationId(id:Int): LiveData<List<Medicine>> {
        return db.medicineDao.getByOrganizationId(id)
    }
    fun getMedicineAll( ): Single<List<Medicine>> {
        return db.medicineDao.getAll()
    }


    private fun createDatabase():MedicalDatabase{
        return Room.databaseBuilder(
            MedicalCounterApplication.instance!!,
            MedicalDatabase::class.java,
            MedicalDatabase.NAME)
            .fallbackToDestructiveMigration().build()
    }
}