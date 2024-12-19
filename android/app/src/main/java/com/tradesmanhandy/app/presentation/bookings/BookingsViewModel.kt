package com.tradesmanhandy.app.presentation.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradesmanhandy.app.data.model.Booking
import com.tradesmanhandy.app.data.model.BookingStatus
import com.tradesmanhandy.app.data.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {
    private val _pendingBookings = MutableStateFlow<List<Booking>>(emptyList())
    val pendingBookings: StateFlow<List<Booking>> = _pendingBookings.asStateFlow()

    private val _acceptedBookings = MutableStateFlow<List<Booking>>(emptyList())
    val acceptedBookings: StateFlow<List<Booking>> = _acceptedBookings.asStateFlow()

    private val _completedBookings = MutableStateFlow<List<Booking>>(emptyList())
    val completedBookings: StateFlow<List<Booking>> = _completedBookings.asStateFlow()

    init {
        loadBookings()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            try {
                repository.getTradesmanBookings("2b6fe808-b73b-4d16-915b-298b4d076c47")
                    .collect { bookings ->
                        _pendingBookings.value = bookings.filter { it.status == BookingStatus.PENDING }
                        _acceptedBookings.value = bookings.filter { it.status == BookingStatus.ACCEPTED }
                        _completedBookings.value = bookings.filter { it.status == BookingStatus.COMPLETED }
                    }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun confirmBooking(bookingId: String) {
        viewModelScope.launch {
            try {
                repository.acceptBooking(bookingId)
                loadBookings()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun rejectBooking(bookingId: String) {
        viewModelScope.launch {
            try {
                repository.rejectBooking(bookingId)
                loadBookings()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun navigateToManage(bookingId: String) {
        // This will be handled by the navigation in the UI
    }
}
