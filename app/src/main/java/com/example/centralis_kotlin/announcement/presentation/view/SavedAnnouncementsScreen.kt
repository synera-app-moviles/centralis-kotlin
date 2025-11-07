package com.example.centralis_kotlin.announcement.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.centralis_kotlin.announcement.presentation.viewmodels.LocalAnnouncementViewModel
import com.example.centralis_kotlin.announcement.model.Announcement
import com.example.centralis_kotlin.ui.theme.CentralisSecondary
import com.example.centralis_kotlin.ui.theme.CentralisBackground
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedAnnouncementsScreen(
    navController: NavHostController? = null,
    viewModel: LocalAnnouncementViewModel = viewModel()
) {
    val savedAnnouncements by viewModel.getAllSavedAnnouncements().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Announcements", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CentralisSecondary)
            )
        },
        containerColor = CentralisSecondary
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CentralisBackground)
                .padding(innerPadding)
        ) {
            if (savedAnnouncements.isEmpty()) {
                Text(
                    text = "No saved announcements yet.",
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(savedAnnouncements) { announcement ->
                        SavedAnnouncementCard(
                            announcement = announcement,
                            onDelete = { viewModel.deleteAnnouncement(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavedAnnouncementCard(
    announcement: Announcement,
    onDelete: (Announcement) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CentralisSecondary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = announcement.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = announcement.description,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            Text(
                text = "Created: ${sdf.format(announcement.createdAt)}",
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { onDelete(announcement) },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF8E3DF9), RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

