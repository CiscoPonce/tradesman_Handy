package com.tradesmanhandy.app.presentation.booking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradesmanhandy.app.data.model.Booking
import com.tradesmanhandy.app.data.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BookingDetailViewModel"

sealed class BookingDetailState {
    object Loading : BookingDetailState()
    data class Success(val booking: Booking) : BookingDetailState()
    data class Error(val message: String) : BookingDetailState()
}

@HiltViewModel
class BookingDetailViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {

    private val _state = MutableStateFlow<BookingDetailState>(BookingDetailState.Loading)
    val state: StateFlow<BookingDetailState> = _state

    fun loadBooking(bookingId: String) {
        viewModelScope.launch {
            try {
                _state.value = BookingDetailState.Loading
                val bookings = repository.getTradesmanBookings("2b6fe808-b73b-4d16-915b-298b4d076c47").first()
                val booking = bookings.find { it.id == bookingId }
                if (booking != null) {
                    _state.value = BookingDetailState.Success(booking)
                } else {
                    _state.value = BookingDetailState.Error("Booking not found")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading booking", e)
                _state.value = BookingDetailState.Error("Failed to load booking")
            }
        }
    }

    fun scheduleVisitation(dateTime: String, price: Double) {
        viewModelScope.launch {
            try {
                when (val currentState = _state.value) {
                    is BookingDetailState.Success -> {
                        val booking = currentState.booking
                        repository.scheduleBooking(booking.id, dateTime)
                        repository.submitQuote(booking.id, price)
                        loadBooking(booking.id) // Reload to get updated state
                    }
                    else -> {
                        Log.e(TAG, "Cannot schedule visitation: booking not loaded")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error scheduling visitation", e)
                _state.value = BookingDetailState.Error("Failed to schedule visitation")
            }
        }
    }

    fun submitCounterOffer(amount: Double) {
        viewModelScope.launch {
            try {
                when (val currentState = _state.value) {
                    is BookingDetailState.Success -> {
                        val booking = currentState.booking
                        repository.submitQuote(booking.id, amount)
                        loadBooking(booking.id) // Reload to get updated state
                    }
                    else -> {
                        Log.e(TAG, "Cannot submit counter offer: booking not loaded")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error submitting counter offer", e)
                _state.value = BookingDetailState.Error("Failed to submit counter offer")
            }
        }
    }
}
