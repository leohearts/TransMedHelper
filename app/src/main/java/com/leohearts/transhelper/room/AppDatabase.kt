package com.leohearts.transhelper.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.leohearts.transhelper.room.dao.MedicationItemDao
import com.leohearts.transhelper.room.entity.MedicationItemEntiry

@Database(entities = [MedicationItemEntiry::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicationItemDao(): MedicationItemDao
}
