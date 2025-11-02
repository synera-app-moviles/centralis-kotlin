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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.centralis_kotlin.events.model.EventResponse
import com.example.centralis_kotlin.events.presentation.viewmodels.EventViewModel
import com.example.centralis_kotlin.events.presentation.viewmodels.EventUiState
import com.example.centralis_kotlin.common.di.DependencyFactory
import kotlinx.coroutines.launch
import java.util.UUID

private fun formatDateSafe(dateObj: Any?): String {
    return try {
        val s = dateObj?.toString() ?: ""
        val replaced = s.replace('T', ' ')
        if (replaced.length > 16) replaced.substring(0, 16) else replaced
    } catch (_: Exception) { "" }
}

@Composable
fun EventsScreen(
    onAddEventClick: () -> Unit,
    onViewEventClick: (EventResponse) -> Unit
) {
    val context = LocalContext.current
    val eventViewModel = remember { DependencyFactory.createEventViewModel(context) }
    
    // Observar estados del ViewModel
    val uiState by eventViewModel.uiState.collectAsState()
    val events by eventViewModel.events.collectAsState()
    
    // Cargar eventos al iniciar
    LaunchedEffect(Unit) {
        eventViewModel.loadAllEvents()
    }

    Scaffold(
        topBar = { EventsTopBar(onAddEventClick) },
        containerColor = Color(0xFF160F23)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF160F23))
        ) {
            when (uiState) {
                is EventUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                is EventUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text((uiState as EventUiState.Error).message, color = Color.Red)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { eventViewModel.loadAllEvents() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.padding(padding)
                    ) {
                        items(events) { event ->
                            EventItem(
                                event = event,
                                onViewClick = { onViewEventClick(event) }
                            )
                        }
                    }
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
                    color = Color.White,
                    modifier = Modifier.offset(y = (-20).dp)

                )
            }
                },
        actions = {
            IconButton(
                onClick = onAddEventClick,
                modifier = Modifier.offset(y = (-20).dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Event",
                    tint = Color.White
                )
            }
                  },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF170F24)
        )
    )
}

@Composable
fun EventItem(event: EventResponse, onViewClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFF30214A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Event Icon",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = event.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Location: ${event.location ?: "Sin ubicación"}",
                    fontSize = 14.sp,
                    color = Color(0xFFB3B3B3)
                )
                Text(
                    text = "Attendees: ${event.recipientIds.size}",
                    fontSize = 14.sp,
                    color = Color(0xFFB3B3B3)
                )
                val dateStr = formatDateSafe(event.date)
                Text(
                    text = dateStr,
                    fontSize = 14.sp,
                    color = Color(0xFFB3B3B3)
                )
            }
        }
        Button(
            onClick = onViewClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF302149)
            ),
            modifier = Modifier
                .height(40.dp)
                .width(90.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Details",
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}