package com.tradesmanhandy.app.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubmitQuoteRequest(
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "description")
    val description: String
)
