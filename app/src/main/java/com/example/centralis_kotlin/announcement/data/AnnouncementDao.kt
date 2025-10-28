package com.example.centralis_kotlin.announcement.data

import androidx.room.*

@Dao
interface AnnouncementDao {
    @Query("SELECT * FROM announcements")
    suspend fun getAll(): List<AnnouncementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(announcement: AnnouncementEntity)

    @Delete
    suspend fun delete(announcement: AnnouncementEntity)
}