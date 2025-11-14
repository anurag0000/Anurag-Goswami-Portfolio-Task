package com.anurag.portfoliotask

import com.anurag.portfoliotask.domain.model.Holding
import com.anurag.portfoliotask.domain.model.PortfolioSummary
import com.anurag.portfoliotask.domain.usecase.GetPortfolioUseCase
import com.anurag.portfoliotask.ui.viewmodel.PortfolioViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PortfolioViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: PortfolioViewModel
    private lateinit var getPortfolioUseCase: GetPortfolioUseCase

    @Before
    fun setup() {
        getPortfolioUseCase = mockk()
        viewModel = PortfolioViewModel(getPortfolioUseCase)
    }

    @Test
    fun `loadPortfolioSuccessUpdatesHoldingsAndSummary`() = runTest {

        val holdings = listOf(
            Holding("AAPL", 10, 150.0, 140.0, 148.0)
        )
        val summary = PortfolioSummary(1500.0, 1400.0, 100.0, 20.0)
        coEvery { getPortfolioUseCase() } returns Pair(holdings, summary)

        viewModel.loadPortfolio()

        advanceUntilIdle()

        assertEquals(holdings, viewModel.holdings.value)
        assertEquals(summary, viewModel.summary.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)

        coVerify(exactly = 1) { getPortfolioUseCase() }
    }

    @Test
    fun `loadPortfolioFailureSetsErrorState`() = runTest {

        val errorMessage = "Network error"
        coEvery { getPortfolioUseCase() } throws Exception(errorMessage)

        viewModel.loadPortfolio()
        advanceUntilIdle()

        assertEquals(emptyList<Holding>(), viewModel.holdings.value)
        assertEquals(errorMessage, viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `initialStateIsEmpty`() {
        assertEquals(emptyList<Holding>(), viewModel.holdings.value)
        assertNull(viewModel.summary.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }
}
