package com.tradesmanhandy.app.presentation.bookings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tradesmanhandy.app.data.model.Booking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    selectedTab: Int = 0,
    onNavigateToBookingDetail: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: BookingsViewModel = hiltViewModel()
) {
    var currentTab by remember { mutableStateOf(selectedTab) }
    
    val pendingBookings by viewModel.pendingBookings.collectAsState()
    val acceptedBookings by viewModel.acceptedBookings.collectAsState()
    val completedBookings by viewModel.completedBookings.collectAsState()
    
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Bookings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = currentTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    text = { Text("Pending") }
                )
                Tab(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    text = { Text("Accepted") }
                )
                Tab(
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 },
                    text = { Text("Completed") }
                )
            }
            
            when (currentTab) {
                0 -> BookingsList(
                    bookings = pendingBookings,
                    onBookingClick = onNavigateToBookingDetail,
                    onManageClick = { bookingId -> onNavigateToBookingDetail(bookingId) },
                    onConfirmClick = { bookingId -> viewModel.confirmBooking(bookingId) },
                    onRejectClick = { bookingId -> viewModel.rejectBooking(bookingId) }
                )
                1 -> BookingsList(
                    bookings = acceptedBookings,
                    onBookingClick = onNavigateToBookingDetail,
                    onManageClick = { bookingId -> onNavigateToBookingDetail(bookingId) },
                    onConfirmClick = { },
                    onRejectClick = { }
                )
                2 -> BookingsList(
                    bookings = completedBookings,
                    onBookingClick = onNavigateToBookingDetail,
                    onManageClick = { bookingId -> onNavigateToBookingDetail(bookingId) },
                    onConfirmClick = { },
                    onRejectClick = { }
                )
            }
        }
    }
}

@Composable
private fun BookingsList(
    bookings: List<Booking>,
    onBookingClick: (String) -> Unit,
    onManageClick: (String) -> Unit,
    onConfirmClick: (String) -> Unit,
    onRejectClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(bookings) { booking ->
            BookingCard(
                booking = booking,
                onClick = { onBookingClick(booking.id) },
                onManageClick = { onManageClick(booking.id) },
                onConfirmClick = { onConfirmClick(booking.id) },
                onRejectClick = { onRejectClick(booking.id) }
            )
        }
    }
}

@Composable
fun BookingCard(
    booking: Booking,
    onClick: () -> Unit,
    onManageClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = booking.scheduledDate?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Time not set",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = "£${booking.quotedPrice ?: "TBD"}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = booking.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onManageClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Manage")
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onRejectClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Reject")
                    }
                    
                    Button(
                        onClick = onConfirmClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}
