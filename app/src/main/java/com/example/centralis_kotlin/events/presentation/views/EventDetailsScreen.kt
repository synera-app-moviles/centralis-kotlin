package com.example.centralis_kotlin.events.presentation.views

        import androidx.compose.foundation.border
        import androidx.compose.foundation.layout.*
        import androidx.compose.foundation.shape.RoundedCornerShape
        import androidx.compose.material.icons.Icons
        import androidx.compose.material.icons.filled.ArrowBack
        import androidx.compose.material3.*
        import androidx.compose.runtime.*
        import androidx.compose.ui.Alignment
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.graphics.Color
        import androidx.compose.ui.text.font.FontWeight
        import androidx.compose.ui.unit.dp
        import androidx.compose.ui.unit.sp
        import androidx.compose.ui.window.Dialog
        import com.example.centralis_kotlin.ui.theme.CentralisPrimary

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun EventDetailsScreen(
            title: String,
            description: String,
            location: String,
            attendees: Int,
            dateTime: String,
            onEditEvent: () -> Unit,
            onDeleteEvent: () -> Unit,
            onBack: () -> Unit
        ) {
            var showDeleteDialog by remember { mutableStateOf(false) }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Event details",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.offset(x = (-20).dp)
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
                            containerColor = Color(0xFF160F23)
                        )
                    )
                },
                containerColor = Color(0xFF160F23)
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Description:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA68FCC)
                    )
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Location:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA68FCC)
                    )
                    Text(
                        text = location.ifBlank { "Sin ubicación" },
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Attendees: $attendees",
                        fontSize = 14.sp,
                        color = Color(0xFFA68FCC)
                    )
                    Text(
                        text = "DateTime:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA68FCC)
                    )
                    Text(
                        text = dateTime,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onEditEvent,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF30214A)),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                "Edit Event",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { showDeleteDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF30214A)),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                "Delete Event",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (showDeleteDialog) {
                        Dialog(onDismissRequest = { showDeleteDialog = false }) {
                            Surface(
                                color = Color(0xFF302149),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .width(320.dp)
                                    .border(
                                        width = 1.dp,
                                        color = CentralisPrimary,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            ) {
                                Column(
                                    Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Do you want to delete this event?",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                    Spacer(Modifier.height(24.dp))
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Button(
                                            onClick = { showDeleteDialog = false },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF30214A)),
                                            modifier = Modifier.weight(1f)
                                        ) { Text("Close", color = Color.White, fontSize = 17.sp) }
                                        Spacer(Modifier.width(16.dp))
                                        Button(
                                            onClick = {
                                                showDeleteDialog = false
                                                onDeleteEvent()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF30214A)),
                                            modifier = Modifier.weight(1f)
                                        ) { Text("Delete", color = Color.White, fontSize = 17.sp) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }