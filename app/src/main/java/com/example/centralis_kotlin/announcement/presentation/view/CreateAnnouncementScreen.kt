@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.centralis_kotlin.announcement.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.centralis_kotlin.announcement.model.Announcement
import com.example.centralis_kotlin.announcement.model.Priority
import com.example.centralis_kotlin.announcement.presentation.viewmodels.AnnouncementViewModel
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateAnnouncementScreen(
    onCreated: () -> Unit,
    vm: AnnouncementViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.Normal) }

    Scaffold(
        containerColor = Color(0xFF120E1C), // fondo oscuro
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "New Announcement",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCreated) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        val newAnnouncement = Announcement(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            description = description,
                            image = null,
                            priority = selectedPriority,
                            createdAt = Date(),
                            createdBy = "123e4567-e89b-12d3-a456-426614174000", // luego lo traemos de IAM
                            comments = mutableListOf(),
                            seenBy = mutableSetOf()
                        )
                        vm.createAnnouncement(newAnnouncement)
                        onCreated()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8A46FF), // morado
                    contentColor = Color.White
                )
            ) {
                Text("Publish Announcement")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF120E1C))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Announcement Title", color = Color(0xFFB39DDB)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2A1E4D),
                    unfocusedContainerColor = Color(0xFF2A1E4D),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                placeholder = { Text("Write the announcement details...", color = Color(0xFFB39DDB)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2A1E4D),
                    unfocusedContainerColor = Color(0xFF2A1E4D),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Upload Image
            Button(
                onClick = { /* TODO: implementar carga */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2A1E4D),
                    contentColor = Color(0xFFB39DDB)
                )
            ) {
                Text("Upload Image")
            }

            // Priority selector
            Text(
                text = "Priority",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(Priority.Normal, Priority.High, Priority.Urgent).forEach { priority ->
                    val isSelected = selectedPriority == priority
                    Button(
                        onClick = { selectedPriority = priority },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF8A46FF) else Color.Transparent,
                            contentColor = if (isSelected) Color.White else Color(0xFFB39DDB)
                        ),
                        border = if (!isSelected) ButtonDefaults.outlinedButtonBorder else null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(priority.name)
                    }
                }
            }
        }
    }
}
