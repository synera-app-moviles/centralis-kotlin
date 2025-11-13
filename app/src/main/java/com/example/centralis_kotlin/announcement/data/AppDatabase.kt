package com.example.centralis_kotlin.announcement.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AnnouncementEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "centralis_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}