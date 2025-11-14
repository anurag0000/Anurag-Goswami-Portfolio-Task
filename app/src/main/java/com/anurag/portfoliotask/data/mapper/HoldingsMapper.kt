package com.anurag.portfoliotask.data.mapper

import com.anurag.portfoliotask.data.local.entities.HoldingsEntity
import com.anurag.portfoliotask.data.remote.model.HoldingItem
import com.anurag.portfoliotask.domain.model.Holding

fun HoldingItem.toEntity() = HoldingsEntity(symbol, quantity, ltp, avgPrice, close)
fun HoldingsEntity.toDomain() = Holding(symbol, quantity, ltp, avgPrice, close)
fun HoldingItem.toDomain() = Holding(symbol, quantity, ltp, avgPrice, close)