package com.anurag.portfoliotask

import com.anurag.portfoliotask.data.repository.PortfolioRepository
import com.anurag.portfoliotask.domain.model.Holding
import com.anurag.portfoliotask.domain.model.PortfolioSummary
import com.anurag.portfoliotask.domain.usecase.GetPortfolioUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetPortfolioUseCaseTest {

    private lateinit var useCase: GetPortfolioUseCase
    private lateinit var repository: PortfolioRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetPortfolioUseCase(repository)
    }

    @Test
    fun `InvokeDelegatesToRepositoryAndReturnsHoldingsAndSummary`() = runTest {
        val holdings = listOf(
            Holding("AAPL", 10, 150.0, 140.0, 148.0),
            Holding("GOOGL", 5, 2800.0, 2700.0, 2790.0)
        )
        val summary = PortfolioSummary(15500.0, 14900.0, 600.0, 50.0)
        coEvery { repository.getHoldingsWithSummary() } returns Pair(holdings, summary)

        val result = useCase()

        assertEquals(holdings, result.first)
        assertEquals(summary, result.second)
        coVerify(exactly = 1) { repository.getHoldingsWithSummary() }
    }

    @Test
    fun `InvokeReturnsEmptyListWhenRepositoryReturnsEmpty`() = runTest {
        val emptyHoldings = emptyList<Holding>()
        val emptySummary = PortfolioSummary(0.0, 0.0, 0.0, 0.0)
        coEvery { repository.getHoldingsWithSummary() } returns Pair(emptyHoldings, emptySummary)

        val result = useCase()

        assertEquals(emptyList<Holding>(), result.first)
        assertEquals(emptySummary, result.second)
    }

    @Test(expected = Exception::class)
    fun `InvokeThrowsExceptionWhenRepositoryThrowsException`() = runTest {
        coEvery { repository.getHoldingsWithSummary() } throws Exception("Network error")
        useCase()
    }

    @Test
    fun `InvokeHandlesSingleHoldingCorrectly`() = runTest {
        val singleHolding = listOf(Holding("TSLA", 1, 700.0, 650.0, 695.0))
        val summary = PortfolioSummary(700.0, 650.0, 50.0, 5.0)
        coEvery { repository.getHoldingsWithSummary() } returns Pair(singleHolding, summary)

        val result = useCase()

        assertEquals(1, result.first.size)
        assertEquals("TSLA", result.first[0].symbol)
        assertEquals(50.0, result.second.totalPNL, 0.01)
    }
}
