package com.example.centralis_kotlin.common.navigation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.centralis_kotlin.common.di.DependencyFactory
import com.example.centralis_kotlin.common.data.local.entities.NotificationEntity
import com.example.centralis_kotlin.common.SharedPreferencesManager
import kotlinx.coroutines.launch

@Composable
fun CentralisApp() {
    val navController = rememberNavController()
    var isLoggedIn by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Función para manejar login exitoso
    val handleLoginSuccess = {
        isLoggedIn = true
        
        // Obtener FCM token DESPUÉS del login
        scope.launch {
            try {
                val tokenManager = DependencyFactory.getDeviceTokenManager(context)
                val token = tokenManager.getCurrentToken()
                
                if (token != null) {
                    Log.d("CentralisApp", "✅ FCM Token obtenido después del login: $token")
                    
                    // Enviar el token al backend con el userId del usuario logueado
                    val sharedPrefs = SharedPreferencesManager(context)
                    val userId = sharedPrefs.getUserId()
                    
                    if (!userId.isNullOrEmpty()) {
                        try {
                            val api = com.example.centralis_kotlin.common.RetrofitClient.fcmApiService
                            val request = com.example.centralis_kotlin.common.network.FcmTokenRequest(
                                fcmToken = token,
                                deviceType = "Android",
                                deviceId = android.provider.Settings.Secure.getString(context.contentResolver, android.provider.Settings.Secure.ANDROID_ID)
                            )
                            
                            val authToken = sharedPrefs.getToken() ?: ""
                            Log.d("CentralisApp", "📤 Enviando token al backend para userId=$userId")
                            val response = api.registerFCMToken(userId, request, "Bearer $authToken")
                            if (response.isSuccessful) {
                                Log.d("CentralisApp", "✅ Token registrado automáticamente en backend")
                            } else {
                                Log.e("CentralisApp", "❌ Error registrando token automáticamente: ${response.code()}")
                            }
                        } catch (e: Exception) {
                            Log.e("CentralisApp", "❌ Excepción registrando token automáticamente", e)
                        }
                    }
                    
                } else {
                    Log.w("CentralisApp", "⚠️ No se pudo obtener FCM token después del login")
                }

                
            } catch (e: Exception) {
                Log.e("CentralisApp", "❌ Error obteniendo FCM token después del login", e)
            }
        }
        Unit // Explicitly return Unit
    }
    
    if (isLoggedIn) {
        MainNavigation(
            onLogout = { 
                isLoggedIn = false
                // TODO: Limpiar FCM token del backend al hacer logout
            }
        )
    } else {
        AuthNavigation(
            onLoginSuccess = handleLoginSuccess
        )
    }
}