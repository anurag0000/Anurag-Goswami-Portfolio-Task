package com.anurag.portfoliotask.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.anurag.portfoliotask.ui.screens.FundsScreen
import com.anurag.portfoliotask.ui.screens.InvestScreen
import com.anurag.portfoliotask.ui.screens.OrdersScreen
import com.anurag.portfoliotask.ui.screens.PortfolioScreen
import com.anurag.portfoliotask.ui.screens.WatchlistScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph() {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem("Watchlist", Icons.Default.List, "watchlist"),
        BottomNavItem("Orders", Icons.Default.ShoppingCart, "orders"),
        BottomNavItem("Portfolio", Icons.Default.Home, "portfolio"),
        BottomNavItem("Funds", Icons.Default.Add, "funds"),
        BottomNavItem("Invest", Icons.Default.Create, "invest")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentTitle = when (currentRoute) {
        "watchlist" -> "Watchlist"
        "orders" -> "Orders"
        "portfolio" -> "Portfolio"
        "funds" -> "Funds"
        "invest" -> "Invest"
        else -> "Portfolio"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentTitle) },
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                    }
                },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A2A66),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = { BottomNavigationBar(navController, items) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "portfolio",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("watchlist") { WatchlistScreen("Watchlist") }
            composable("orders") { OrdersScreen("Orders") }
            composable("portfolio") { PortfolioScreen() }
            composable("funds") { FundsScreen("Funds") }
            composable("invest") { InvestScreen("Invest") }
        }
    }
}