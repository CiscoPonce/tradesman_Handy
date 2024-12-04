package com.tradesmanhandy.app.presentation.pending

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradesmanhandy.app.data.model.Booking
import com.tradesmanhandy.app.data.model.BookingStatus
import com.tradesmanhandy.app.data.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PendingBookingsViewModel"

sealed class PendingBookingsState {
    object Loading : PendingBookingsState()
    data class Success(val bookings: List<Booking>) : PendingBookingsState()
    data class Error(val message: String) : PendingBookingsState()
}

@HiltViewModel
class PendingBookingsViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {

    private val _state = MutableStateFlow<PendingBookingsState>(PendingBookingsState.Loading)
    val state: StateFlow<PendingBookingsState> = _state.asStateFlow()

    init {
        loadPendingBookings()
    }

    private fun loadPendingBookings() {
        viewModelScope.launch {
            try {
                val response = repository.getTradesmanBookings("2b6fe808-b73b-4d16-915b-298b4d076c47").first()
                val pendingBookings = response.filter { booking -> booking.status == BookingStatus.PENDING }
                _state.value = PendingBookingsState.Success(pendingBookings)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading pending bookings", e)
                _state.value = PendingBookingsState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
