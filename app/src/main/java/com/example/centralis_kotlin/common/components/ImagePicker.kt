package com.example.centralis_kotlin.common.components

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.example.centralis_kotlin.common.config.ImageType
import com.example.centralis_kotlin.common.config.UploadState
import com.example.centralis_kotlin.common.services.CloudinaryService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImagePicker(
    imageType: ImageType,
    currentImageUrl: String? = null,
    onImageUploaded: (String) -> Unit,
    onImageRemoved: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cloudinaryService = remember { CloudinaryService(context) }
    val coroutineScope = rememberCoroutineScope()
    
    // Estados
    var showDialog by remember { mutableStateOf(false) }
    var uploadState by remember { mutableStateOf<UploadState>(UploadState.Idle) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    
    // Permisos
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    
    // Crear archivo temporal para la cámara
    val tempImageFile = remember {
        File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
    }
    
    val tempImageFileUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempImageFile
        )
    }
    
    // Launcher para galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            if (cloudinaryService.isValidImageType(selectedUri)) {
                uploadImage(selectedUri, imageType, cloudinaryService, coroutineScope) { state ->
                    uploadState = state
                    if (state is UploadState.Success) {
                        onImageUploaded(state.imageUrl)
                    }
                }
            }
        }
    }
    
    // Launcher para cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            uploadImage(tempImageFileUri, imageType, cloudinaryService, coroutineScope) { state ->
                uploadState = state
                if (state is UploadState.Success) {
                    onImageUploaded(state.imageUrl)
                }
            }
        }
    }
    
    // UI
    Column(modifier = modifier) {
        // Botón para abrir selector
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF823DF9)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            val currentState = uploadState
            when (currentState) {
                is UploadState.Loading -> {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Subiendo...")
                }
                is UploadState.Progress -> {
                    Text("${currentState.percentage.toInt()}%")
                }
                else -> {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (imageType) {
                            ImageType.AVATAR -> "Seleccionar Avatar"
                            ImageType.CHAT -> "Enviar Imagen"
                            ImageType.ANNOUNCEMENT -> "Agregar Imagen"
                        }
                    )
                }
            }
        }
        
        // Mostrar estado de error
        val currentStateForError = uploadState
        if (currentStateForError is UploadState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currentStateForError.message,
                color = Color.Red,
                fontSize = 12.sp
            )
        }
        
        // Mostrar opción de eliminar si hay imagen actual
        if (currentImageUrl != null && uploadState !is UploadState.Loading) {
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = {
                    onImageRemoved()
                    uploadState = UploadState.Idle
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Eliminar imagen", color = Color.Red)
            }
        }
    }
    
    // Diálogo de selección
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF302149))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Seleccionar imagen",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    // Opción Galería
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF4A3B5C))
                            .clickable {
                                showDialog = false
                                galleryLauncher.launch("image/*")
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Galería",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    
                    // Opción Cámara
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF4A3B5C))
                            .clickable {
                                showDialog = false
                                if (cameraPermissionState.status.isGranted) {
                                    cameraLauncher.launch(tempImageFileUri)
                                } else {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Cámara",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    
                    // Botón Cancelar
                    TextButton(
                        onClick = { showDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cancelar", color = Color(0xFFD6BBFB))
                    }
                }
            }
        }
    }
}

/**
 * Función helper para subir imagen
 */
private fun uploadImage(
    uri: Uri,
    imageType: ImageType,
    cloudinaryService: CloudinaryService,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onStateChange: (UploadState) -> Unit
) {
    coroutineScope.launch {
        cloudinaryService.uploadImage(uri, imageType).collect { state ->
            onStateChange(state)
        }
    }
}