package com.example.centralis_kotlin.announcement.local

import androidx.room.*

@Dao
interface SavedAnnouncementDao {
    @Query("SELECT * FROM saved_announcements")
    suspend fun getAll(): List<SavedAnnouncementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(announcement: SavedAnnouncementEntity)

    @Delete
    suspend fun delete(announcement: SavedAnnouncementEntity)

    @Query("DELETE FROM saved_announcements WHERE id = :id")
    suspend fun deleteById(id: String)
}
