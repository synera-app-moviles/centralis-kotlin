package com.example.centralis_kotlin.common.config

import com.cloudinary.android.MediaManager
import android.content.Context

object CloudinaryConfig {
    // Credenciales de Cloudinary (cambiar por las reales)
    const val CLOUD_NAME = "dpprgycup"
    const val API_KEY = "454726488133594"
    const val API_SECRET = "G1TYaQ8qxvFzqnO5pogYhr-nFFQ"
    
    // Configuraciones por tipo de imagen - Ahora usando tus presets personalizados
    const val AVATAR_UPLOAD_PRESET = "centralis_avatars"
    const val CHAT_UPLOAD_PRESET = "centralis_chat"
    const val ANNOUNCEMENT_UPLOAD_PRESET = "centralis_announcements"
    
    // Carpetas organizadas por función
    const val AVATAR_FOLDER = "avatars/"
    const val CHAT_FOLDER = "chat/"
    const val ANNOUNCEMENT_FOLDER = "announcements/"
    
    // Configuraciones de transformación
    const val AVATAR_SIZE = "c_fill,w_200,h_200,r_max"
    const val CHAT_SIZE = "c_fit,w_800,h_600,q_auto"
    const val ANNOUNCEMENT_SIZE = "c_fit,w_1200,h_800,q_auto"
    
    // Tamaños máximos en bytes
    const val AVATAR_MAX_SIZE = 1024L * 1024L // 1MB
    const val CHAT_MAX_SIZE = 5L * 1024L * 1024L // 5MB
    const val ANNOUNCEMENT_MAX_SIZE = 10L * 1024L * 1024L // 10MB
    
    private var isInitialized = false
    
    /**
     * Inicializar Cloudinary MediaManager
     */
    fun initialize(context: Context) {
        if (!isInitialized) {
            val config = hashMapOf(
                "cloud_name" to CLOUD_NAME,
                "api_key" to API_KEY,
                "api_secret" to API_SECRET
            )
            MediaManager.init(context, config)
            isInitialized = true
        }
    }
    
    /**
     * Obtener configuración según el tipo de imagen
     */
    fun getConfigForType(imageType: ImageType): ImageConfig {
        return when (imageType) {
            ImageType.AVATAR -> ImageConfig(
                uploadPreset = AVATAR_UPLOAD_PRESET,
                folder = AVATAR_FOLDER,
                transformation = AVATAR_SIZE,
                maxSize = AVATAR_MAX_SIZE
            )
            ImageType.CHAT -> ImageConfig(
                uploadPreset = CHAT_UPLOAD_PRESET,
                folder = CHAT_FOLDER,
                transformation = CHAT_SIZE,
                maxSize = CHAT_MAX_SIZE
            )
            ImageType.ANNOUNCEMENT -> ImageConfig(
                uploadPreset = ANNOUNCEMENT_UPLOAD_PRESET,
                folder = ANNOUNCEMENT_FOLDER,
                transformation = ANNOUNCEMENT_SIZE,
                maxSize = ANNOUNCEMENT_MAX_SIZE
            )
        }
    }
}

/**
 * Enum para los diferentes tipos de imagen
 */
enum class ImageType {
    AVATAR,
    CHAT,
    ANNOUNCEMENT
}

/**
 * Configuración específica para cada tipo de imagen
 */
data class ImageConfig(
    val uploadPreset: String,
    val folder: String,
    val transformation: String,
    val maxSize: Long
)

/**
 * Estados de la subida de imagen
 */
sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    data class Progress(val percentage: Float) : UploadState()
    data class Success(val imageUrl: String) : UploadState()
    data class Error(val message: String) : UploadState()
}