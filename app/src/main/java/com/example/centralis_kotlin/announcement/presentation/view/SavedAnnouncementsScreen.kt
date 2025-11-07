package com.example.centralis_kotlin.announcement.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.centralis_kotlin.announcement.local.LocalAnnouncementRepository
import com.example.centralis_kotlin.announcement.local.SavedAnnouncementEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedAnnouncementsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val repo = remember { LocalAnnouncementRepository(context) }
    val scope = rememberCoroutineScope()

    var savedAnnouncements by remember { mutableStateOf<List<SavedAnnouncementEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        savedAnnouncements = repo.getAll()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anuncios Guardados") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Delete, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (savedAnnouncements.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No tienes anuncios guardados")
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(savedAnnouncements) { item ->
                    Card(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(item.title, style = MaterialTheme.typography.titleMedium)
                                Text(item.description, style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = {
                                scope.launch {
                                    repo.deleteById(item.id)
                                    savedAnnouncements = repo.getAll()
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}
