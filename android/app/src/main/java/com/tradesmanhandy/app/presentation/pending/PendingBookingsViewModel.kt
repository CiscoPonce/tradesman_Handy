package com.tradesmanhandy.app.presentation.pending

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradesmanhandy.app.domain.model.Booking
import com.tradesmanhandy.app.domain.model.BookingStatus
import com.tradesmanhandy.app.data.repository.BookingRepository
import com.tradesmanhandy.app.data.mapper.toDomainList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val TAG = "PendingBookingsViewModel"
private const val TRADESMAN_ID = "2b6fe808-b73b-4d16-915b-298b4d076c47" // TODO: Get from auth
private const val MAX_RETRIES = 3
private const val RETRY_DELAY_MS = 1000L

@HiltViewModel
class PendingBookingsViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {

    private val _state = MutableStateFlow<BookingsState>(BookingsState.Loading)
    val state: StateFlow<BookingsState> = _state.asStateFlow()

    init {
        loadBookings()
    }

    private fun loadBookings(retryCount: Int = 0) {
        viewModelScope.launch {
            try {
                _state.value = BookingsState.Loading
                repository.getTradesmanBookings(TRADESMAN_ID)
                    .map { bookings -> bookings.toDomainList() }
                    .catch { e ->
                        Log.e(TAG, "Error collecting bookings", e)
                        handleError(e as Exception, retryCount)
                    }
                    .collect { bookings ->
                        val pendingBookings = bookings.filter { it.status == BookingStatus.PENDING }
                        val confirmedBookings = bookings.filter { it.status == BookingStatus.CONFIRMED }
                        val completedBookings = bookings.filter { it.status == BookingStatus.COMPLETED }
                        
                        _state.value = BookingsState.Success(
                            pendingBookings = pendingBookings,
                            confirmedBookings = confirmedBookings,
                            completedBookings = completedBookings
                        )
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error loading bookings", e)
                _state.value = BookingsState.Error("An unexpected error occurred")
            }
        }
    }

    private suspend fun handleError(e: Exception, retryCount: Int) {
        if (retryCount < MAX_RETRIES) {
            delay(RETRY_DELAY_MS * (retryCount + 1))
            loadBookings(retryCount + 1)
        } else {
            _state.value = BookingsState.Error(
                when (e) {
                    is HttpException -> "Network request failed"
                    is IOException -> "Couldn't reach server"
                    else -> "An unexpected error occurred"
                }
            )
        }
    }

    fun rejectBooking(bookingId: String) {
        viewModelScope.launch {
            try {
                repository.rejectBooking(bookingId)
                loadBookings()
            } catch (e: Exception) {
                Log.e(TAG, "Error rejecting booking", e)
            }
        }
    }

    fun confirmBooking(bookingId: String) {
        viewModelScope.launch {
            try {
                repository.acceptBooking(bookingId)
                loadBookings()
            } catch (e: Exception) {
                Log.e(TAG, "Error confirming booking", e)
            }
        }
    }

    fun submitQuote(bookingId: String, price: Double) {
        viewModelScope.launch {
            try {
                repository.submitQuote(bookingId, price)
                loadBookings()
            } catch (e: Exception) {
                Log.e(TAG, "Error submitting quote", e)
            }
        }
    }

    fun scheduleBooking(bookingId: String, scheduledDate: LocalDateTime) {
        viewModelScope.launch {
            try {
                val formattedDate = scheduledDate.format(DateTimeFormatter.ISO_DATE_TIME)
                repository.scheduleBooking(bookingId, formattedDate)
                loadBookings()
            } catch (e: Exception) {
                Log.e(TAG, "Error scheduling booking", e)
            }
        }
    }
}
