package com.example.centralis_kotlin.announcement.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedAnnouncementEntity::class], version = 1)
abstract class AnnouncementDatabase : RoomDatabase() {
    abstract fun savedAnnouncementDao(): SavedAnnouncementDao
}
