package com.example.medicalcounter.model.db

import android.annotation.SuppressLint
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.medicalcounter.model.db.dao.CounterDao
import com.example.medicalcounter.model.db.dao.MedicineDao
import com.example.medicalcounter.model.db.dao.OrganizationDao
import com.example.medicalcounter.model.db.dao.PeriodDao
import com.example.medicalcounter.model.db.dataClass.Counter
import com.example.medicalcounter.model.db.dataClass.Medicine
import com.example.medicalcounter.model.db.dataClass.Organization
import com.example.medicalcounter.model.db.dataClass.Period


@Database(entities = [
    Counter::class,
    Organization::class,
    Period::class,
    Medicine::class
],
    version=10,
    exportSchema = true//,
    //autoMigrations = [AutoMigration(from = 1, to = 1)]
 )
abstract class MedicalDatabase : RoomDatabase(){
    companion object{
        const val NAME = "medical_counter_db"
    }
    abstract val periodDao: PeriodDao
    abstract val counterDao: CounterDao
    abstract val organizationDao: OrganizationDao
    abstract val medicineDao: MedicineDao
}
