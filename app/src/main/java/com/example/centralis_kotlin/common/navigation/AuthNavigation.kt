package com.example.centralis_kotlin.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.centralis_kotlin.iam.presentation.views.SignInView
import com.example.centralis_kotlin.iam.presentation.views.SignUpView

@Composable
fun AuthNavigation(onLoginSuccess: () -> Unit) {
    val authNavController = rememberNavController()
    
    NavHost(
        navController = authNavController,
        startDestination = "SignInView"
    ) {
        composable("SignInView") { 
            SignInView(
                nav = authNavController,
                onLoginSuccess = onLoginSuccess
            )
        }
        composable("SignUpView") { 
            SignUpView(
                nav = authNavController,
                onSignUpSuccess = onLoginSuccess // Ir directamente a la aplicación después del registro
            )
        }

    }
}