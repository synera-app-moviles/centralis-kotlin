package com.example.centralis_kotlin.announcement.service

import com.example.centralis_kotlin.announcement.model.Announcement
import com.example.centralis_kotlin.announcement.model.Comment
import java.util.UUID

interface IAnnouncementRepository {
    fun getAll(): List<Announcement>
    fun getById(id: String): Announcement?
    fun save(announcement: Announcement): Announcement
    fun delete(id: String)
    suspend fun getAnnouncements(): List<Announcement>
    suspend fun getAnnouncementById(id: String): Announcement?

    suspend fun addComment(announcementId: String, content: String, employeeId: String): Comment

}
