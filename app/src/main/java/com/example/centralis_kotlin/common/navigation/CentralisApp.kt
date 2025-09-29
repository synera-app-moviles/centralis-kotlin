package com.example.centralis_kotlin.common.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController

@Composable
fun CentralisApp() {
    val navController = rememberNavController()
    var isLoggedIn by remember { mutableStateOf(false) }
    
    if (isLoggedIn) {
        MainNavigation(
            onLogout = { isLoggedIn = false }
        )
    } else {
        AuthNavigation(
            onLoginSuccess = { isLoggedIn = true }
        )
    }
}