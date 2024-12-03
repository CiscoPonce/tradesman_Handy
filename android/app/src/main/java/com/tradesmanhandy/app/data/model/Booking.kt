package com.tradesmanhandy.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Booking(
    val id: String,
    val title: String,
    val description: String,
    val source: BookingSource,
    val status: BookingStatus,
    @Json(name = "quotedPrice")
    val quotedPrice: Double?,
    @Json(name = "scheduledDate")
    val scheduledDate: String?,
    @Json(name = "clientId")
    val clientId: String,
    @Json(name = "tradesmanId")
    val tradesmanId: String,
    val location: String?,
    @Json(name = "housingAssociationRef")
    val housingAssociationRef: String?,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "updatedAt")
    val updatedAt: String,
    val client: User?
)

enum class BookingSource {
    @Json(name = "local")
    LOCAL,
    @Json(name = "housing_association")
    HOUSING_ASSOCIATION
}

enum class BookingStatus {
    @Json(name = "pending")
    PENDING,
    @Json(name = "quoted")
    QUOTED,
    @Json(name = "accepted")
    ACCEPTED,
    @Json(name = "rejected")
    REJECTED,
    @Json(name = "in_progress")
    IN_PROGRESS,
    @Json(name = "completed")
    COMPLETED,
    @Json(name = "cancelled")
    CANCELLED
}
