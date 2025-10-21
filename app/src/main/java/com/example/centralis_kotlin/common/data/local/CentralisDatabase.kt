package com.example.centralis_kotlin.common.data.local

import android.content.Context
import androidx.room.*
import com.example.centralis_kotlin.common.data.local.entities.NotificationEntity
import com.example.centralis_kotlin.common.data.local.daos.NotificationDao

@Database(
    entities = [NotificationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CentralisDatabase : RoomDatabase() {
    
    abstract fun notificationDao(): NotificationDao
    
    companion object {
        @Volatile
        private var INSTANCE: CentralisDatabase? = null
        
        fun getDatabase(context: Context): CentralisDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CentralisDatabase::class.java,
                    "centralis_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}