package com.tradesmanhandy.app.domain.model

data class Booking(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val quotedPrice: Double? = null,
    val scheduledDate: String? = null,
    val status: BookingStatus = BookingStatus.PENDING
)

enum class BookingStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    REJECTED
}
