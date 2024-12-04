package com.tradesmanhandy.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val email: String,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "role")
    val role: UserRole?,
    @Json(name = "phoneNumber")
    val phoneNumber: String,
    @Json(name = "companyName")
    val companyName: String?,
    @Json(name = "services")
    val services: List<String>?,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "updatedAt")
    val updatedAt: String
)

enum class UserRole {
    @Json(name = "client")
    CLIENT,
    @Json(name = "tradesman")
    TRADESMAN,
    @Json(name = "admin")
    ADMIN
}
