package com.example.centralis_kotlin.common.services

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class DeviceTokenManager(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("centralis_prefs", Context.MODE_PRIVATE)
    
    suspend fun getCurrentToken(): String? {
        return try {
            // Verificar si Firebase está inicializado
            if (!isFirebaseInitialized()) {
                Log.w("DeviceTokenManager", "⚠️ Firebase no está inicializado - usando token mock para desarrollo")
                val mockToken = "mock_token_${System.currentTimeMillis()}"
                saveTokenLocally(mockToken)
                return mockToken
            }
            
            val token = FirebaseMessaging.getInstance().token.await()
            Log.d("DeviceTokenManager", "FCM Token obtenido: $token")
            
            // Guardar token localmente
            saveTokenLocally(token)
            
            token
        } catch (e: Exception) {
            Log.e("DeviceTokenManager", "Error obteniendo FCM token", e)
            
            // En caso de error, generar token mock para desarrollo
            val mockToken = "mock_token_error_${System.currentTimeMillis()}"
            saveTokenLocally(mockToken)
            Log.w("DeviceTokenManager", "🔧 Usando token mock para desarrollo: $mockToken")
            mockToken
        }
    }
    
    private fun isFirebaseInitialized(): Boolean {
        return try {
            com.google.firebase.FirebaseApp.getInstance()
            true
        } catch (e: IllegalStateException) {
            false
        }
    }
    
    fun saveTokenLocally(token: String) {
        sharedPreferences.edit()
            .putString(KEY_FCM_TOKEN, token)
            .putLong(KEY_TOKEN_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }
    
    fun getStoredToken(): String? {
        return sharedPreferences.getString(KEY_FCM_TOKEN, null)
    }
    
    fun isTokenExpired(): Boolean {
        val timestamp = sharedPreferences.getLong(KEY_TOKEN_TIMESTAMP, 0)
        val currentTime = System.currentTimeMillis()
        val dayInMs = 24 * 60 * 60 * 1000L
        
        return (currentTime - timestamp) > dayInMs
    }
    
    fun clearToken() {
        sharedPreferences.edit()
            .remove(KEY_FCM_TOKEN)
            .remove(KEY_TOKEN_TIMESTAMP)
            .apply()
    }
    
    companion object {
        private const val KEY_FCM_TOKEN = "fcm_token"
        private const val KEY_TOKEN_TIMESTAMP = "token_timestamp"
    }
}