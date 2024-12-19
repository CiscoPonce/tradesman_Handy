package com.tradesmanhandy.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tradesmanhandy.app.presentation.bookings.BookingsScreen
import com.tradesmanhandy.app.presentation.bookingdetail.BookingDetailScreen
import com.tradesmanhandy.app.presentation.home.HomeScreen

object NavigationDestinations {
    const val HOME = "home"
    const val BOOKINGS = "bookings"
    const val BOOKING_DETAIL = "booking_detail"
}

sealed class Screen(val route: String) {
    object Home : Screen(NavigationDestinations.HOME)
    object Bookings : Screen("${NavigationDestinations.BOOKINGS}?tab={tab}") {
        fun createRoute(tab: Int = 0) = "${NavigationDestinations.BOOKINGS}?tab=$tab"
    }
    object BookingDetail : Screen("${NavigationDestinations.BOOKING_DETAIL}/{bookingId}") {
        fun createRoute(bookingId: String) = "${NavigationDestinations.BOOKING_DETAIL}/$bookingId"
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController
            )
        }

        composable(
            route = Screen.Bookings.route,
            arguments = listOf(
                navArgument("tab") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val tab = backStackEntry.arguments?.getInt("tab") ?: 0
            BookingsScreen(
                selectedTab = tab,
                onNavigateToBookingDetail = { bookingId ->
                    navController.navigate(Screen.BookingDetail.createRoute(bookingId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.BookingDetail.route,
            arguments = listOf(
                navArgument("bookingId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId")
            BookingDetailScreen(
                bookingId = bookingId ?: "",
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
