package com.anurag.portfoliotask.domain.model

data class PortfolioSummary(
    val currentValue: Double,
    val totalInvestment: Double,
    val totalPNL: Double,
    val todaysPNL: Double
)