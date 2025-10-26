package com.example.centralis_kotlin.events.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.centralis_kotlin.events.model.UpdateEventRequest
import com.example.centralis_kotlin.ui.theme.CentralisOnPrimary
import com.example.centralis_kotlin.ui.theme.CentralisPrimary
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateEventScreen(
    eventId: UUID,
    currentTitle: String,
    currentDescription: String,
    currentDateTime: String,
    currentLocation: String?,
    onUpdate: (UUID, UpdateEventRequest) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(currentTitle) }
    var description by remember { mutableStateOf(currentDescription) }
    var dateTime by remember { mutableStateOf(currentDateTime) }
    var location by remember { mutableStateOf(currentLocation ?: "") }


    fun toIsoLike(input: String): String {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return trimmed
        return if (trimmed.contains(' ')) {
            val t = trimmed.replace(' ', 'T')
            if (t.length <= 16) "$t:00" else t
        } else if (trimmed.contains('T')) {
            if (trimmed.length <= 16) "$trimmed:00" else trimmed
        } else {
            trimmed
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Update Event",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(x = (-24).dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF170F24)
                )
            )
        },
        containerColor = Color(0xFF170F24)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF170F24))
        ) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Event title", color = Color(0xFFA68FCC)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF30214A),
                        unfocusedContainerColor = Color(0xFF30214A),
                        focusedTextColor = Color(0xFFA68FCC),
                        unfocusedTextColor = Color(0xFFA68FCC),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Event description", color = Color(0xFFA68FCC)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF30214A),
                        unfocusedContainerColor = Color(0xFF30214A),
                        focusedTextColor = Color(0xFFA68FCC),
                        unfocusedTextColor = Color(0xFFA68FCC),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )

                OutlinedTextField(
                    value = dateTime,
                    onValueChange = { dateTime = it },
                    label = { Text("Date and time", color = Color(0xFFA68FCC)) },
                    trailingIcon = {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFFA68FCC)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF30214A),
                        unfocusedContainerColor = Color(0xFF30214A),
                        focusedTextColor = Color(0xFFA68FCC),
                        unfocusedTextColor = Color(0xFFA68FCC),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location", color = Color(0xFFA68FCC)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF30214A),
                        unfocusedContainerColor = Color(0xFF30214A),
                        focusedTextColor = Color(0xFFA68FCC),
                        unfocusedTextColor = Color(0xFFA68FCC),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        val formattedDate = try {
                            toIsoLike(dateTime)
                        } catch (e: Exception) {
                            dateTime
                        }

                        val request = UpdateEventRequest(
                            title = title,
                            description = description,
                            date = formattedDate,
                            location = location.ifBlank { null },
                            recipientIds = null
                        )
                        onUpdate(eventId, request)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CentralisPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        "Update Event",
                        color = CentralisOnPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
