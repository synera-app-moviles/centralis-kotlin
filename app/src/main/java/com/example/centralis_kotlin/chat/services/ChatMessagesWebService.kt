package com.example.centralis_kotlin.chat.services

import com.example.centralis_kotlin.chat.domain.models.*
import retrofit2.Response
import retrofit2.http.*

interface ChatMessagesWebService {

    @GET("api/v1/groups/{groupId}/messages/{messageId}")
    suspend fun getMessageById(
        @Path("groupId") groupId: String,
        @Path("messageId") messageId: String,
        @Header("Authorization") token: String
    ): Response<MessageResponse>

    @PUT("api/v1/groups/{groupId}/messages/{messageId}")
    suspend fun updateMessageBody(
        @Path("groupId") groupId: String,
        @Path("messageId") messageId: String,
        @Body request: UpdateMessageBodyRequest,
        @Header("Authorization") token: String
    ): Response<MessageResponse>

    @DELETE("api/v1/groups/{groupId}/messages/{messageId}")
    suspend fun deleteMessage(
        @Path("groupId") groupId: String,
        @Path("messageId") messageId: String,
        @Header("Authorization") token: String
    ): Response<Unit>

    @GET("api/v1/groups/{groupId}/messages")
    suspend fun getMessagesByGroup(
        @Path("groupId") groupId: String,
        @Header("Authorization") token: String
    ): Response<List<MessageResponse>>

    @POST("api/v1/groups/{groupId}/messages")
    suspend fun createMessage(
        @Path("groupId") groupId: String,
        @Body request: CreateMessageRequest,
        @Header("Authorization") token: String
    ): Response<MessageResponse>

    @PATCH("api/v1/groups/{groupId}/messages/{messageId}/status")
    suspend fun updateMessageStatus(
        @Path("groupId") groupId: String,
        @Path("messageId") messageId: String,
        @Body request: UpdateMessageStatusRequest,
        @Header("Authorization") token: String
    ): Response<MessageResponse>

    @GET("api/v1/groups/messages/status/{status}")
    suspend fun getMessagesByStatus(
        @Path("status") status: MessageStatus,
        @Header("Authorization") token: String
    ): Response<List<MessageResponse>>
}