package com.tradesmanhandy.app.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateBookingStatusRequest(
    @Json(name = "status")
    val status: String,
    @Json(name = "scheduledDate")
    val scheduledDate: String? = null,
    @Json(name = "quotedPrice")
    val quotedPrice: Double? = null
)
