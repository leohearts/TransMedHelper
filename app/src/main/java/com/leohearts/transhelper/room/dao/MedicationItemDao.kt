package com.leohearts.transhelper.room.dao

import androidx.room.*
import com.leohearts.transhelper.room.entity.MedicationItemEntiry

@Dao
interface MedicationItemDao {

    @Query("SELECT * FROM MedicationItemTable")
    fun getAll(): List<MedicationItemEntiry>

    @Query("SELECT * FROM MedicationItemTable WHERE id = :medicationItemId")
    fun loadById(medicationItemId: Int): MedicationItemEntiry

    @Query("SELECT * FROM MedicationItemTable WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): MedicationItemEntiry

    @Insert
    fun insertAll(vararg medicationItem: MedicationItemEntiry)

    @Delete
    fun delete(item: MedicationItemEntiry)

    @Update
    fun update(item: MedicationItemEntiry)

}