package com.example.medicalcounter.model.db.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Period(
    var title: String,
    var year: Int,
    var month: Int
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

