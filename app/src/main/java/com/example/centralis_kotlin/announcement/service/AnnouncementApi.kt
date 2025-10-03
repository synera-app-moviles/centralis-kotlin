package com.example.centralis_kotlin.announcement.service

import com.example.centralis_kotlin.announcement.model.dto.AnnouncementDto
import com.example.centralis_kotlin.announcement.model.dto.CommentDto
import retrofit2.http.*

interface AnnouncementApi {

    @GET("announcements")
    suspend fun getAnnouncements(): List<AnnouncementDto>

    @GET("announcements/{id}")
    suspend fun getAnnouncementById(@Path("id") id: String): AnnouncementDto

    @POST("announcements")
    suspend fun createAnnouncement(@Body announcement: AnnouncementDto): AnnouncementDto

    @DELETE("announcements/{id}")
    suspend fun deleteAnnouncement(@Path("id") id: String)

    // Comments
    @GET("announcements/{announcementId}/comments")
    suspend fun getComments(@Path("announcementId") announcementId: String): List<CommentDto>

    @POST("announcements/{announcementId}/comments")
    suspend fun addComment(
        @Path("announcementId") announcementId: String,
        @Body comment: CommentDto
    ): CommentDto
}
