package com.example.centralis_kotlin.common.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.content.FileProvider
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.centralis_kotlin.common.config.CloudinaryConfig
import com.example.centralis_kotlin.common.config.ImageType
import com.example.centralis_kotlin.common.config.UploadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * Servicio para subir imágenes a Cloudinary
 * Maneja compresión automática, corrección de orientación y recorte inteligente
 */
class CloudinaryService(private val context: Context) {
    
    init {
        // Inicializar MediaManager con configuración
        try {
            val config = hashMapOf<String, Any>(
                "cloud_name" to CloudinaryConfig.CLOUD_NAME,
                "api_key" to CloudinaryConfig.API_KEY,
                "api_secret" to CloudinaryConfig.API_SECRET
            )
            MediaManager.init(context, config)
        } catch (e: Exception) {
            // Ya está inicializado o error de configuración
        }
    }
    
    /**
     * Subir imagen a Cloudinary de forma asíncrona con compresión automática
     * @param uri URI de la imagen seleccionada
     * @param imageType Tipo de imagen (AVATAR, CHAT, ANNOUNCEMENT)
     * @return Flow que emite el estado de la subida
     */
    fun uploadImage(uri: Uri, imageType: ImageType): Flow<UploadState> = callbackFlow {
        trySend(UploadState.Loading)
        
        try {
            val config = CloudinaryConfig.getConfigForType(imageType)
            
            // Comprimir imagen si es necesario
            val finalUri = withContext(Dispatchers.IO) {
                compressImageIfNeeded(uri, config.maxSize, imageType)
            }
            
            // Validar tamaño después de compresión
            val finalFileSize = getFileSize(finalUri)
            if (finalFileSize > config.maxSize) {
                trySend(UploadState.Error("La imagen es demasiado grande incluso después de compresión. Máximo ${config.maxSize / (1024 * 1024)}MB"))
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
            MediaManager.get().upload(finalUri)
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
     * Comprimir imagen si excede el tamaño máximo (con recorte inteligente)
     */
    private suspend fun compressImageIfNeeded(uri: Uri, maxSize: Long, imageType: ImageType): Uri {
        return withContext(Dispatchers.IO) {
            val currentSize = getFileSize(uri)
            
            // Si el archivo ya es menor al límite, usar el original
            if (currentSize <= maxSize) {
                return@withContext uri
            }
            
            try {
                // Leer la imagen original
                val inputStream = context.contentResolver.openInputStream(uri)
                var originalBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                
                if (originalBitmap == null) {
                    return@withContext uri
                }
                
                // Corregir orientación EXIF
                originalBitmap = correctImageOrientation(uri, originalBitmap)
                
                // Dimensiones objetivo basadas en el tipo de imagen
                val (targetWidth, targetHeight) = when (imageType) {
                    ImageType.AVATAR -> Pair(512, 512)      // Avatares cuadrados
                    ImageType.CHAT -> Pair(1024, 768)       // Imágenes de chat 4:3
                    ImageType.ANNOUNCEMENT -> Pair(1200, 900) // Anuncios 4:3
                }
                
                // Procesar imagen: recortar y escalar manteniendo proporciones
                val processedBitmap = cropAndScaleImage(originalBitmap, targetWidth, targetHeight)
                
                // Crear archivo temporal comprimido
                val tempFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
                var quality = 85 // Calidad inicial
                
                // Comprimir iterativamente hasta alcanzar el tamaño deseado
                do {
                    val outputStream = FileOutputStream(tempFile)
                    processedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                    outputStream.flush()
                    outputStream.close()
                    
                    val compressedSize = tempFile.length()
                    
                    if (compressedSize <= maxSize || quality <= 30) {
                        break
                    }
                    
                    quality -= 10 // Reducir calidad gradualmente
                } while (compressedSize > maxSize && quality > 30)
                
                // Limpiar recursos
                originalBitmap.recycle()
                processedBitmap.recycle()
                
                // Retornar URI del archivo comprimido
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    tempFile
                )
                
            } catch (e: Exception) {
                // En caso de error, retornar URI original
                uri
            }
        }
    }
    
    /**
     * Corregir la orientación de la imagen basándose en datos EXIF
     */
    private fun correctImageOrientation(uri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val exif = inputStream?.let { ExifInterface(it) }
            inputStream?.close()
            
            val orientation = exif?.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 
                ExifInterface.ORIENTATION_UNDEFINED
            ) ?: ExifInterface.ORIENTATION_UNDEFINED
            
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                else -> bitmap
            }
        } catch (e: Exception) {
            bitmap // Retornar original si hay error
        }
    }
    
    /**
     * Rotar bitmap según grados especificados
     */
    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    
    /**
     * Recortar y escalar imagen manteniendo proporciones (crop inteligente)
     */
    private fun cropAndScaleImage(originalBitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val originalWidth = originalBitmap.width
        val originalHeight = originalBitmap.height
        
        // Calcular la escala necesaria para cubrir completamente el área objetivo
        val scaleX = targetWidth.toFloat() / originalWidth
        val scaleY = targetHeight.toFloat() / originalHeight
        val scale = if (scaleX > scaleY) scaleX else scaleY // Usar la escala mayor para cubrir completamente
        
        // Nuevas dimensiones escaladas
        val scaledWidth = (originalWidth * scale).toInt()
        val scaledHeight = (originalHeight * scale).toInt()
        
        // Escalar imagen
        val scaledBitmap = Bitmap.createScaledBitmap(
            originalBitmap, scaledWidth, scaledHeight, true
        )
        
        // Calcular coordenadas de recorte para centrar la imagen
        val cropX = if ((scaledWidth - targetWidth) / 2 > 0) (scaledWidth - targetWidth) / 2 else 0
        val cropY = if ((scaledHeight - targetHeight) / 2 > 0) (scaledHeight - targetHeight) / 2 else 0
        
        // Asegurar que no excedamos los límites
        val finalWidth = if (targetWidth < scaledWidth) targetWidth else scaledWidth
        val finalHeight = if (targetHeight < scaledHeight) targetHeight else scaledHeight
        
        // Crear bitmap recortado
        val croppedBitmap = Bitmap.createBitmap(
            scaledBitmap, 
            cropX, 
            cropY, 
            finalWidth, 
            finalHeight
        )
        
        // Limpiar bitmap escalado si es diferente al recortado
        if (scaledBitmap != croppedBitmap) {
            scaledBitmap.recycle()
        }
        
        return croppedBitmap
    }
    
    /**
     * Obtener el tamaño del archivo en bytes (mejorado)
     */
    private fun getFileSize(uri: Uri): Long {
        return try {
            // Intentar obtener el tamaño usando query primero
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
                if (cursor.moveToFirst() && sizeIndex >= 0) {
                    val size = cursor.getLong(sizeIndex)
                    if (size > 0) return size
                }
            }
            
            // Fallback: usar inputStream
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                var totalSize = 0L
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    totalSize += bytesRead
                }
                totalSize
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