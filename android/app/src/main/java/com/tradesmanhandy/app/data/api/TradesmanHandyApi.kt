package com.tradesmanhandy.app.data.api

import com.tradesmanhandy.app.data.model.Booking
import com.tradesmanhandy.app.data.model.User
import com.tradesmanhandy.app.data.api.models.CreateUserRequest
import com.tradesmanhandy.app.data.api.models.CreateBookingRequest
import com.tradesmanhandy.app.data.api.models.SubmitQuoteRequest
import retrofit2.http.*

interface TradesmanHandyApi {
    @POST("/api/v1/users/")
    suspend fun createUser(@Body user: CreateUserRequest): User

    @GET("/api/v1/users/{id}/")
    suspend fun getUser(@Path("id") id: String): User

    @GET("/api/v1/bookings/tradesman/{tradesmanId}/")
    suspend fun getTradesmanBookings(@Path("tradesmanId") tradesmanId: String): List<Booking>

    @POST("/api/v1/bookings/")
    suspend fun createBooking(@Body booking: CreateBookingRequest): Booking

    @PUT("/api/v1/bookings/{id}/quote/")
    suspend fun submitQuote(
        @Path("id") bookingId: String,
        @Body request: SubmitQuoteRequest
    ): Booking

    @PUT("/api/v1/bookings/{id}/accept/")
    suspend fun acceptBooking(@Path("id") bookingId: String): Booking

    @PUT("/api/v1/bookings/{id}/reject/")
    suspend fun rejectBooking(@Path("id") bookingId: String): Booking

    @PUT("/api/v1/bookings/{id}/complete/")
    suspend fun completeBooking(@Path("id") bookingId: String): Booking
}
