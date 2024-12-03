package com.tradesmanhandy.app.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateFormatter {
    private val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
    private val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")

    fun formatDateTime(isoDateTime: String): String {
        return try {
            val dateTime = LocalDateTime.parse(isoDateTime, inputFormatter)
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            isoDateTime // Return original string if parsing fails
        }
    }
}
