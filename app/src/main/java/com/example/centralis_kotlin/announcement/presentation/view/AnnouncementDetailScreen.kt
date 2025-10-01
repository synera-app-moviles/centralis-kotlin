@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.centralis_kotlin.announcement.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.centralis_kotlin.announcement.model.Comment
import com.example.centralis_kotlin.announcement.presentation.view.components.CommentCard
import com.example.centralis_kotlin.announcement.presentation.viewmodels.AnnouncementViewModel
import java.text.SimpleDateFormat
import java.util.*

private val DETAIL_DATE_FORMATTER = SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a las' HH:mm", Locale("es", "ES"))

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnnouncementDetailScreen(
    announcementId: String,
    onBack: () -> Unit,
    vm: AnnouncementViewModel = viewModel()
) {
    val selected by vm.selectedAnnouncement.collectAsState()
    var commentText by remember { mutableStateOf("") }

    LaunchedEffect(announcementId) {
        vm.selectAnnouncement(announcementId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Announcement Detail", color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (selected == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                return@Box
            }

            val announcement = selected!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = announcement.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                val createdAtText = try {
                    DETAIL_DATE_FORMATTER.format(announcement.createdAt)
                } catch (t: Throwable) {
                    announcement.createdAt.toString()
                }

                Text(
                    text = "Publicado: $createdAtText",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = announcement.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Comentarios",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(announcement.comments) { comment ->
                        CommentCard(comment)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Escribe un comentario...") }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        if (commentText.isNotBlank()) {
                            val newComment = Comment(
                                id = UUID.randomUUID().toString(),
                                announcementId = announcement.id,
                                employeeId = "employee_1", // aquí conectas con IAM
                                content = commentText,
                                createdAt = Date()
                            )
                            vm.addComment(announcement.id, newComment)
                            commentText = ""
                        }
                    }) {
                        Text("Enviar")
                    }
                }
            }
        }
    }
}
