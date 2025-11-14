package com.anurag.portfoliotask

import com.anurag.portfoliotask.domain.calculator.PortfolioCalculator
import com.anurag.portfoliotask.domain.model.Holding
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class PortfolioCalculatorTest {

    private lateinit var calculator: PortfolioCalculator

    @Before
    fun setup() {
        calculator = PortfolioCalculator()
    }

    @Test
    fun CalculateSummaryWithEmptyListReturnsZeroValues() {
        val holdings = emptyList<Holding>()
        val summary = calculator.calculateSummary(holdings)

        assertEquals(0.0, summary.currentValue, 0.01)
        assertEquals(0.0, summary.totalInvestment, 0.01)
        assertEquals(0.0, summary.totalPNL, 0.01)
        assertEquals(0.0, summary.todaysPNL, 0.01)
    }

    @Test
    fun CalculateSummaryWithSingleHoldingCalculatesCorrectly() {
        val holdings = listOf(
            Holding("AAPL", 10, 150.0, 140.0, 148.0)
        )

        val summary = calculator.calculateSummary(holdings)

        assertEquals(1500.0, summary.currentValue, 0.01)
        assertEquals(1400.0, summary.totalInvestment, 0.01)
        assertEquals(100.0, summary.totalPNL, 0.01)
        assertEquals(-20.0, summary.todaysPNL, 0.01)
    }

    @Test
    fun CalculateSummaryWithMultipleHoldingsCalculatesCorrectly() {
        val holdings = listOf(
            Holding("AAPL", 10, 150.0, 140.0, 148.0),
            Holding("GOOGL", 5, 2800.0, 2700.0, 2790.0),
            Holding("TSLA", 15, 700.0, 650.0, 695.0)
        )

        val summary = calculator.calculateSummary(holdings)

        assertEquals(26000.0, summary.currentValue, 0.01)
        assertEquals(24650.0, summary.totalInvestment, 0.01)
        assertEquals(1350.0, summary.totalPNL, 0.01)
        assertEquals(-145.0, summary.todaysPNL, 0.01)
    }

    @Test
    fun CalculateSummaryWithNegativePNL() {
        val holdings = listOf(
            Holding("LOSS", 100, 50.0, 80.0, 55.0)
        )

        val summary = calculator.calculateSummary(holdings)

        assertEquals(5000.0, summary.currentValue, 0.01)
        assertEquals(8000.0, summary.totalInvestment, 0.01)
        assertEquals(-3000.0, summary.totalPNL, 0.01)
        assertEquals(500.0, summary.todaysPNL, 0.01)
    }

    @Test
    fun CalculateSummaryWithZeroQuantityHoldings() {
        val holdings = listOf(
            Holding("ZERO", 0, 100.0, 100.0, 100.0),
            Holding("AAPL", 10, 150.0, 140.0, 148.0)
        )

        val summary = calculator.calculateSummary(holdings)

        assertEquals(1500.0, summary.currentValue, 0.01)
        assertEquals(1400.0, summary.totalInvestment, 0.01)
        assertEquals(100.0, summary.totalPNL, 0.01)
    }


    @Test
    fun CalculateSummaryWithFractionalShares() {
        val holdings = listOf(
            Holding("AAPL", 10, 150.5, 140.25, 148.75)
        )

        val summary = calculator.calculateSummary(holdings)

        assertEquals(1505.0, summary.currentValue, 0.01)
        assertEquals(1402.5, summary.totalInvestment, 0.01)
        assertEquals(102.5, summary.totalPNL, 0.01)
    }


    @Test
    fun CalculateSummaryWithMixedPositiveAndNegativeHoldings() {
        val holdings = listOf(
            Holding("WINNER", 10, 200.0, 100.0, 195.0),
            Holding("LOSER", 10, 50.0, 100.0, 55.0),
            Holding("NEUTRAL", 10, 100.0, 100.0, 100.0)
        )

        val summary = calculator.calculateSummary(holdings)

        assertEquals(3500.0, summary.currentValue, 0.01)
        assertEquals(3000.0, summary.totalInvestment, 0.01)
        assertEquals(500.0, summary.totalPNL, 0.01)
        assertEquals(0.0, summary.todaysPNL, 0.01)
    }

    @Test
    fun CalculateSummaryHandlesDecimalPrecisionCorrectly() {
        val holdings = listOf(
            Holding("PREC", 7, 123.456, 100.789, 120.123)
        )

        val summary = calculator.calculateSummary(holdings)

        assertEquals(864.192, summary.currentValue, 0.01)
        assertEquals(705.523, summary.totalInvestment, 0.01)
        assertEquals(158.669, summary.totalPNL, 0.01)
        assertEquals(-23.331, summary.todaysPNL, 0.01)
    }
}
