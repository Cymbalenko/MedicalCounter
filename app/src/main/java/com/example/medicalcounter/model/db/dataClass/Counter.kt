package com.example.medicalcounter.model.db.dataClass

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(foreignKeys = [ForeignKey(
    entity = Period::class,
    parentColumns = ["id"],
    childColumns = ["periodId"]
)]
)
data class Counter(
    var coun:Int,
    var medicineId:Int,
    var medicineTitle:String,
    var periodId:Int,
    var periodName:String,
    var organizationId:Int,
    var organizationName:String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
