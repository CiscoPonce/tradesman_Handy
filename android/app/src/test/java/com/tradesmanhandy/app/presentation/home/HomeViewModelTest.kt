package com.tradesmanhandy.app.presentation.home

import com.tradesmanhandy.app.data.model.Booking
import com.tradesmanhandy.app.data.model.BookingSource
import com.tradesmanhandy.app.data.model.BookingStatus
import com.tradesmanhandy.app.data.repository.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private lateinit var repository: BookingRepository
    private lateinit var viewModel: HomeViewModel
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setup() {
        repository = mock()
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadData updates state with correct booking stats`() = runTest {
        // Given
        val bookings = listOf(
            Booking(
                id = "1",
                title = "Test Booking 1",
                description = "Test Description 1",
                location = "Test Location 1",
                quotedPrice = 100.0,
                status = BookingStatus.PENDING,
                scheduledDate = "2024-12-19T10:00:00",
                source = BookingSource.LOCAL,
                clientId = "clientId",
                tradesmanId = "tradesmanId",
                housingAssociationRef = "ref",
                createdAt = "2024-12-19T10:00:00",
                updatedAt = "2024-12-19T10:00:00",
                client = null
            ),
            Booking(
                id = "2",
                title = "Test Booking 2",
                description = "Test Description 2",
                location = "Test Location 2",
                quotedPrice = 200.0,
                status = BookingStatus.ACCEPTED,
                scheduledDate = "2024-12-20T10:00:00",
                source = BookingSource.LOCAL,
                clientId = "clientId",
                tradesmanId = "tradesmanId",
                housingAssociationRef = "ref",
                createdAt = "2024-12-19T10:00:00",
                updatedAt = "2024-12-19T10:00:00",
                client = null
            )
        )

        whenever(repository.getTradesmanBookings(any())).thenReturn(flowOf(bookings))

        // When
        viewModel.loadData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals(1, state.stats.pending)
        assertEquals(1, state.stats.confirmed)
        assertEquals(0, state.stats.completed)
    }
}
