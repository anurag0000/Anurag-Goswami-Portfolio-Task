package com.anurag.portfoliotask.domain.usecase

import com.anurag.portfoliotask.data.repository.PortfolioRepository
import com.anurag.portfoliotask.domain.model.Holding
import com.anurag.portfoliotask.domain.model.PortfolioSummary
import javax.inject.Inject

class GetPortfolioUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    suspend operator fun invoke(): Pair<List<Holding>, PortfolioSummary> {
        return repository.getHoldingsWithSummary()
    }
}