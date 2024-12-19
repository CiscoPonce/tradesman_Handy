package com.tradesmanhandy.app

import android.util.Log
import com.tradesmanhandy.app.data.model.BookingStatus
import com.tradesmanhandy.app.data.repository.BookingRepository
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TestBookingCreator @Inject constructor(
    private val repository: BookingRepository
) {
    private val tradesmanId = "2b6fe808-b73b-4d16-915b-298b4d076c47" // Your tradesman ID
    private val clientId = "65783d021ed91c40607dd001" // Test client ID
    private val locations = listOf(
        "123 Main St, London",
        "456 High St, Manchester",
        "789 Park Rd, Birmingham",
        "321 Queen St, Edinburgh",
        "654 King St, Glasgow"
    )
    private val titles = listOf(
        "Plumbing Repair",
        "Electrical Installation",
        "Bathroom Renovation",
        "Kitchen Remodeling",
        "Roof Repair",
        "Window Replacement",
        "Door Installation",
        "HVAC Maintenance",
        "Painting Job",
        "Flooring Installation"
    )
    private val descriptions = listOf(
        "Fix leaking pipe in the kitchen",
        "Install new electrical outlets in living room",
        "Complete bathroom renovation including new fixtures",
        "Kitchen remodeling with new cabinets",
        "Repair damaged roof tiles",
        "Replace old windows with double-glazed units",
        "Install new front door with security features",
        "Annual HVAC system maintenance",
        "Paint all interior walls",
        "Install hardwood flooring in living room"
    )
    
    private val currentTime = LocalDateTime.parse("2024-12-12T11:53:47")
    private val dateFormatter = DateTimeFormatter.ISO_DATE_TIME

    fun createTestBookings() = runBlocking {
        try {
            // Create 5 pending bookings
            repeat(5) { i ->
                val preferredDate = currentTime.plusDays(i.toLong() + 1)
                val booking = repository.createBooking(
                    title = titles[i],
                    description = descriptions[i],
                    source = "app",
                    tradesmanId = tradesmanId,
                    clientId = clientId,
                    location = locations[i % locations.size],
                    serviceType = "general",
                    preferredDate = preferredDate.format(dateFormatter)
                )
                Log.d("TestBookingCreator", "Created pending booking: ${booking.id}")
            }

            // Create 3 confirmed bookings
            repeat(3) { i ->
                val preferredDate = currentTime.plusDays((i + 7).toLong())
                val booking = repository.createBooking(
                    title = titles[i + 5],
                    description = descriptions[i + 5],
                    source = "app",
                    tradesmanId = tradesmanId,
                    clientId = clientId,
                    location = locations[(i + 2) % locations.size],
                    serviceType = "general",
                    preferredDate = preferredDate.format(dateFormatter)
                )
                
                // Accept and schedule the booking
                val acceptedBooking = repository.acceptBooking(booking.id)
                Log.d("TestBookingCreator", "Accepted booking: ${acceptedBooking.id}")
                
                val scheduledBooking = repository.scheduleBooking(
                    booking.id,
                    preferredDate.format(dateFormatter)
                )
                Log.d("TestBookingCreator", "Scheduled booking: ${scheduledBooking.id}")
            }

            // Create 2 completed bookings
            repeat(2) { i ->
                val preferredDate = currentTime.minusDays((i + 1).toLong())
                val booking = repository.createBooking(
                    title = titles[i + 8],
                    description = descriptions[i + 8],
                    source = "app",
                    tradesmanId = tradesmanId,
                    clientId = clientId,
                    location = locations[(i + 3) % locations.size],
                    serviceType = "general",
                    preferredDate = preferredDate.format(dateFormatter)
                )
                
                // Accept, schedule, and complete the booking
                val acceptedBooking = repository.acceptBooking(booking.id)
                Log.d("TestBookingCreator", "Accepted booking: ${acceptedBooking.id}")
                
                val scheduledBooking = repository.scheduleBooking(
                    booking.id,
                    preferredDate.format(dateFormatter)
                )
                Log.d("TestBookingCreator", "Scheduled booking: ${scheduledBooking.id}")
                
                val completedBooking = repository.updateBookingStatus(booking.id, BookingStatus.COMPLETED)
                Log.d("TestBookingCreator", "Completed booking: ${completedBooking.id}")
            }

            Log.d("TestBookingCreator", "Successfully created all test bookings")
        } catch (e: Exception) {
            Log.e("TestBookingCreator", "Error creating test bookings", e)
            throw e
        }
    }
}
