package com.tradesmanhandy.app.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import com.tradesmanhandy.app.R
import com.tradesmanhandy.app.navigation.Screen
import com.tradesmanhandy.app.presentation.theme.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.height(0.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            when (uiState) {
                is HomeUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is HomeUiState.Success -> {
                    val state = uiState as HomeUiState.Success
                    
                    TopBar(
                        userName = state.userName,
                        profileImageUrl = state.profileImageUrl,
                        onNotificationClick = viewModel::onNotificationClick,
                        onMessageClick = viewModel::onMessageClick
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    CalendarSection(
                        bookingDates = state.bookingDates,
                        currentMonth = currentMonth,
                        startMonth = startMonth,
                        endMonth = endMonth,
                        daysOfWeek = daysOfWeek,
                        onDateSelected = viewModel::onDateSelected
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    StatsSection(
                        title = "Bookings",
                        stats = listOf(
                            StatItem(state.stats.pending, "Pending"),
                            StatItem(state.stats.confirmed, "Confirmed"),
                            StatItem(state.stats.completed, "Completed")
                        ),
                        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                        onStatClick = { index ->
                            when (index) {
                                0 -> navController.navigate(Screen.Bookings.createRoute(0)) // Pending tab
                                1 -> navController.navigate(Screen.Bookings.createRoute(1)) // Accepted tab
                                2 -> navController.navigate(Screen.Bookings.createRoute(2)) // Completed tab
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    StatsSection(
                        title = "L&Q Referrals",
                        stats = listOf(
                            StatItem(1, "Pending"),
                            StatItem(3, "Confirmed"),
                            StatItem(0, "Completed")
                        ),
                        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                        onStatClick = { /* Handle L&Q stat click */ }
                    )
                }
                is HomeUiState.Error -> {
                    Text(
                        text = (uiState as HomeUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarSection(
    bookingDates: Set<LocalDate>,
    currentMonth: YearMonth,
    startMonth: YearMonth,
    endMonth: YearMonth,
    daysOfWeek: List<DayOfWeek>,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = remember { LocalDate.now() }
    var selectedDate by remember { mutableStateOf(today) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Month name
            Text(
                text = YearMonth.from(today).month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            // Calendar content
            HorizontalCalendar(
                modifier = Modifier.fillMaxSize(),
                state = rememberCalendarState(
                    startMonth = YearMonth.from(today),
                    endMonth = YearMonth.from(today),
                    firstVisibleMonth = YearMonth.from(today),
                    firstDayOfWeek = daysOfWeek.first()
                ),
                dayContent = { day ->
                    val isSelected = day.date == selectedDate
                    val isToday = day.date == today
                    val hasBooking = bookingDates.contains(day.date)
                    
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    isToday -> Color(0xFF4CAF50) // Green color for today
                                    hasBooking -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                    else -> Color.Transparent
                                }
                            )
                            .clickable {
                                selectedDate = day.date
                                onDateSelected(day.date)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.date.dayOfMonth.toString(),
                            fontSize = 14.sp,
                            color = when {
                                isSelected || isToday -> Color.White
                                day.position == DayPosition.MonthDate -> MaterialTheme.colorScheme.onSurface
                                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            }
                        )
                    }
                },
                monthHeader = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        daysOfWeek.forEach { dayOfWeek ->
                            Text(
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                text = dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun MonthHeader(
    daysOfWeek: List<DayOfWeek>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    isToday: Boolean,
    hasBooking: Boolean,
    isVisible: Boolean,
    onDateSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .clip(CircleShape)
            .background(
                when {
                    !isVisible -> Color.Transparent
                    isToday -> Color.Red.copy(alpha = 0.1f)
                    hasBooking -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    else -> Color.Transparent
                }
            )
            .clickable(
                enabled = isVisible && day.position == DayPosition.MonthDate,
                onClick = onDateSelected
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isVisible || day.position == DayPosition.MonthDate) {
            Text(
                text = day.date.dayOfMonth.toString(),
                fontSize = 12.sp,
                color = when {
                    !isVisible -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    isToday -> Color.Red
                    day.position == DayPosition.MonthDate -> MaterialTheme.colorScheme.onSurface
                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                }
            )
            
            if (isToday) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .border(1.dp, Color.Red, CircleShape)
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    userName: String = "",
    profileImageUrl: String? = null,
    onNotificationClick: () -> Unit,
    onMessageClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .border(
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        shape = CircleShape
                    )
            ) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    fallback = painterResource(R.drawable.default_profile),
                    error = painterResource(R.drawable.default_profile)
                )
            }
            
            Column {
                Text(
                    text = "Welcome",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = userName.ifEmpty { "User" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            IconButton(onClick = onMessageClick) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Messages",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun StatsSection(
    title: String,
    stats: List<StatItem>,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    onStatClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = backgroundColor,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                stats.forEachIndexed { index, stat ->
                    StatCard(
                        number = stat.number,
                        label = stat.label,
                        onClick = { onStatClick(index) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    number: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(2.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

data class StatItem(
    val number: Int,
    val label: String,
    val onClick: () -> Unit = {}
)
