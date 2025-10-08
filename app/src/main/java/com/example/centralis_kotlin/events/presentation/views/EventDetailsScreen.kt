package com.example.centralis_kotlin.events.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.centralis_kotlin.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    title: String,
    location: String,
    attendees: Int,
    dateTime: String,
    onConfirmRead: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Event Details",
                            color = androidx.compose.ui.graphics.Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.offset(x = (-24).dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onConfirmRead) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = androidx.compose.ui.graphics.Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF170F24)
                )
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
                    .fillMaxSize()
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = androidx.compose.ui.graphics.Color.White)
                Spacer(Modifier.height(8.dp))
                Text("This is a detailed description of the event.", color = androidx.compose.ui.graphics.Color(0xFFB3B3B3))

                Spacer(Modifier.height(24.dp))

                DetailItem("Date", dateTime.split(" ")[0])
                DetailItem("Time", dateTime.split(" ")[1])
                DetailItem("Location", location)
                DetailItem("Attendees", attendees.toString())
                DetailItem("Created by", "Ethan Carter")

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onConfirmRead,
                    colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF8C3DFA)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Confirm Read", color = androidx.compose.ui.graphics.Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, fontSize = 14.sp, color = androidx.compose.ui.graphics.Color(0xFFB3B3B3))
        Text(value, fontSize = 16.sp, color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(androidx.compose.ui.graphics.Color.White.copy(alpha = 0.3f))
        )
    }
}
