package com.example.centralis_kotlin.analytics.service

import android.util.Log
import com.example.centralis_kotlin.analytics.model.ViewRequest
import com.example.centralis_kotlin.common.RetrofitClient
import java.util.concurrent.ConcurrentHashMap

class AnalyticsService {
    private val api: AnalyticsApi = RetrofitClient.retrofitInstance.create(AnalyticsApi::class.java)
    
    // Cache local para evitar registros duplicados en la misma sesión
    private val viewedContent = ConcurrentHashMap<String, Boolean>()
    
    // Generar clave única para el cache: "userId|contentType|contentId"
    private fun getCacheKey(userId: String, contentType: String, contentId: String): String {
        return "$userId|$contentType|$contentId"
    }
    
    suspend fun registerAnnouncementView(
        authorization: String,
        announcementId: String,
        userId: String
    ) {
        val cacheKey = getCacheKey(userId, "announcement", announcementId)
        
        // Verificar si ya fue visto en esta sesión
        if (viewedContent.containsKey(cacheKey)) {
            Log.d("AnalyticsService", "Announcement $announcementId already viewed by user $userId in this session - skipping")
            return
        }
        
        try {
            val viewRequest = ViewRequest(userId = userId)
            val response = api.registerAnnouncementView(authorization, announcementId, viewRequest)
            
            if (response.isSuccessful) {
                val viewResponse = response.body()
                if (viewResponse?.isNewView == true) {
                    // Solo marcar como visto si es realmente una nueva vista
                    viewedContent[cacheKey] = true
                    Log.d("AnalyticsService", "NEW Announcement view registered: ${viewResponse.message}")
                } else {
                    // Era vista duplicada en backend, marcar en cache para evitar futuras llamadas
                    viewedContent[cacheKey] = true
                    Log.d("AnalyticsService", "DUPLICATE Announcement view: ${viewResponse?.message}")
                }
            } else {
                Log.w("AnalyticsService", "Failed to register announcement view: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("AnalyticsService", "Error registering announcement view", e)
        }
    }
    
    suspend fun registerEventView(
        authorization: String,
        eventId: String,
        userId: String
    ) {
        val cacheKey = getCacheKey(userId, "event", eventId)
        
        // Verificar si ya fue visto en esta sesión
        if (viewedContent.containsKey(cacheKey)) {
            Log.d("AnalyticsService", "Event $eventId already viewed by user $userId in this session - skipping")
            return
        }
        
        try {
            val viewRequest = ViewRequest(userId = userId)
            val response = api.registerEventView(authorization, eventId, viewRequest)
            
            if (response.isSuccessful) {
                val viewResponse = response.body()
                if (viewResponse?.isNewView == true) {
                    // Solo marcar como visto si es realmente una nueva vista
                    viewedContent[cacheKey] = true
                    Log.d("AnalyticsService", "NEW Event view registered: ${viewResponse.message}")
                } else {
                    // Era vista duplicada en backend, marcar en cache para evitar futuras llamadas
                    viewedContent[cacheKey] = true
                    Log.d("AnalyticsService", "DUPLICATE Event view: ${viewResponse?.message}")
                }
            } else {
                Log.w("AnalyticsService", "Failed to register event view: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("AnalyticsService", "Error registering event view", e)
        }
    }
    
    // Método para limpiar cache (útil para testing o cambio de usuario)
    fun clearViewCache() {
        viewedContent.clear()
        Log.d("AnalyticsService", "View cache cleared")
    }
}