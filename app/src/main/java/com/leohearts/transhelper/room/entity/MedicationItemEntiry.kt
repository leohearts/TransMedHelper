package com.leohearts.transhelper.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "MedicationItemTable")
data class MedicationItemEntiry (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "dose") var dose: String,
    @ColumnInfo(name = "unit") var unit: String,
    @ColumnInfo(name = "creationDate") var creationDate: Date,      // cTime to calculate when to start reporting every * days
    @ColumnInfo(name = "remindTime") var remindTime: ArrayList<ArrayList<Int>>      //  List of <Hour, Min>
)