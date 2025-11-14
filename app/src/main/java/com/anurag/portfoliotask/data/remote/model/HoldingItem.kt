package com.anurag.portfoliotask.data.remote.model

import com.google.gson.annotations.SerializedName

data class HoldingItem(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("avgPrice") val avgPrice: Double,
    @SerializedName("ltp") val ltp: Double,
    @SerializedName("close") val close: Double
)
