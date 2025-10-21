package com.example.centralis_kotlin.common.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.centralis_kotlin.MainActivity
import com.example.centralis_kotlin.R
import com.example.centralis_kotlin.common.di.DependencyFactory
import com.example.centralis_kotlin.common.data.local.entities.NotificationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FCMService : FirebaseMessagingService() {
    
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        // Guardar notificación en Room Database
        saveNotificationToDatabase(remoteMessage)
        
        // Mostrar notificación visual
        showNotification(
            title = remoteMessage.notification?.title ?: "Nueva notificación",
            message = remoteMessage.notification?.body ?: "Tienes una nueva notificación"
        )
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        
        // Guardar token localmente
        val tokenManager = DependencyFactory.getDeviceTokenManager(this)
        tokenManager.saveTokenLocally(token)
        
        // Aquí podrías enviar el token al backend
        sendTokenToServer(token)
    }
    
    private fun saveNotificationToDatabase(remoteMessage: RemoteMessage) {
        serviceScope.launch {
            try {
                val repository = DependencyFactory.getNotificationRepository(this@FCMService)
                
                // Obtener el userId actual del usuario logueado
                val sharedPrefs = com.example.centralis_kotlin.common.SharedPreferencesManager(this@FCMService)
                val currentUserId = sharedPrefs.getUserId() ?: "unknown"
                
                val notification = NotificationEntity(
                    id = remoteMessage.messageId ?: "fcm-${System.currentTimeMillis()}",
                    userId = currentUserId, // Usar el userId del usuario actual logueado
                    title = remoteMessage.notification?.title ?: "Sin título",
                    message = remoteMessage.notification?.body ?: "Sin mensaje",
                    type = remoteMessage.data["type"] ?: "FCM",
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    priority = when (remoteMessage.data["priority"]?.uppercase()) {
                        "HIGH", "URGENT" -> 2
                        "MEDIUM" -> 1
                        else -> 0
                    },
                    fcmMessageId = remoteMessage.messageId,
                    imageUrl = remoteMessage.notification?.imageUrl?.toString(),
                    actionUrl = remoteMessage.data["actionUrl"]
                )
                
                repository.insertNotification(notification)
                
            } catch (e: Exception) {
                // Error silencioso - la notificación al menos se muestra en el sistema
            }
        }
    }
    
    private fun showNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)  // Ícono temporal del sistema
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notificaciones Centralis",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para notificaciones de la app Centralis"
                enableLights(true)
                lightColor = android.graphics.Color.BLUE
                enableVibration(true)
                setShowBadge(true)
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun sendTokenToServer(token: String) {
        serviceScope.launch {
            try {
                // Intentar registrar el token usando el cliente Retrofit central
                val userId = com.example.centralis_kotlin.common.SharedPreferencesManager(this@FCMService).getUserId()
                if (userId.isNullOrEmpty()) {
                    return@launch
                }

                val api = com.example.centralis_kotlin.common.RetrofitClient.fcmApiService
                val request = com.example.centralis_kotlin.common.network.FcmTokenRequest(
                    fcmToken = token,
                    deviceType = "Android",
                    deviceId = android.provider.Settings.Secure.getString(this@FCMService.contentResolver, android.provider.Settings.Secure.ANDROID_ID)
                )

                val sharedPrefs = com.example.centralis_kotlin.common.SharedPreferencesManager(this@FCMService)
                val authToken = sharedPrefs.getToken() ?: ""
                api.registerFCMToken(userId, request, "Bearer $authToken")
                
            } catch (e: Exception) {
                // Error silencioso - el token se reintentará en el siguiente login
            }
        }
    }
    
    companion object {
        private const val CHANNEL_ID = "centralis_notifications"
    }
}