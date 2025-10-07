package com.example.centralis_kotlin.common.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.centralis_kotlin.common.components.BottomNavigationBar
import com.example.centralis_kotlin.profile.presentation.views.ProfileView
import com.example.centralis_kotlin.notification.components.NotificationScreen

@Composable
fun MainNavigation(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute?.destination?.route ?: "",
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.NOTIFICATIONS,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavigationRoutes.PROFILE) { 
                ProfileView(
                    nav = navController,
                    onLogout = onLogout
                )
            }
            composable(NavigationRoutes.EVENTS) { 
                // TODO: EventsView(navController) 
            }
            composable(NavigationRoutes.CHAT) { 
                // TODO: ChatView(navController) 
            }
            composable(NavigationRoutes.ANNOUNCEMENTS) { 
                // TODO: AnnouncementsView(navController) 
            }
            composable(NavigationRoutes.NOTIFICATIONS) {
                NotificationScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}