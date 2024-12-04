package com.tradesmanhandy.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tradesmanhandy.app.presentation.home.HomeScreen
import com.tradesmanhandy.app.presentation.pending.PendingBookingsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object PendingBookings : Screen("pending_bookings")
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.PendingBookings.route) {
            PendingBookingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
