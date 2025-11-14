package com.anurag.portfoliotask
import com.anurag.portfoliotask.data.cache.InMemoryCache
import com.anurag.portfoliotask.data.local.dao.HoldingsDao
import com.anurag.portfoliotask.data.remote.api.PortfolioApiService
import com.anurag.portfoliotask.data.repository.PortfolioRepository
import com.anurag.portfoliotask.domain.calculator.PortfolioCalculator
import com.anurag.portfoliotask.domain.model.Holding
import com.anurag.portfoliotask.domain.model.PortfolioSummary
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PortfolioRepositoryTest {

    private lateinit var repository: PortfolioRepository
    private lateinit var api: PortfolioApiService
    private lateinit var dao: HoldingsDao
    private lateinit var calculator: PortfolioCalculator

    @Before
    fun setup() {
        api = mockk()
        dao = mockk()
        calculator = mockk()
        every { calculator.calculateSummary(any()) } returns
                PortfolioSummary(
                    totalInvestment = 1400.0,
                    currentValue = 1500.0,
                    totalPNL = 100.0,
                    todaysPNL = 7.14
                )
        repository = PortfolioRepository(api, dao, calculator)
    }

    @Test
    fun `getHoldingsWithSummaryReturnsMemoryCacheIfAvailable`() = runTest {

        val holdings = listOf(Holding("AAPL", 10, 150.0, 140.0, 148.0))
        InMemoryCache.holdings = holdings

        val (result, summary) = repository.getHoldingsWithSummary()

        assertEquals(holdings, result)
        assertEquals(1500.0, summary.currentValue, 0.01)
        coVerify(exactly = 0) { dao.getAllHoldings() }
        coVerify(exactly = 0) { api.getHoldings() }
    }

    @After
    fun tearDown() {
        InMemoryCache.holdings = null
    }
}