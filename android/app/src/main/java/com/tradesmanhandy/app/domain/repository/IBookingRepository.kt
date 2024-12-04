package com.tradesmanhandy.app.domain.repository

import com.tradesmanhandy.app.data.model.Booking
import kotlinx.coroutines.flow.Flow

interface IBookingRepository {
    fun getTradesmanBookings(tradesmanId: String): Flow<List<Booking>>

    suspend fun createBooking(
        title: String,
        description: String,
        source: String,
        tradesmanId: String,
        clientId: String,
        location: String,
        serviceType: String = source,
        preferredDate: String = ""
    ): Booking

    suspend fun submitQuote(bookingId: String, amount: Double): Booking

    suspend fun acceptBooking(bookingId: String): Booking

    suspend fun rejectBooking(bookingId: String): Booking

    suspend fun scheduleBooking(bookingId: String, scheduledDate: String): Booking
}
