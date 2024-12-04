package com.tradesmanhandy.app.presentation.home

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
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookingRepository: IBookingRepository
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    // TODO: Get from auth
    private val tradesmanId = "85c629b1-d581-4af4-998a-af09e45e9fb9"

    init {
        loadBookings()
    }

    fun loadBookings() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Loading bookings for tradesman: $tradesmanId")
                _uiState.value = HomeUiState.Loading
                
                bookingRepository.getTradesmanBookings(tradesmanId)
                    .catch { error ->
                        Log.e(TAG, "Error loading bookings", error)
                        when (error) {
                            is HttpException -> {
                                val errorBody = error.response()?.errorBody()?.string()
                                Log.e(TAG, "HTTP Error ${error.code()}: $errorBody")
                                _uiState.value = HomeUiState.Error(
                                    "Server error (${error.code()}): ${error.message()}"
                                )
                            }
                            is IOException -> {
                                Log.e(TAG, "Network error: ${error.message}")
                                _uiState.value = HomeUiState.Error(
                                    "Network error: Please check your internet connection"
                                )
                            }
                            else -> {
                                Log.e(TAG, "Unexpected error: ${error.message}")
                                _uiState.value = HomeUiState.Error(
                                    error.message ?: "An unexpected error occurred"
                                )
                            }
                        }
                    }
                    .collect { bookings ->
                        Log.d(TAG, "Successfully loaded ${bookings.size} bookings")
                        _uiState.value = HomeUiState.Success(
                            bookings = bookings,
                            stats = calculateBookingStats(bookings)
                        )
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error in loadBookings", e)
                _uiState.value = HomeUiState.Error(
                    "An unexpected error occurred: ${e.message}"
                )
            }
        }
    }

    private fun calculateBookingStats(bookings: List<Booking>): BookingStats {
        return try {
            BookingStats(
                pending = bookings.count { it.status == BookingStatus.PENDING },
                confirmed = bookings.count { 
                    it.status == BookingStatus.ACCEPTED || 
                    it.status == BookingStatus.IN_PROGRESS 
                },
                completed = bookings.count { it.status == BookingStatus.COMPLETED }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error calculating booking stats", e)
            BookingStats(pending = 0, confirmed = 0, completed = 0)
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val bookings: List<Booking>,
        val stats: BookingStats
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

data class BookingStats(
    val pending: Int,
    val confirmed: Int,
    val completed: Int
)
