package com.example.centralis_kotlin.common.services

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.centralis_kotlin.common.config.CloudinaryConfig
import com.example.centralis_kotlin.common.config.ImageType
import com.example.centralis_kotlin.common.config.UploadState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

class CloudinaryService(private val context: Context) {
    
    init {
        CloudinaryConfig.initialize(context)
    }
    
    /**
     * Subir imagen a Cloudinary de forma asíncrona
     * @param uri URI de la imagen seleccionada
     * @param imageType Tipo de imagen (AVATAR, CHAT, ANNOUNCEMENT)
     * @return Flow que emite el estado de la subida
     */
    fun uploadImage(uri: Uri, imageType: ImageType): Flow<UploadState> = callbackFlow {
        trySend(UploadState.Loading)
        
        try {
            val config = CloudinaryConfig.getConfigForType(imageType)
            
            // Validar tamaño del archivo
            val fileSize = getFileSize(uri)
            if (fileSize > config.maxSize) {
                trySend(UploadState.Error("El archivo es demasiado grande. Máximo ${config.maxSize / (1024 * 1024)}MB"))
                close()
                return@callbackFlow
            }
            
            // Generar nombre único para el archivo
            val fileName = "${config.folder}${System.currentTimeMillis()}_${UUID.randomUUID()}"
            
            // Configurar opciones de subida con tus presets personalizados
            val options = hashMapOf<String, Any>(
                "upload_preset" to config.uploadPreset,
                "public_id" to fileName,
                "resource_type" to "image",
                "folder" to config.folder
            )
            
            // Realizar la subida
            MediaManager.get().upload(uri)
                .options(options)
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        trySend(UploadState.Loading)
                    }
                    
                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        val progress = (bytes.toFloat() / totalBytes.toFloat()) * 100
                        trySend(UploadState.Progress(progress))
                    }
                    
                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val imageUrl = resultData["secure_url"] as? String
                        if (imageUrl != null) {
                            trySend(UploadState.Success(imageUrl))
                        } else {
                            trySend(UploadState.Error("No se pudo obtener la URL de la imagen"))
                        }
                        close()
                    }
                    
                    override fun onError(requestId: String, error: ErrorInfo) {
                        trySend(UploadState.Error("Error al subir imagen: ${error.description}"))
                        close()
                    }
                    
                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        trySend(UploadState.Error("Reintentando subida..."))
                    }
                })
                .dispatch()
                
        } catch (e: Exception) {
            trySend(UploadState.Error("Error inesperado: ${e.message}"))
            close()
        }
        
        awaitClose { }
    }
    
    /**
     * Obtener el tamaño del archivo en bytes
     */
    private fun getFileSize(uri: Uri): Long {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.available().toLong()
            } ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
    
    /**
     * Validar si el URI es válido
     */
    fun isValidImageUri(uri: Uri): Boolean {
        return try {
            context.contentResolver.openInputStream(uri)?.use { 
                true 
            } ?: false
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Obtener el tipo MIME del archivo
     */
    fun getMimeType(uri: Uri): String? {
        return context.contentResolver.getType(uri)
    }
    
    /**
     * Validar si es una imagen válida
     */
    fun isValidImageType(uri: Uri): Boolean {
        val mimeType = getMimeType(uri)
        return mimeType?.startsWith("image/") == true
    }
}