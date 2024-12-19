package com.tradesmanhandy.app.presentation.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tradesmanhandy.app.data.model.Booking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    bookingId: String,
    navController: NavController,
    viewModel: BookingDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showVisitationDialog by remember { mutableStateOf(false) }
    var showCounterOfferDialog by remember { mutableStateOf(false) }

    LaunchedEffect(bookingId) {
        viewModel.loadBooking(bookingId)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Booking Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (val currentState = state) {
            is BookingDetailState.Success -> {
                BookingDetailContent(
                    booking = currentState.booking,
                    modifier = Modifier.padding(padding),
                    onAddVisitation = { showVisitationDialog = true },
                    onMakeCounterOffer = { showCounterOfferDialog = true }
                )
            }
            is BookingDetailState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is BookingDetailState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(currentState.message)
                }
            }
        }

        if (showVisitationDialog) {
            AddVisitationDialog(
                onDismiss = { showVisitationDialog = false },
                onConfirm = { dateTime, price ->
                    viewModel.scheduleVisitation(dateTime, price)
                    showVisitationDialog = false
                }
            )
        }

        if (showCounterOfferDialog) {
            CounterOfferDialog(
                onDismiss = { showCounterOfferDialog = false },
                onConfirm = { amount ->
                    viewModel.submitCounterOffer(amount)
                    showCounterOfferDialog = false
                }
            )
        }
    }
}

@Composable
private fun BookingDetailContent(
    booking: Booking,
    modifier: Modifier = Modifier,
    onAddVisitation: () -> Unit,
    onMakeCounterOffer: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Title and Price Section
        Text(
            text = booking.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (booking.quotedPrice != null) "£${String.format("%.2f", booking.quotedPrice)}" else "Price TBD",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        // Date and Location Section
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = booking.scheduledDate?.let {
                    try {
                        val dateTime = LocalDateTime.parse(it)
                        dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))
                    } catch (e: Exception) {
                        it
                    }
                } ?: "Not scheduled",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = booking.location,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Description Section
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = booking.description,
            style = MaterialTheme.typography.bodyLarge
        )

        // Visitations Section
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Visitations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = onAddVisitation,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Text("+ Add")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Example visitation item
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "12:00",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "30 July",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = "£40",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = { /* Delete visitation */ }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        // Counter Offer Section
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Counter Offer",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Date") },
                placeholder = { Text("dd/mm/yyyy") },
                singleLine = true,
                enabled = false // We'll implement date picker later
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Cancel */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = onMakeCounterOffer,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Text("Submit")
                }
            }
        }
    }
}

@Composable
private fun AddVisitationDialog(
    onDismiss: () -> Unit,
    onConfirm: (dateTime: String, price: Double) -> Unit
) {
    var selectedTime by remember { mutableStateOf("12:00") }
    var selectedDate by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("40") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Visitation") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Time Field
                OutlinedTextField(
                    value = selectedTime,
                    onValueChange = { selectedTime = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Time") },
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Date Field
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { selectedDate = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Date") },
                    placeholder = { Text("30 July") },
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Price Field
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Price (£)") },
                    singleLine = true,
                    prefix = { Text("£") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Format date and time to ISO string
                    val dateTime = try {
                        val formatter = DateTimeFormatter.ofPattern("dd MMMM HH:mm")
                        val parsed = LocalDateTime.parse("$selectedDate $selectedTime", formatter)
                        parsed.format(DateTimeFormatter.ISO_DATE_TIME)
                    } catch (e: Exception) {
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
                    }
                    onConfirm(dateTime, price.toDoubleOrNull() ?: 40.0)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun CounterOfferDialog(
    onDismiss: () -> Unit,
    onConfirm: (amount: Double) -> Unit
) {
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Make Counter Offer") },
        text = {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount (£)") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    amount.toDoubleOrNull()?.let { onConfirm(it) }
                }
            ) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
