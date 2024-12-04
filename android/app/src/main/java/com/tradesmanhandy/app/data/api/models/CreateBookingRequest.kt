package com.tradesmanhandy.app.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateBookingRequest(
    @Json(name = "title")
    val title: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "source")
    val source: String,
    @Json(name = "tradesmanId")
    val tradesmanId: String,
    @Json(name = "clientId")
    val clientId: String,
    @Json(name = "location")
    val location: String,
    @Json(name = "serviceType")
    val serviceType: String,
    @Json(name = "preferredDate")
    val preferredDate: String
)
