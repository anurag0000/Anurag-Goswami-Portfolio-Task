package com.anurag.portfoliotask.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anurag.portfoliotask.data.local.entities.HoldingsEntity

@Dao
interface HoldingsDao {

    @Query("SELECT * FROM holdings")
    suspend fun getAllHoldings(): List<HoldingsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(holdings: List<HoldingsEntity>)

    @Query("DELETE FROM holdings")
    suspend fun clearAll()
}