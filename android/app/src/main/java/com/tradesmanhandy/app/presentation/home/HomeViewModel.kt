package com.tradesmanhandy.app.presentation.home

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

private const val TAG = "HomeViewModel"

data class BookingStats(
    val pending: Int = 0,
    val confirmed: Int = 0,
    val completed: Int = 0
)

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val stats: BookingStats) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadBookings()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            try {
                val response = repository.getTradesmanBookings("2b6fe808-b73b-4d16-915b-298b4d076c47").first()
                val stats = calculateStats(response)
                _uiState.value = HomeUiState.Success(stats)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading bookings", e)
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun calculateStats(bookings: List<Booking>): BookingStats {
        var pending = 0
        var confirmed = 0
        var completed = 0

        bookings.forEach { booking ->
            when (booking.status) {
                BookingStatus.PENDING -> pending++
                BookingStatus.ACCEPTED -> confirmed++
                BookingStatus.COMPLETED -> completed++
                else -> {} // Ignore other statuses
            }
        }

        return BookingStats(
            pending = pending,
            confirmed = confirmed,
            completed = completed
        )
    }
}
