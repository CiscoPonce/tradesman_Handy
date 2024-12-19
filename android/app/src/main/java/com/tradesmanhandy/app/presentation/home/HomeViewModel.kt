package com.tradesmanhandy.app.presentation.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
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
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class BookingStats(
    val pending: Int = 0,
    val confirmed: Int = 0,
    val completed: Int = 0
)

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val userName: String = "",
        val profileImageUrl: String? = null,
        val stats: BookingStats = BookingStats(),
        val bookingDates: Set<LocalDate> = emptySet(),
        val currentMonth: YearMonth? = null,
        val startMonth: YearMonth? = null,
        val endMonth: YearMonth? = null,
        val daysOfWeek: List<String> = emptyList()
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
        initializeCalendar()
    }

    public fun loadData() {
        viewModelScope.launch {
            try {
                val bookings = repository.getTradesmanBookings("2b6fe808-b73b-4d16-915b-298b4d076c47").first()
                
                // Calculate booking dates
                val bookingDates = bookings.mapNotNull { booking ->
                    booking.scheduledDate?.let { dateStr ->
                        try {
                            // Assuming the date string is in ISO format (e.g., "2024-12-08")
                            LocalDate.parse(dateStr.split("T")[0])
                        } catch (e: Exception) {
                            null
                        }
                    }
                }.toSet()

                // Calculate stats
                val stats = BookingStats(
                    pending = bookings.count { it.status == BookingStatus.PENDING },
                    confirmed = bookings.count { it.status == BookingStatus.ACCEPTED || it.status == BookingStatus.CONFIRMED },
                    completed = bookings.count { it.status == BookingStatus.COMPLETED }
                )

                _uiState.value = when (val currentState = _uiState.value) {
                    is HomeUiState.Success -> currentState.copy(
                        stats = stats,
                        bookingDates = bookingDates
                    )
                    else -> HomeUiState.Success(
                        userName = "John Smith", // TODO: Get from user repository
                        profileImageUrl = null,
                        stats = stats,
                        bookingDates = bookingDates
                    )
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Failed to load bookings: ${e.message}")
            }
        }
    }

    private fun initializeCalendar() {
        val today = LocalDate.of(2024, 12, 9) // Using the provided current time
        val currentMonth = YearMonth.from(today)
        
        _uiState.value = (_uiState.value as? HomeUiState.Success)?.copy(
            currentMonth = currentMonth,
            startMonth = YearMonth.from(today.minusWeeks(1)),
            endMonth = YearMonth.from(today.plusWeeks(2)),
            daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        ) ?: HomeUiState.Success(
            userName = "John Smith", 
            profileImageUrl = null,
            stats = BookingStats(),
            bookingDates = emptySet(),
            currentMonth = currentMonth,
            startMonth = YearMonth.from(today.minusWeeks(1)),
            endMonth = YearMonth.from(today.plusWeeks(2)),
            daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        )
    }

    fun onNotificationClick() {
        // TODO: Implement notification handling
    }

    fun onMessageClick() {
        // TODO: Implement message handling
    }

    fun onDateSelected(date: LocalDate) {
        // TODO: Implement date selection handling
        // This could navigate to a day detail screen or show a bottom sheet with bookings for that day
    }
}
