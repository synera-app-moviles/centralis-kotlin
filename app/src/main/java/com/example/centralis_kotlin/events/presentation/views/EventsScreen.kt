package com.example.centralis_kotlin.events.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.centralis_kotlin.ui.theme.*

data class Event(
    val title: String,
    val location: String,
    val attendees: Int,
    val dateTime: String
)

@Composable
fun EventsScreen(
    events: List<Event> = sampleEvents,
    onAddEventClick: () -> Unit = {},
    onViewEventClick: (Event) -> Unit = {}
) {
    Scaffold(
        topBar = {
            EventsTopBar(onAddEventClick = onAddEventClick)
        },
        bottomBar = { EventsBottomNav() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color(0xFF170F24))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(events) { event ->
                    EventItem(event, onViewClick = { onViewEventClick(event) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsTopBar(onAddEventClick: () -> Unit) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Events",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.offset(y = (-4).dp)
                )
            }
        },
        actions = {
            IconButton(
                onClick = onAddEventClick,
                modifier = Modifier.offset(y = (-4).dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Event",
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = androidx.compose.ui.graphics.Color(0xFF170F24)
        )
    )
}

@Composable
fun EventItem(event: Event, onViewClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(androidx.compose.ui.graphics.Color(0xFF30214A))
                    .offset(x = 0.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Event Icon",
                    tint = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = event.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.White
                )
                Text(
                    text = "Location: ${event.location}",
                    fontSize = 14.sp,
                    color = androidx.compose.ui.graphics.Color(0xFFB3B3B3)
                )
                Text(
                    text = "Attendees: ${event.attendees}",
                    fontSize = 14.sp,
                    color = androidx.compose.ui.graphics.Color(0xFFB3B3B3)
                )
                Text(
                    text = event.dateTime,
                    fontSize = 14.sp,
                    color = androidx.compose.ui.graphics.Color(0xFFB3B3B3)
                )
            }
        }
        Button(
            onClick = onViewClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = androidx.compose.ui.graphics.Color(0xFF30214A)
            ),
            modifier = Modifier
                .height(40.dp)
                .width(70.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "View",
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun EventsBottomNav() {
    NavigationBar(
        containerColor = androidx.compose.ui.graphics.Color(0xFF30214A),
        modifier = Modifier.height(64.dp)
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { /* TODO */ },
            icon = {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = androidx.compose.ui.graphics.Color(0xFF30214A),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .offset(y = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Events",
                        tint = androidx.compose.ui.graphics.Color(0xFFA68FCC),
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            label = {
                Text(
                    "Events",
                    color = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.offset(y = 10.dp)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = androidx.compose.ui.graphics.Color(0xFFA68FCC),
                unselectedIconColor = androidx.compose.ui.graphics.Color(0xFFA68FCC),
                selectedTextColor = androidx.compose.ui.graphics.Color.White,
                unselectedTextColor = androidx.compose.ui.graphics.Color.White,
                indicatorColor = androidx.compose.ui.graphics.Color(0xFF30214A)
            )
        )
    }
}

// Datos de ejemplo
val sampleEvents = listOf(
    Event("Team Meeting", "Conference Room A", 25, "2024-03-15 10:00 AM"),
    Event("Sales Training", "Training Center B", 40, "2024-03-16 02:00 PM"),
    Event("Leadership Workshop", "Main Auditorium", 30, "2024-03-17 09:00 AM"),
    Event("Marketing Meeting", "Marketing Suite", 20, "2024-03-18 03:00 PM"),
    Event("Product Presentation", "Presentation Hall", 50, "2024-03-19 11:00 AM")
)
