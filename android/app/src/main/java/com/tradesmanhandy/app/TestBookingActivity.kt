package com.tradesmanhandy.app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.tradesmanhandy.app.data.repository.BookingRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class TestBookingActivity : ComponentActivity() {
    
    @Inject
    lateinit var bookingRepository: BookingRepository

    private val tradesmanId = "2b6fe808-b73b-4d16-915b-298b4d076c47"
    private val clientId = "65783d021ed91c40607dd001"
    private val currentTime = LocalDateTime.parse("2024-12-12T12:27:19")
    private val dateFormatter = DateTimeFormatter.ISO_DATE_TIME

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

    private val prices = listOf(
        150.0,  // Plumbing
        250.0,  // Electrical
        3500.0, // Bathroom
        5000.0, // Kitchen
        800.0,  // Roof
        1200.0, // Windows
        600.0,  // Door
        400.0,  // HVAC
        1800.0, // Painting
        2500.0  // Flooring
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        lifecycleScope.launch {
            try {
                // Create 5 pending bookings for next week
                repeat(5) { i ->
                    val preferredDate = currentTime.plusDays(i.toLong() + 7) // Starting next week
                    val booking = bookingRepository.createBooking(
                        title = titles[i],
                        description = descriptions[i],
                        source = "app",
                        tradesmanId = tradesmanId,
                        clientId = clientId,
                        location = locations[i % locations.size],
                        serviceType = "general",
                        preferredDate = preferredDate.format(dateFormatter)
                    )
                    // Add quoted price
                    bookingRepository.submitQuote(booking.id, prices[i])
                    Log.d("TestBookingActivity", "Created pending booking: ${booking.id}")
                    Toast.makeText(this@TestBookingActivity, "Created pending booking: ${booking.id}", Toast.LENGTH_SHORT).show()
                }

                // Create 3 confirmed bookings for this week
                repeat(3) { i ->
                    val preferredDate = currentTime.plusDays(i.toLong() + 2) // Later this week
                    val booking = bookingRepository.createBooking(
                        title = titles[i + 5],
                        description = descriptions[i + 5],
                        source = "app",
                        tradesmanId = tradesmanId,
                        clientId = clientId,
                        location = locations[(i + 2) % locations.size],
                        serviceType = "general",
                        preferredDate = preferredDate.format(dateFormatter)
                    )
                    
                    // Add quoted price
                    bookingRepository.submitQuote(booking.id, prices[i + 5])
                    
                    // Accept and schedule the booking
                    val acceptedBooking = bookingRepository.acceptBooking(booking.id)
                    Log.d("TestBookingActivity", "Accepted booking: ${acceptedBooking.id}")
                    
                    val scheduledBooking = bookingRepository.scheduleBooking(
                        booking.id,
                        preferredDate.format(dateFormatter)
                    )
                    Log.d("TestBookingActivity", "Scheduled booking: ${scheduledBooking.id}")
                    Toast.makeText(this@TestBookingActivity, "Created confirmed booking: ${booking.id}", Toast.LENGTH_SHORT).show()
                }

                // Create 2 completed bookings from last week
                repeat(2) { i ->
                    val preferredDate = currentTime.minusDays((i + 7).toLong()) // Last week
                    val booking = bookingRepository.createBooking(
                        title = titles[i + 8],
                        description = descriptions[i + 8],
                        source = "app",
                        tradesmanId = tradesmanId,
                        clientId = clientId,
                        location = locations[(i + 3) % locations.size],
                        serviceType = "general",
                        preferredDate = preferredDate.format(dateFormatter)
                    )
                    
                    // Add quoted price
                    bookingRepository.submitQuote(booking.id, prices[i + 8])
                    
                    // Accept, schedule, and complete the booking
                    val acceptedBooking = bookingRepository.acceptBooking(booking.id)
                    Log.d("TestBookingActivity", "Accepted booking: ${acceptedBooking.id}")
                    
                    val scheduledBooking = bookingRepository.scheduleBooking(
                        booking.id,
                        preferredDate.format(dateFormatter)
                    )
                    Log.d("TestBookingActivity", "Scheduled booking: ${scheduledBooking.id}")
                    
                    val completedBooking = bookingRepository.updateBookingStatus(booking.id, com.tradesmanhandy.app.data.model.BookingStatus.COMPLETED)
                    Log.d("TestBookingActivity", "Completed booking: ${completedBooking.id}")
                    Toast.makeText(this@TestBookingActivity, "Created completed booking: ${booking.id}", Toast.LENGTH_SHORT).show()
                }

                Toast.makeText(this@TestBookingActivity, "Successfully created all test bookings!", Toast.LENGTH_LONG).show()
                Log.d("TestBookingActivity", "Successfully created all test bookings")
            } catch (e: Exception) {
                Log.e("TestBookingActivity", "Error creating test bookings", e)
                Toast.makeText(this@TestBookingActivity, "Error creating bookings: ${e.message}", Toast.LENGTH_LONG).show()
            }
            
            // Close the activity
            finish()
        }
    }
}
