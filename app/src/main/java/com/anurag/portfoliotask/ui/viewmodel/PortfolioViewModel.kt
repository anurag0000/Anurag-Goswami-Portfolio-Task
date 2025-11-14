package com.anurag.portfoliotask.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anurag.portfoliotask.domain.model.Holding
import com.anurag.portfoliotask.domain.model.PortfolioSummary
import com.anurag.portfoliotask.domain.usecase.GetPortfolioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase
) : ViewModel() {

    private val _holdings = MutableStateFlow<List<Holding>>(emptyList())
    val holdings = _holdings.asStateFlow()

    private val _summary = MutableStateFlow<PortfolioSummary?>(null)
    val summary = _summary.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadPortfolio() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val (list, summary) = getPortfolioUseCase.invoke()
                _holdings.value = list
                _summary.value = summary
                launch {
                    val updated = getPortfolioUseCase.refresh()
                    if (updated != null) {
                        _holdings.value = updated.first
                        _summary.value = updated.second
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load portfolio"
            } finally {
                _isLoading.value = false
            }
        }
    }
}