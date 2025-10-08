package com.example.centralis_kotlin.events.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.centralis_kotlin.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    onCreate: (Event) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Create Event",
                            color = androidx.compose.ui.graphics.Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(x = (-24).dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel", tint = androidx.compose.ui.graphics.Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color(0xFF170F24))
            )
        },
        containerColor = androidx.compose.ui.graphics.Color(0xFF170F24)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color(0xFF170F24))
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
                    label = { Text("Event title", color = androidx.compose.ui.graphics.Color(0xFFA68FCC)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = androidx.compose.ui.graphics.Color(0xFF30214A),
                        unfocusedContainerColor = androidx.compose.ui.graphics.Color(0xFF30214A),
                        focusedTextColor = androidx.compose.ui.graphics.Color(0xFFA68FCC),
                        unfocusedTextColor = androidx.compose.ui.graphics.Color(0xFFA68FCC),
                        focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Event description", color = androidx.compose.ui.graphics.Color(0xFFA68FCC)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = androidx.compose.ui.graphics.Color(0xFF30214A),
                        unfocusedContainerColor = androidx.compose.ui.graphics.Color(0xFF30214A),
                        focusedTextColor = androidx.compose.ui.graphics.Color(0xFFA68FCC),
                        unfocusedTextColor = androidx.compose.ui.graphics.Color(0xFFA68FCC),
                        focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )

                OutlinedTextField(
                    value = dateTime,
                    onValueChange = { dateTime = it },
                    label = { Text("Fecha y hora del evento", color = androidx.compose.ui.graphics.Color(0xFFA68FCC)) },
                    trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = androidx.compose.ui.graphics.Color(0xFFA68FCC)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = androidx.compose.ui.graphics.Color(0xFF30214A),
                        unfocusedContainerColor = androidx.compose.ui.graphics.Color(0xFF30214A),
                        focusedTextColor = androidx.compose.ui.graphics.Color(0xFFA68FCC),
                        unfocusedTextColor = androidx.compose.ui.graphics.Color(0xFFA68FCC),
                        focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location", color = androidx.compose.ui.graphics.Color(0xFFA68FCC)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = androidx.compose.ui.graphics.Color(0xFF30214A),
                        unfocusedContainerColor = androidx.compose.ui.graphics.Color(0xFF30214A),
                        focusedTextColor = androidx.compose.ui.graphics.Color(0xFFA68FCC),
                        unfocusedTextColor = androidx.compose.ui.graphics.Color(0xFFA68FCC),
                        focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        val newEvent = Event(title, location, 0, dateTime)
                        onCreate(newEvent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CentralisPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Create Event", color = CentralisOnPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
