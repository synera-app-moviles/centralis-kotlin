package com.example.centralis_kotlin.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.centralis_kotlin.iam.presentation.views.SignInView
import com.example.centralis_kotlin.iam.presentation.views.SignUpProfile
import com.example.centralis_kotlin.iam.presentation.views.SignUpView
import com.example.centralis_kotlin.profile.presentation.views.ProfileView

@Composable
fun AppNavigation(){
    // Crear y recordar el controlador de navegación
    val recordarPantalla = rememberNavController()

    // Configurar el NavHost con las rutas
    NavHost(navController = recordarPantalla, startDestination = "SignInView"){


        composable ("SignUpView"){ SignUpView(recordarPantalla) }
        composable ("SignInView"){ SignInView(recordarPantalla) }
        composable ("SignUpProfile") {  SignUpProfile(recordarPantalla)  }


        composable ("ProfileView") { ProfileView(recordarPantalla)}
    }
}