package com.tradesmanhandy.app.presentation.pending

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradesmanhandy.app.data.model.Booking
import com.tradesmanhandy.app.data.model.BookingStatus
import com.tradesmanhandy.app.domain.repository.IBookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PendingBookingsViewModel"

@HiltViewModel
class PendingBookingsViewModel @Inject constructor(
    private val bookingRepository: IBookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PendingBookingsUiState>(PendingBookingsUiState.Loading)
    val uiState: StateFlow<PendingBookingsUiState> = _uiState

    init {
        loadPendingBookings()
    }

    fun loadPendingBookings() {
        viewModelScope.launch {
            _uiState.value = PendingBookingsUiState.Loading
            bookingRepository.getTradesmanBookings("2b6fe808-b73b-4d16-915b-298b4d076c47")
                .map { bookings -> 
                    bookings.filter { it.status == BookingStatus.PENDING }
                }
                .catch { e ->
                    Log.e(TAG, "Error loading pending bookings", e)
                    _uiState.value = PendingBookingsUiState.Error(
                        e.message ?: "Failed to load pending bookings"
                    )
                }
                .collect { pendingBookings ->
                    Log.d(TAG, "Loaded ${pendingBookings.size} pending bookings")
                    _uiState.value = PendingBookingsUiState.Success(pendingBookings)
                }
        }
    }
}

sealed class PendingBookingsUiState {
    object Loading : PendingBookingsUiState()
    data class Error(val message: String) : PendingBookingsUiState()
    data class Success(val bookings: List<Booking>) : PendingBookingsUiState()
}
