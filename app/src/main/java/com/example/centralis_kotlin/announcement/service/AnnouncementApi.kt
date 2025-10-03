package com.example.centralis_kotlin.announcement.service

import com.example.centralis_kotlin.announcement.model.dto.AnnouncementDto
import com.example.centralis_kotlin.announcement.model.dto.CommentDto
import com.example.centralis_kotlin.announcement.model.dto.CreateCommentDto
import retrofit2.http.*

interface AnnouncementApi {

    @GET("api/v1/announcements")
    suspend fun getAnnouncements(): List<AnnouncementDto>

    @GET("api/v1/announcements/{id}")
    suspend fun getAnnouncementById(@Path("id") id: String): AnnouncementDto

    @POST("api/v1/announcements")
    suspend fun createAnnouncement(@Body announcement: AnnouncementDto): AnnouncementDto

    @DELETE("api/v1/announcements/{id}")
    suspend fun deleteAnnouncement(@Path("id") id: String)

    // Comments
    @GET("api/v1/announcements/{announcementId}/comments")
    suspend fun getComments(@Path("announcementId") announcementId: String): List<CommentDto>

    // NOTE: aquí aceptamos CreateCommentDto (request) y devolvemos CommentDto (response)
    @POST("api/v1/announcements/{announcementId}/comments")
    suspend fun addComment(
        @Path("announcementId") announcementId: String,
        @Body comment: CreateCommentDto
    ): CommentDto
}
