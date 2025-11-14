package com.anurag.portfoliotask.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent
import kotlin.math.abs


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
        Column(modifier = Modifier.background(Color.White)) {
            TabRow(
                selectedTabIndex = if (selectedTab == "Positions") 0 else 1,
                containerColor = Color.White,
                contentColor = Color.DarkGray,
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[if (selectedTab == "Positions") 0 else 1])
                            .fillMaxWidth()
                            .padding(horizontal = 64.dp)
                    ) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.DarkGray
                        )
                    }
                },
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == "Positions",
                    onClick = { selectedTab = "Positions" },
                    text = {
                        Text(
                            "POSITIONS",
                            fontSize = 14.sp,
                            fontWeight = if (selectedTab == "Positions") FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == "Positions") Color.DarkGray else Color.Gray
                        )
                    }
                )
                Tab(
                    selected = selectedTab == "Holdings",
                    onClick = { selectedTab = "Holdings" },
                    text = {
                        Text(
                            "HOLDINGS",
                            fontSize = 14.sp,
                            fontWeight = if (selectedTab == "Holdings") FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == "Holdings") Color.DarkGray  else Color.Gray
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
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
                                .background(Color.White)
                                .padding(horizontal = 2.dp)
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
                        Box(Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
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
    val pnlValue = (holding.ltp - holding.avgPrice) * holding.quantity
    val isProfit = pnlValue >= 0
    val displayPnlValue = (if (isProfit) "" else "-") +
            "₹${String.format("%.2f", abs(pnlValue))}"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(holding.symbol, fontWeight = FontWeight.Bold)
            Row {
                Text(text = "NET QTY: ",fontSize = 12.sp, color = Color.Gray)
                Text("${holding.quantity}")
            }
        }
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row {
                Text(text = "LTP: ",fontSize = 12.sp, color = Color.Gray)
                Text("₹${holding.ltp}")
            }
            Row {
                Text(text = "P&L: ",fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = displayPnlValue,
                    color = if (isProfit) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }
        }
    }
}

@Composable
fun SummaryCard(
    summary: PortfolioSummary,
    expanded: Boolean,
    onExpandToggle: () -> Unit
) {
    val pnl = summary.totalPNL
    val isProfit = pnl >= 0
    val investment = summary.totalInvestment

    val pnlText = (if (isProfit) "" else "-") +
            "₹${String.format("%.2f", abs(pnl))}"

    val percent = if (investment > 0) {
        String.format("%.2f", (pnl / investment) * 100)
    } else {
        "0.00"
    }
    val todaysPnl = summary.todaysPNL
    val isTodaysPnlProfit = todaysPnl >= 0

    val todaysPnlText = (if (isTodaysPnlProfit) "" else "-") +
            "₹${String.format("%.2f", abs(todaysPnl))}"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandToggle() }
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0E0E0)
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            if (expanded) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Current value*",
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "₹ ${String.format("%.2f", summary.currentValue)}",
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total investment*",
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "₹ ${String.format("%.2f", summary.totalInvestment)}",
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Profit & Loss*",
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    Text(
                        text = todaysPnlText,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = if (isTodaysPnlProfit) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }
                HorizontalDivider()
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Profit & Loss*",
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse summary" else "Expand summary",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Text(
                    text = "$pnlText ($percent%)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (isProfit) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }
        }
    }
}