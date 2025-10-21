package com.example.centralis_kotlin.common.di

import android.content.Context
import com.example.centralis_kotlin.common.data.local.CentralisDatabase
import com.example.centralis_kotlin.common.data.repository.NotificationRepository
import com.example.centralis_kotlin.notification.presentation.viewmodels.NotificationViewModel
import com.example.centralis_kotlin.common.services.DeviceTokenManager

/**
 * Factory para crear dependencias manualmente (sin Hilt)
 * Patrón Singleton simple con lazy initialization
 */
object DependencyFactory {
    
    @Volatile
    private var database: CentralisDatabase? = null
    
    @Volatile
    private var notificationRepository: NotificationRepository? = null
    
    @Volatile
    private var deviceTokenManager: DeviceTokenManager? = null
    
    fun getDatabase(context: Context): CentralisDatabase {
        return database ?: synchronized(this) {
            database ?: CentralisDatabase.getDatabase(context.applicationContext)
                .also { database = it }
        }
    }
    
    fun getNotificationRepository(context: Context): NotificationRepository {
        return notificationRepository ?: synchronized(this) {
            notificationRepository ?: NotificationRepository(
                notificationDao = getDatabase(context).notificationDao()
            ).also { notificationRepository = it }
        }
    }
    
    fun getDeviceTokenManager(context: Context): DeviceTokenManager {
        return deviceTokenManager ?: synchronized(this) {
            deviceTokenManager ?: DeviceTokenManager(context.applicationContext)
                .also { deviceTokenManager = it }
        }
    }
    
    fun createNotificationViewModel(context: Context): NotificationViewModel {
        val sharedPrefs = com.example.centralis_kotlin.common.SharedPreferencesManager(context)
        val userId = sharedPrefs.getUserId() ?: "test-user-123"
        return NotificationViewModel(
            notificationRepository = getNotificationRepository(context),
            currentUserId = userId,
            context = context
        )
    }
    
    /**
     * Limpia instancias (útil para testing)
     */
    fun clearInstances() {
        database = null
        notificationRepository = null
        deviceTokenManager = null
    }
}