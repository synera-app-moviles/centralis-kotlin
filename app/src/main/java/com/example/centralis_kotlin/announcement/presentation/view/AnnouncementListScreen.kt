@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.centralis_kotlin.announcement.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.centralis_kotlin.announcement.model.Priority
import com.example.centralis_kotlin.announcement.presentation.viewmodels.AnnouncementViewModel

@Composable
fun AnnouncementListScreen(
    onSelect: (String) -> Unit, // pasamos el ID en lugar del objeto completo
    onCreate: () -> Unit = {},
    isManager: Boolean = false,
    vm: AnnouncementViewModel = viewModel()
) {
    val announcementsState by vm.announcements.collectAsState()

    // Lanzamos la carga al entrar a la pantalla
    LaunchedEffect(Unit) {
        vm.loadAnnouncements()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Announcements", color = MaterialTheme.colorScheme.onBackground) }
            )
        },
        floatingActionButton = {
            if (isManager) {
                FloatingActionButton(
                    onClick = onCreate,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create announcement",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { padding ->
        when {
            announcementsState.isEmpty() -> {
                // Si aún no hay anuncios (o sigue cargando)
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
                // Mostrar lista de anuncios
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
                            onClick = { onSelect(announcement.id) }
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
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono en cajita a la izquierda
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = "Announcement icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Título
            Text(
                text = announcementTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Priority pill
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
