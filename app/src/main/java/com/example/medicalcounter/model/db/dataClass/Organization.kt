package com.example.medicalcounter.model.db.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Organization (
    var  title:String,
    var  countMedicine:Int,
    var  agent:String="",
    var  phone:String=""

){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}