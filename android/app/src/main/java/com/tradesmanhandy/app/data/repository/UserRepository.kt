package com.tradesmanhandy.app.data.repository

import com.tradesmanhandy.app.data.api.TradesmanHandyApi
import com.tradesmanhandy.app.data.api.models.CreateUserRequest
import com.tradesmanhandy.app.data.model.User
import javax.inject.Inject
import android.util.Log

class UserRepository @Inject constructor(
    private val api: TradesmanHandyApi
) {
    suspend fun createUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        role: String = "client",
        companyName: String? = null,
        services: List<String>? = null
    ): User {
        Log.d("UserRepository", "Creating user with role: $role")
        val request = CreateUserRequest(
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            role = role,
            companyName = companyName,
            services = services,
            isTradesmen = role == "tradesman"
        )
        Log.d("UserRepository", "Request body: $request")
        return api.createUser(request)
    }

    suspend fun getUser(id: String): User {
        return api.getUser(id)
    }
}
