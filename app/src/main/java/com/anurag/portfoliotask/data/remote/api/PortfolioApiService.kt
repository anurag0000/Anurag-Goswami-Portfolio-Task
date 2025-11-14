package com.anurag.portfoliotask.data.remote.api

import com.anurag.portfoliotask.data.remote.model.HoldingsResponse
import retrofit2.http.GET

interface PortfolioApiService {
    @GET("https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io/")
    suspend fun getHoldings(): HoldingsResponse
}