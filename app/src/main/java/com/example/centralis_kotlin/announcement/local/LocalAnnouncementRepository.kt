package com.example.centralis_kotlin.announcement.local

import android.content.Context
import androidx.room.Room

class LocalAnnouncementRepository(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AnnouncementDatabase::class.java,
        "announcements_db"
    ).build()

    private val dao = db.savedAnnouncementDao()

    suspend fun getAll() = dao.getAll()

    suspend fun save(announcement: SavedAnnouncementEntity) {
        dao.insert(announcement)
    }

    suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }
}
