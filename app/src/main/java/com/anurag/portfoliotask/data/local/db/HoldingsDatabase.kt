package com.anurag.portfoliotask.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anurag.portfoliotask.data.local.dao.HoldingsDao
import com.anurag.portfoliotask.data.local.entities.HoldingsEntity

@Database(entities = [HoldingsEntity::class], version = 1)
abstract class HoldingsDatabase : RoomDatabase() {
    abstract fun holdingsDao(): HoldingsDao
}