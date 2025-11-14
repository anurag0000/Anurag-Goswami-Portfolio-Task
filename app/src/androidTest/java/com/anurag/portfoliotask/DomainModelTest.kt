package com.anurag.portfoliotask

import com.anurag.portfoliotask.domain.model.Holding
import com.anurag.portfoliotask.domain.model.PortfolioSummary
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotSame
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DomainModelTest {

    @Test
    fun HoldingDataClassCreatesInstanceWithCorrectProperties() {
        val holding = Holding(
            symbol = "AAPL",
            quantity = 10,
            ltp = 150.0,
            avgPrice = 140.0,
            close = 148.0
        )

        assertEquals("AAPL", holding.symbol)
        assertEquals(10, holding.quantity)
        assertEquals(150.0, holding.ltp, 0.01)
        assertEquals(140.0, holding.avgPrice, 0.01)
        assertEquals(148.0, holding.close, 0.01)
    }

    @Test
    fun HoldingDataClassEqualityWorksCorrectly() {
        val holding1 = Holding("AAPL", 10, 150.0, 140.0, 148.0)
        val holding2 = Holding("AAPL", 10, 150.0, 140.0, 148.0)
        val holding3 = Holding("GOOGL", 10, 150.0, 140.0, 148.0)

        assertEquals(holding1, holding2)
        assertFalse(holding1 == holding3)
    }

    @Test
    fun HoldingDataClassCopyCreatesNewInstance() {
        val original = Holding("AAPL", 10, 150.0, 140.0, 148.0)
        val copy = original.copy(quantity = 20)

        assertNotSame(original, copy)
        assertEquals("AAPL", copy.symbol)
        assertEquals(20, copy.quantity)
        assertEquals(150.0, copy.ltp, 0.01)
    }

    @Test
    fun HoldingDataClassHashCodeWorksCorrectly() {
        val holding1 = Holding("AAPL", 10, 150.0, 140.0, 148.0)
        val holding2 = Holding("AAPL", 10, 150.0, 140.0, 148.0)

        assertEquals(holding1.hashCode(), holding2.hashCode())
    }

    @Test
    fun HoldingDataClassToStringContainsAllProperties() {
        val holding = Holding("TSLA", 5, 700.0, 650.0, 695.0)
        val str = holding.toString()

        assertTrue(str.contains("TSLA"))
        assertTrue(str.contains("5"))
        assertTrue(str.contains("700.0"))
    }

    @Test
    fun PortfolioSummaryCreatesInstanceWithCorrectProperties() {
        val summary = PortfolioSummary(10000.0, 9000.0, 1000.0, 50.0)

        assertEquals(10000.0, summary.currentValue, 0.01)
        assertEquals(9000.0, summary.totalInvestment, 0.01)
        assertEquals(1000.0, summary.totalPNL, 0.01)
        assertEquals(50.0, summary.todaysPNL, 0.01)
    }

    @Test
    fun PortfolioSummaryEqualityWorksCorrectly() {
        val s1 = PortfolioSummary(10000.0, 9000.0, 1000.0, 50.0)
        val s2 = PortfolioSummary(10000.0, 9000.0, 1000.0, 50.0)
        val s3 = PortfolioSummary(10000.0, 9000.0, 1000.0, 100.0)

        assertEquals(s1, s2)
        assertFalse(s1 == s3)
    }

    @Test
    fun PortfolioSummaryCopyCreatesNewInstance() {
        val original = PortfolioSummary(10000.0, 9000.0, 1000.0, 50.0)
        val copy = original.copy(todaysPNL = 100.0)

        assertNotSame(original, copy)
        assertEquals(10000.0, copy.currentValue, 0.01)
        assertEquals(100.0, copy.todaysPNL, 0.01)
    }

    @Test
    fun PortfolioSummaryHandlesZeroValues() {
        val summary = PortfolioSummary(0.0, 0.0, 0.0, 0.0)

        assertEquals(0.0, summary.currentValue, 0.01)
        assertEquals(0.0, summary.totalInvestment, 0.01)
        assertEquals(0.0, summary.totalPNL, 0.01)
        assertEquals(0.0, summary.todaysPNL, 0.01)
    }

    @Test
    fun PortfolioSummaryHandlesNegativeValues() {
        val summary = PortfolioSummary(8000.0, 10000.0, -2000.0, -100.0)

        assertEquals(8000.0, summary.currentValue, 0.01)
        assertEquals(10000.0, summary.totalInvestment, 0.01)
        assertEquals(-2000.0, summary.totalPNL, 0.01)
        assertEquals(-100.0, summary.todaysPNL, 0.01)
    }

    @Test
    fun PortfolioSummaryHandlesLargeValues() {
        val summary = PortfolioSummary(1_000_000_000.0, 900_000_000.0, 100_000_000.0, 1_000_000.0)

        assertEquals(1_000_000_000.0, summary.currentValue, 0.01)
        assertEquals(900_000_000.0, summary.totalInvestment, 0.01)
        assertEquals(100_000_000.0, summary.totalPNL, 0.01)
        assertEquals(1_000_000.0, summary.todaysPNL, 0.01)
    }

    @Test
    fun HoldingWithZeroQuantity() {
        val holding = Holding("ZERO", 0, 100.0, 100.0, 100.0)

        assertEquals(0, holding.quantity)
        assertEquals("ZERO", holding.symbol)
    }

    @Test
    fun HoldingWithNegativeQuantity() {
        val holding = Holding("SHORT", -10, 150.0, 140.0, 148.0)

        assertEquals(-10, holding.quantity)
    }

    @Test
    fun HoldingWithVerySmallPriceValues() {
        val holding = Holding("PENNY", 1000, 0.01, 0.009, 0.0095)

        assertEquals(0.01, holding.ltp, 0.001)
        assertEquals(0.009, holding.avgPrice, 0.001)
        assertEquals(0.0095, holding.close, 0.001)
    }

    @Test
    fun PortfolioSummaryWithDecimalPrecision() {
        val summary = PortfolioSummary(
            12345.6789,
            11111.1111,
            1234.5678,
            123.4567
        )

        assertEquals(12345.6789, summary.currentValue)
        assertEquals(11111.1111, summary.totalInvestment)
        assertEquals(1234.5678, summary.totalPNL)
        assertEquals(123.4567, summary.todaysPNL)
    }

    @Test
    fun HoldingComponentFunctionsWorkCorrectly() {
        val holding = Holding("AAPL", 10, 150.0, 140.0, 148.0)
        val (symbol, quantity, ltp, avgPrice, close) = holding

        assertEquals("AAPL", symbol)
        assertEquals(10, quantity)
        assertEquals(150.0, ltp, 0.01)
        assertEquals(140.0, avgPrice, 0.01)
        assertEquals(148.0, close, 0.01)
    }

    @Test
    fun PortfolioSummaryComponentFunctionsWorkCorrectly() {
        val summary = PortfolioSummary(10000.0, 9000.0, 1000.0, 50.0)
        val (cv, ti, pnl, today) = summary

        assertEquals(10000.0, cv, 0.01)
        assertEquals(9000.0, ti, 0.01)
        assertEquals(1000.0, pnl, 0.01)
        assertEquals(50.0, today, 0.01)
    }
}
