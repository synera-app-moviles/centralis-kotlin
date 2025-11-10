@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.centralis_kotlin.announcement.presentation.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.centralis_kotlin.announcement.data.AppDatabase
import com.example.centralis_kotlin.announcement.data.LocalAnnouncementRepository
import com.example.centralis_kotlin.announcement.model.Priority
import com.example.centralis_kotlin.announcement.presentation.viewmodels.AnnouncementViewModel
import com.example.centralis_kotlin.common.navigation.NavigationRoutes
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun AnnouncementListScreen(
    navController: NavHostController,
    vm: AnnouncementViewModel = viewModel()
) {
    val announcementsState by vm.announcements.collectAsState()

    // Cargar anuncios al entrar
    LaunchedEffect(Unit) {
        vm.loadAnnouncements()
    }

    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val localRepo = remember { LocalAnnouncementRepository(db.announcementDao()) }
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        containerColor = Color(0xFF160F23),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Announcements", color = Color.White) },
                actions = {
                    // Botón de guardar localmente
                    IconButton(onClick = {
                        coroutineScope.launch {
                            announcementsState.forEach { announcement ->
                                localRepo.saveAnnouncement(announcement)
                            }
                            Toast.makeText(context, "Anuncios guardados localmente", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Guardar localmente",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF160F23)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavigationRoutes.ANNOUNCEMENT_CREATE) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create announcement",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        when {
            announcementsState.isEmpty() -> {
                // Si no hay anuncios
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(announcementsState, key = { it.id }) { announcement ->
                        AnnouncementCard(
                            announcementTitle = announcement.title,
                            priority = announcement.priority,
                            onClick = {
                                navController.navigate("${NavigationRoutes.ANNOUNCEMENT_DETAIL}/${announcement.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnnouncementCard(
    announcementTitle: String,
    priority: Priority,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4A2B61)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = "Announcement icon",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Titulo
            Text(
                text = announcementTitle,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Etiqueta de prioridad
            val pillColor = when (priority) {
                Priority.LOW -> MaterialTheme.colorScheme.tertiary
                Priority.High -> MaterialTheme.colorScheme.secondary
                Priority.Urgent -> MaterialTheme.colorScheme.primary
                Priority.Normal -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = pillColor,
                tonalElevation = 0.dp
            ) {
                Text(
                    text = priority.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}
