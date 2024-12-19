package com.tradesmanhandy.app.data.repository

import com.tradesmanhandy.app.data.api.TradesmanHandyApi
import com.tradesmanhandy.app.data.api.models.CreateBookingRequest
import com.tradesmanhandy.app.data.api.models.SubmitQuoteRequest
import com.tradesmanhandy.app.data.api.models.UpdateBookingStatusRequest
import com.tradesmanhandy.app.data.model.Booking
import com.tradesmanhandy.app.data.model.BookingStatus
import com.tradesmanhandy.app.domain.repository.IBookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val api: TradesmanHandyApi
) : IBookingRepository {
    override fun getTradesmanBookings(tradesmanId: String): Flow<List<Booking>> = flow {
        try {
            val bookings = api.getTradesmanBookings(tradesmanId)
            emit(bookings)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createBooking(
        title: String,
        description: String,
        source: String,
        tradesmanId: String,
        clientId: String,
        location: String,
        serviceType: String,
        preferredDate: String
    ): Booking {
        return try {
            val request = CreateBookingRequest(
                title = title,
                description = description,
                source = source,
                tradesmanId = tradesmanId,
                clientId = clientId,
                location = location,
                serviceType = serviceType,
                preferredDate = preferredDate
            )
            api.createBooking(request)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun submitQuote(
        bookingId: String,
        amount: Double
    ): Booking {
        return try {
            val request = SubmitQuoteRequest(
                amount = amount,
                description = "Quote for booking $bookingId"
            )
            api.submitQuote(bookingId, request)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun acceptBooking(bookingId: String): Booking {
        return try {
            api.acceptBooking(bookingId)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun rejectBooking(bookingId: String): Booking {
        return try {
            api.rejectBooking(bookingId)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun scheduleBooking(bookingId: String, scheduledDate: String): Booking {
        return try {
            val request = UpdateBookingStatusRequest(
                status = BookingStatus.ACCEPTED.name.lowercase(),
                scheduledDate = scheduledDate
            )
            api.scheduleBooking(bookingId, request)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): Booking {
        return try {
            api.updateBookingStatus(bookingId, UpdateBookingStatusRequest(status = status.name.lowercase()))
        } catch (e: Exception) {
            throw e
        }
    }
}
