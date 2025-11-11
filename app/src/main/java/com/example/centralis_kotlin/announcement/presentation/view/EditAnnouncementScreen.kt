@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.centralis_kotlin.announcement.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.centralis_kotlin.announcement.model.Announcement
import com.example.centralis_kotlin.announcement.model.Priority
import com.example.centralis_kotlin.announcement.presentation.viewmodels.AnnouncementViewModel
import com.example.centralis_kotlin.common.components.ImagePicker
import com.example.centralis_kotlin.common.config.ImageType

@OptIn(ExperimentalGlideComposeApi::class)
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
    var announcementImageUrl by remember { mutableStateOf<String?>(null) }

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
            announcementImageUrl = it.image
        }
    }

    Scaffold(
        containerColor = Color(0xFF160F23),
        topBar = {
            TopAppBar(
                title = { Text("Editar anuncio", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
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
            if (selected == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                val scrollState = rememberScrollState()
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("Título del anuncio", color = Color(0xFFB39DDB)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF4A2B61),
                            unfocusedContainerColor = Color(0xFF4A2B61),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("Descripción del anuncio", color = Color(0xFFB39DDB)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF4A2B61),
                            unfocusedContainerColor = Color(0xFF4A2B61),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Selector de prioridad
                    Text("Prioridad", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    PrioritySelector(
                        selectedPriority = selectedPriority,
                        onPrioritySelected = { selectedPriority = it }
                    )
                    
                    // Editor de imagen
                    Text("Imagen del anuncio", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    ImagePicker(
                        imageType = ImageType.ANNOUNCEMENT,
                        currentImageUrl = announcementImageUrl,
                        onImageUploaded = { imageUrl ->
                            announcementImageUrl = imageUrl
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Previsualización de la imagen
                    if (!announcementImageUrl.isNullOrEmpty()) {
                        Text(
                            text = "Previsualización:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                            color = Color.White
                        )
                        
                        GlideImage(
                            model = announcementImageUrl,
                            contentDescription = "Preview de la imagen del anuncio",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val updatedAnnouncement = selected!!.copy(
                                title = title,
                                description = description,
                                priority = selectedPriority,
                                image = announcementImageUrl
                            )
                            vm.createAnnouncement(updatedAnnouncement)
                            onUpdated()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8A46FF),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
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
