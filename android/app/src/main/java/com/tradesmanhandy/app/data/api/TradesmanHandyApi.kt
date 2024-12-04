package com.tradesmanhandy.app.data.api

import com.tradesmanhandy.app.data.model.Booking
import com.tradesmanhandy.app.data.model.User
import com.tradesmanhandy.app.data.api.models.CreateUserRequest
import com.tradesmanhandy.app.data.api.models.CreateBookingRequest
import com.tradesmanhandy.app.data.api.models.SubmitQuoteRequest
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
}
