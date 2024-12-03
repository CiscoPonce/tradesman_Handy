package com.tradesmanhandy.app.data.api

import com.tradesmanhandy.app.data.model.Booking
import com.tradesmanhandy.app.data.model.User
import retrofit2.http.*

interface TradesmanHandyApi {
    @POST("users")
    suspend fun createUser(@Body user: CreateUserRequest): User

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): User

    @GET("bookings/tradesman/{tradesmanId}")
    suspend fun getTradesmanBookings(@Path("tradesmanId") tradesmanId: String): List<Booking>

    @POST("bookings")
    suspend fun createBooking(@Body booking: CreateBookingRequest): Booking

    @PUT("bookings/{id}/quote")
    suspend fun submitQuote(
        @Path("id") bookingId: String,
        @Body request: SubmitQuoteRequest
    ): Booking

    @PUT("bookings/{id}/accept")
    suspend fun acceptBooking(@Path("id") bookingId: String): Booking

    @PUT("bookings/{id}/reject")
    suspend fun rejectBooking(@Path("id") bookingId: String): Booking

    @PUT("bookings/{id}/schedule")
    suspend fun scheduleBooking(
        @Path("id") bookingId: String,
        @Body request: ScheduleBookingRequest
    ): Booking
}

data class CreateUserRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val isTradesmen: Boolean,
    val phoneNumber: String,
    val address: String?
)

data class CreateBookingRequest(
    val title: String,
    val description: String,
    val source: String,
    val tradesmanId: String,
    val clientId: String,
    val location: String?
)

data class SubmitQuoteRequest(
    val price: Double
)

data class ScheduleBookingRequest(
    val scheduledDate: String
)
