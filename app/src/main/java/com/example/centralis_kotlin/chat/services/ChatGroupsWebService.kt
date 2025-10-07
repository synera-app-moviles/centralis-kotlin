package com.example.centralis_kotlin.chat.services

import com.example.centralis_kotlin.chat.domain.models.*
import retrofit2.Response
import retrofit2.http.*


interface ChatGroupsWebService {

    @GET("api/v1/groups/{groupId}")
    suspend fun getGroupById(
        @Path("groupId") groupId: String,
        @Header("Authorization") token: String
    ): Response<GroupResponse>

    @PUT("api/v1/groups/{groupId}")
    suspend fun updateGroup(
        @Path("groupId") groupId: String,
        @Body request: UpdateGroupRequest,
        @Header("Authorization") token: String
    ): Response<GroupResponse>

    @DELETE("api/v1/groups/{groupId}")
    suspend fun deleteGroup(
        @Path("groupId") groupId: String,
        @Header("Authorization") token: String
    ): Response<Unit>

    @GET("api/v1/groups")
    suspend fun getGroupsByUser(
        @Query("userId") userId: String,
        @Header("Authorization") token: String
    ): Response<List<GroupResponse>>
    @POST("api/v1/groups")
    suspend fun createGroup(
        @Body request: CreateGroupRequest,
        @Header("Authorization") token: String
    ): Response<GroupResponse>

    @POST("api/v1/groups/{groupId}/members")
    suspend fun addMembers(
        @Path("groupId") groupId: String,
        @Body request: AddMembersRequest,
        @Header("Authorization") token: String
    ): Response<GroupResponse>

    @DELETE("api/v1/groups/{groupId}/members")
    suspend fun removeMember(
        @Path("groupId") groupId: String,
        @Query("userId") userId: String,
        @Header("Authorization") token: String
    ): Response<GroupResponse>

    @PATCH("api/v1/groups/{groupId}/visibility")
    suspend fun updateVisibility(
        @Path("groupId") groupId: String,
        @Body request: UpdateVisibilityRequest,
        @Header("Authorization") token: String
    ): Response<GroupResponse>

    @GET("api/v1/groups/visibility/{visibility}")
    suspend fun getGroupsByVisibility(
        @Path("visibility") visibility: GroupVisibility,
        @Header("Authorization") token: String
    ): Response<List<GroupResponse>>
}