package com.anurag.portfoliotask.data.local.entities
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "holdings")
data class HoldingsEntity(
    @PrimaryKey val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double
)