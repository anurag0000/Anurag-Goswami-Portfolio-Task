package com.anurag.portfoliotask.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anurag.portfoliotask.domain.model.Holding
import com.anurag.portfoliotask.domain.model.PortfolioSummary
import com.anurag.portfoliotask.ui.viewmodel.PortfolioViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(viewModel: PortfolioViewModel = hiltViewModel()) {
    val holdings by viewModel.holdings.collectAsStateWithLifecycle()
    val summary by viewModel.summary.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var selectedTab by remember { mutableStateOf("Holdings") }
    var expanded by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadPortfolio()
    }

    Column(
        Modifier
            .fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = if (selectedTab == "Positions") 0 else 1,
            containerColor = Color.White,
            contentColor = Color(0xFF0A2A66)
        ) {
            Tab(
                selected = selectedTab == "Positions",
                onClick = { selectedTab = "Positions" },
                text = { Text("POSITIONS") }
            )
            Tab(
                selected = selectedTab == "Holdings",
                onClick = { selectedTab = "Holdings" },
                text = { Text("HOLDINGS") }
            )
        }

        when {
            isLoading && !isRefreshing -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            error != null && !isRefreshing -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error: $error",
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadPortfolio() }) {
                            Text("Retry")
                        }
                    }
                }
            }

            holdings.isEmpty() && !isRefreshing -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No holdings found")
                }
            }

            else -> {
                when (selectedTab) {
                    "Holdings" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                        ) {
                            PullToRefreshBox(
                                state = pullRefreshState,
                                isRefreshing = isRefreshing,
                                onRefresh = {
                                    scope.launch {
                                        isRefreshing = true
                                        viewModel.loadPortfolio()
                                        delay(500)
                                        isRefreshing = false
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(holdings, key = { it.symbol }) { holding ->
                                        HoldingItem(holding)
                                        HorizontalDivider()
                                    }
                                }
                            }
                            summary?.let { summaryData ->
                                SummaryCard(summaryData, expanded) { expanded = !expanded }
                            }
                        }
                    }

                    "Positions" -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Positions feature coming soon")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun HoldingItem(holding: Holding) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(holding.symbol, fontWeight = FontWeight.Bold)
            Text("Qty: ${holding.quantity}")
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("LTP: ₹${holding.ltp}")
            Text(
                "P&L: ₹${
                    String.format(
                        "%.2f",
                        (holding.ltp - holding.avgPrice) * holding.quantity
                    )
                }",
                color = if ((holding.ltp - holding.avgPrice) >= 0) Color(0xFF2E7D32) else Color(
                    0xFFC62828
                )
            )
        }
    }
}

@Composable
fun SummaryCard(
    summary: PortfolioSummary,
    expanded: Boolean,
    onExpandToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandToggle() }
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Profit & Loss*",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse summary" else "Expand summary",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Text(
                    text = "₹${String.format("%.2f", summary.totalPNL)} " +
                            if (summary.totalInvestment > 0) {
                                "(${
                                    String.format(
                                        "%.2f",
                                        (summary.totalPNL / summary.totalInvestment) * 100
                                    )
                                }%)"
                            } else {
                                "(0.00%)"
                            },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (summary.totalPNL >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }

            if (expanded) {
                Spacer(Modifier.height(8.dp))
                Text("Current value: ₹${String.format("%.2f", summary.currentValue)}")
                Text("Total investment: ₹${String.format("%.2f", summary.totalInvestment)}")
                Text("Today's P&L: ₹${String.format("%.2f", summary.todaysPNL)}")
            }
        }
    }
}