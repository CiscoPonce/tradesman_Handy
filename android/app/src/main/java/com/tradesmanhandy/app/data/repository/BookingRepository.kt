package com.tradesmanhandy.app.data.repository

import com.tradesmanhandy.app.data.api.CreateBookingRequest
import com.tradesmanhandy.app.data.api.ScheduleBookingRequest
import com.tradesmanhandy.app.data.api.SubmitQuoteRequest
import com.tradesmanhandy.app.data.api.TradesmanHandyApi
import com.tradesmanhandy.app.data.model.Booking
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
            // TODO: Handle error properly
            throw e
        }
    }

    override suspend fun createBooking(
        title: String,
        description: String,
        source: String,
        tradesmanId: String,
        clientId: String,
        location: String?
    ): Booking {
        return api.createBooking(
            CreateBookingRequest(
                title = title,
                description = description,
                source = source,
                tradesmanId = tradesmanId,
                clientId = clientId,
                location = location
            )
        )
    }

    override suspend fun submitQuote(bookingId: String, price: Double): Booking {
        return api.submitQuote(bookingId, SubmitQuoteRequest(price))
    }

    override suspend fun acceptBooking(bookingId: String): Booking {
        return api.acceptBooking(bookingId)
    }

    override suspend fun rejectBooking(bookingId: String): Booking {
        return api.rejectBooking(bookingId)
    }

    override suspend fun scheduleBooking(bookingId: String, scheduledDate: String): Booking {
        return api.scheduleBooking(bookingId, ScheduleBookingRequest(scheduledDate))
    }
}
