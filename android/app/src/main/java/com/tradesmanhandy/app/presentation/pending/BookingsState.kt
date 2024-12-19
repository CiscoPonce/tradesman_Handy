package com.tradesmanhandy.app.presentation.pending

import com.tradesmanhandy.app.domain.model.Booking

sealed class BookingsState {
    object Loading : BookingsState()
    data class Success(
        val pendingBookings: List<Booking>,
        val confirmedBookings: List<Booking>,
        val completedBookings: List<Booking>
    ) : BookingsState()
    data class Error(val message: String) : BookingsState()
}
