package com.example.centralis_kotlin.common

import com.example.centralis_kotlin.chat.services.ChatGroupsWebService
import com.example.centralis_kotlin.chat.services.ChatMessagesWebService
import com.example.centralis_kotlin.chat.services.ChatImagesApi
import com.example.centralis_kotlin.iam.services.IAMWebService
import com.example.centralis_kotlin.profile.services.ProfileWebService
import kotlin.getValue
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.centralis_kotlin.common.network.FCMApiService
import com.example.centralis_kotlin.common.network.NotificationApiService
import com.example.centralis_kotlin.events.service.EventApiService
import kotlin.getValue


object RetrofitClient {

    // Cliente Retrofit unificado (reutilizable para ambos contextos)
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://web-services-okt8.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    // Servicio IAM (Autenticación)
    val iamWebService: IAMWebService by lazy {
        retrofit.create(IAMWebService::class.java)
    }
    
    // Servicio Profile (Perfiles)
    val profileWebService: ProfileWebService by lazy {
        retrofit.create(ProfileWebService::class.java)
    }

    val chatMessagesWebService: ChatMessagesWebService by lazy {
        retrofit.create(ChatMessagesWebService::class.java)
    }

    val chatGroupsWebService : ChatGroupsWebService by lazy {
        retrofit.create(ChatGroupsWebService::class.java)
    }

    val chatImagesApi: ChatImagesApi by lazy {
        retrofit.create(ChatImagesApi::class.java)
    }

    val retrofitInstance: Retrofit
        get() = retrofit

    val fcmApiService: FCMApiService by lazy {
        retrofit.create(FCMApiService::class.java)
    }
    
    val notificationApiService: NotificationApiService by lazy {
        retrofit.create(NotificationApiService::class.java)
    }

    val eventApiService: EventApiService by lazy {
        retrofit.create(EventApiService::class.java)
    }

}