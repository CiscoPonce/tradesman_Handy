package com.tradesmanhandy.app.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateUserRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "phoneNumber")
    val phoneNumber: String,
    @Json(name = "role")
    val role: String,
    @Json(name = "companyName")
    val companyName: String? = null,
    @Json(name = "services")
    val services: List<String>? = null
)
