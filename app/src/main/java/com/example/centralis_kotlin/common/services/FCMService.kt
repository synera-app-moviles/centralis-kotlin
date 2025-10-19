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
        
        Log.d("FCMService", "FCM Message received: ${remoteMessage.data}")
        
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
        Log.d("FCMService", "New FCM Token: $token")
        
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
                
                val notification = NotificationEntity(
                    id = remoteMessage.messageId ?: System.currentTimeMillis().toString(),
                    userId = remoteMessage.data["userId"] ?: "unknown",
                    title = remoteMessage.notification?.title ?: "Sin título",
                    message = remoteMessage.notification?.body ?: "Sin mensaje",
                    type = remoteMessage.data["type"] ?: "GENERAL",
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    priority = remoteMessage.data["priority"]?.toIntOrNull() ?: 0,
                    fcmMessageId = remoteMessage.messageId,
                    imageUrl = remoteMessage.notification?.imageUrl?.toString(),
                    actionUrl = remoteMessage.data["actionUrl"]
                )
                
                repository.insertNotification(notification)
                Log.d("FCMService", "✅ Notificación guardada en Room Database")
                
            } catch (e: Exception) {
                Log.e("FCMService", "❌ Error guardando notificación en database", e)
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
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notificaciones Centralis",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para notificaciones de la app Centralis"
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun sendTokenToServer(token: String) {
        serviceScope.launch {
            try {
                Log.d("FCMService", "📤 Token a enviar al backend: $token")

                // Intentar registrar el token usando el cliente Retrofit central
                val userId = com.example.centralis_kotlin.common.SharedPreferencesManager(this@FCMService).getUserId()
                if (userId.isNullOrEmpty()) {
                    Log.w("FCMService", "No hay userId disponible, posponer registro del token")
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
                val response = api.registerFCMToken(userId, request, "Bearer $authToken")
                if (response.isSuccessful) {
                    Log.d("FCMService", "✅ Token registrado en backend para userId=$userId")
                } else {
                    Log.e("FCMService", "❌ Registro de token falló: ${response.code()} - ${response.errorBody()?.string()}")
                }
                
            } catch (e: Exception) {
                Log.e("FCMService", "❌ Error enviando token al servidor", e)
            }
        }
    }
    
    companion object {
        private const val CHANNEL_ID = "centralis_notifications"
    }
}