package com.tradesmanhandy.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradesmanhandy.app.data.model.Booking
import com.tradesmanhandy.app.data.model.BookingStatus
import com.tradesmanhandy.app.domain.repository.IBookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookingRepository: IBookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    // TODO: Get from auth
    private val tradesmanId = "85c629b1-d581-4af4-998a-af09e45e9fb9"

    init {
        loadBookings()
    }

    fun loadBookings() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            bookingRepository.getTradesmanBookings(tradesmanId)
                .catch { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Unknown error occurred")
                }
                .collect { bookings ->
                    _uiState.value = HomeUiState.Success(
                        bookings = bookings,
                        stats = calculateBookingStats(bookings)
                    )
                }
        }
    }

    private fun calculateBookingStats(bookings: List<Booking>): BookingStats {
        return BookingStats(
            pending = bookings.count { it.status == BookingStatus.PENDING },
            confirmed = bookings.count { 
                it.status == BookingStatus.ACCEPTED || 
                it.status == BookingStatus.IN_PROGRESS 
            },
            completed = bookings.count { it.status == BookingStatus.COMPLETED }
        )
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Error(val message: String) : HomeUiState
    data class Success(
        val bookings: List<Booking>,
        val stats: BookingStats
    ) : HomeUiState
}

data class BookingStats(
    val pending: Int,
    val confirmed: Int,
    val completed: Int
)
