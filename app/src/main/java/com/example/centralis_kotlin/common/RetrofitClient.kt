package com.example.centralis_kotlin.common

import com.example.centralis_kotlin.iam.services.IAMWebService
import com.example.centralis_kotlin.profile.services.ProfileWebService
import kotlin.getValue
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    // Cliente Retrofit unificado (reutilizable para ambos contextos)
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://web-services-yezo.onrender.com/")
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

    val retrofitInstance: Retrofit
        get() = retrofit
}