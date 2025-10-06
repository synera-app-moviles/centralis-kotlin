@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.centralis_kotlin.announcement.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.centralis_kotlin.announcement.model.Announcement
import com.example.centralis_kotlin.announcement.model.Priority
import com.example.centralis_kotlin.announcement.presentation.viewmodels.AnnouncementViewModel

@Composable
fun EditAnnouncementScreen(
    announcementId: String,
    onBack: () -> Unit,
    onUpdated: () -> Unit,
    vm: AnnouncementViewModel = viewModel()
) {
    val selected by vm.selectedAnnouncement.collectAsState()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.Normal) }

    // Cargar datos del anuncio
    LaunchedEffect(announcementId) {
        vm.selectAnnouncement(announcementId)
    }

    // Prellenar cuando llega el anuncio
    LaunchedEffect(selected) {
        selected?.let {
            title = it.title
            description = it.description
            selectedPriority = it.priority
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar anuncio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (selected == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    )

                    // Selector de prioridad
                    Text("Prioridad", style = MaterialTheme.typography.titleMedium)
                    PrioritySelector(
                        selectedPriority = selectedPriority,
                        onPrioritySelected = { selectedPriority = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val updatedAnnouncement = selected!!.copy(
                                title = title,
                                description = description,
                                priority = selectedPriority
                            )
                            vm.createAnnouncement(updatedAnnouncement) // reutilizamos save()
                            onUpdated()
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Actualizar anuncio")
                    }
                }
            }
        }
    }
}

@Composable
private fun PrioritySelector(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    val options = listOf(Priority.Normal, Priority.High, Priority.Urgent)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { priority ->
            FilterChip(
                selected = selectedPriority == priority,
                onClick = { onPrioritySelected(priority) },
                label = { Text(priority.name) }
            )
        }
    }
}
