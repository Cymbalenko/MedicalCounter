package com.example.medicalcounter.model.db.dataClass

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = Organization::class,
    parentColumns = ["id"],
    childColumns = ["organizationId"]
)]
)
 data class Medicine(
    var title:String,
    var organizationId:Int,
    var organizationName:String


){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}