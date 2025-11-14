package com.anurag.portfoliotask.domain.calculator

import com.anurag.portfoliotask.domain.model.Holding
import com.anurag.portfoliotask.domain.model.PortfolioSummary
import javax.inject.Inject

class PortfolioCalculator @Inject constructor() {
    fun calculateSummary(holdings: List<Holding>): PortfolioSummary {
        if (holdings.isEmpty()) {
            return PortfolioSummary(0.0, 0.0, 0.0, 0.0)
        }

        val currentValue = holdings.sumOf { it.ltp * it.quantity }
        val totalInvestment = holdings.sumOf { it.avgPrice * it.quantity }
        val totalPNL = currentValue - totalInvestment
        val todaysPNL = holdings.sumOf { (it.close - it.ltp) * it.quantity }

        return PortfolioSummary(currentValue, totalInvestment, totalPNL, todaysPNL)
    }
}