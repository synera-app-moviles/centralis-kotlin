@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.centralis_kotlin.announcement.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.centralis_kotlin.announcement.presentation.view.components.CommentCard
import com.example.centralis_kotlin.announcement.presentation.viewmodels.AnnouncementViewModel
import com.example.centralis_kotlin.common.navigation.NavigationRoutes
import com.example.centralis_kotlin.common.SharedPreferencesManager
import com.example.centralis_kotlin.analytics.service.AnalyticsService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private val DETAIL_DATE_FORMATTER = SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a las' HH:mm", Locale("es", "ES"))

@OptIn(ExperimentalGlideComposeApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnnouncementDetailScreen(
    announcementId: String,
    onBack: () -> Unit,
    vm: AnnouncementViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val sharedPrefsManager = remember { SharedPreferencesManager(context) }
    val currentUserId = sharedPrefsManager.getUserId() ?: ""
    val analyticsService = remember { AnalyticsService() }
    val scope = rememberCoroutineScope()
    
    val selected by vm.selectedAnnouncement.collectAsState()
    var commentText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    //  Cargar detalle + comentarios desde backend al entrar
    LaunchedEffect(announcementId) {
        isLoading = true
        vm.selectAnnouncement(announcementId)
        isLoading = false
    }

    // Registrar vista del announcement cuando se cargue exitosamente
    LaunchedEffect(selected) {
        selected?.let { announcement ->
            if (currentUserId.isNotEmpty()) {
                scope.launch {
                    val token = sharedPrefsManager.getToken()
                    val authHeader = if (token?.startsWith("Bearer ") == true) token else "Bearer ${token ?: ""}"
                    analyticsService.registerAnnouncementView(
                        authorization = authHeader,
                        announcementId = announcement.id,
                        userId = currentUserId
                    )
                }
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFF160F23),
        topBar = {
            TopAppBar(
                title = { Text(text = "Announcement Detail", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Solo mostrar el menú si el usuario actual es el creador
                    if (selected?.createdBy == currentUserId && currentUserId.isNotEmpty()) {
                        var expanded by remember { mutableStateOf(false) }

                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = Color.White
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("${NavigationRoutes.ANNOUNCEMENT_EDIT}/$announcementId")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete", color = Color.Red) },
                                onClick = {
                                    expanded = false
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF160F23)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF160F23))
        ) {
            when {
                isLoading || selected == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                else -> {
                    val announcement = selected!!

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        //  Título
                        Text(
                            text = announcement.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        //  Fecha
                        val createdAtText = try {
                            DETAIL_DATE_FORMATTER.format(announcement.createdAt)
                        } catch (t: Throwable) {
                            announcement.createdAt.toString()
                        }

                        Text(
                            text = "Publicado: $createdAtText",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Imagen del anuncio (si existe)
                        if (!announcement.image.isNullOrEmpty()) {
                            GlideImage(
                                model = announcement.image,
                                contentDescription = "Imagen del anuncio",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        //  Descripción
                        Text(
                            text = announcement.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        //  Comentarios
                        Text(
                            text = "Comments",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
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

                        //  Input para nuevo comentario
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = commentText,
                                onValueChange = { commentText = it },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("Write a comment...") }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(onClick = {
                                if (commentText.isNotBlank()) {
                                    vm.addCommentRemote(
                                        announcementId = announcement.id,
                                        content = commentText,
                                        employeeId = currentUserId // Usar el ID del usuario autenticado
                                    )
                                    commentText = ""
                                }
                            }) {
                                Text("Send")
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Announcement") },
            text = { Text("Are you sure you want to delete this announcement? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.deleteAnnouncement(announcementId)
                        showDeleteDialog = false
                        onBack() // Regresar después de eliminar
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
