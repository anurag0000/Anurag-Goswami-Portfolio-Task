package com.anurag.portfoliotask.data.repository

import com.anurag.portfoliotask.data.cache.InMemoryCache
import com.anurag.portfoliotask.data.local.dao.HoldingsDao
import com.anurag.portfoliotask.data.mapper.toDomain
import com.anurag.portfoliotask.data.mapper.toEntity
import com.anurag.portfoliotask.data.remote.api.PortfolioApiService
import com.anurag.portfoliotask.domain.calculator.PortfolioCalculator
import com.anurag.portfoliotask.domain.model.Holding
import com.anurag.portfoliotask.domain.model.PortfolioSummary
import javax.inject.Inject

class PortfolioRepository @Inject constructor(
    private val api: PortfolioApiService,
    private val dao: HoldingsDao,
    private val calculator: PortfolioCalculator
) {
    suspend fun getHoldingsWithSummary(): Pair<List<Holding>, PortfolioSummary> {

        InMemoryCache.holdings?.let {
            return it to calculator.calculateSummary(it)
        }


        val local = dao.getAllHoldings()
        if (local.isNotEmpty()) {
            val holdings = local.map { it.toDomain() }
            InMemoryCache.holdings = holdings
            return holdings to calculator.calculateSummary(holdings)
        }

        val holdings = try {
            val response = api.getHoldings()

            val list = response.data.userHolding
            if (list.isEmpty()) {
                throw IllegalStateException("API returned empty holdings")
            }

            val domainList = list.map { it.toDomain() }

            InMemoryCache.holdings = domainList

            dao.clearAll()
            dao.insertAll(list.map { it.toEntity() })

            domainList

        } catch (e: Exception) {

            val message = when (e) {

                // No internet connection
                is java.net.UnknownHostException,
                is java.net.ConnectException,
                is java.net.NoRouteToHostException -> {
                    "No internet connection"
                }

                // Slow internet / server not responding
                is java.net.SocketTimeoutException -> {
                    "Connection timed out"
                }

                // Retrofit / OkHttp IO failures
                is java.io.IOException -> {
                    "Network error. Please check your connection"
                }

                // API error but reachable
                is retrofit2.HttpException -> {
                    "Server error: ${e.code()}"
                }

                // Everything else
                else -> {
                    "Something went wrong"
                }
            }

            throw Exception(message)
        }

        return holdings to calculator.calculateSummary(holdings)
    }

    suspend fun refreshHoldings(): Pair<List<Holding>, PortfolioSummary>? {
        return try {
            val response = api.getHoldings()

            val list = response.data.userHolding
            if (list.isEmpty()) return null

            val domainList = list.map { it.toDomain() }

            InMemoryCache.holdings = domainList
            dao.clearAll()
            dao.insertAll(list.map { it.toEntity() })

            domainList to calculator.calculateSummary(domainList)

        } catch (e: Exception) {
            null
        }
    }
}