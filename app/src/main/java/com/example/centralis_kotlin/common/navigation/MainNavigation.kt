package com.example.centralis_kotlin.common.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.centralis_kotlin.chat.presentation.views.ChatDetailView
import com.example.centralis_kotlin.chat.presentation.views.ChatView
import com.example.centralis_kotlin.chat.presentation.views.CreateGroupView
import com.example.centralis_kotlin.common.components.BottomNavigationBar
import com.example.centralis_kotlin.profile.presentation.views.ProfileView

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
            startDestination = NavigationRoutes.PROFILE,
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
                ChatView(
                    nav = navController,
                    onNewChat = { /* navega a 'createChat' si luego lo agregas */ },
                    onOpenChat = { chat -> /* navega a ChatDetail con chat.id */ }
                )
            }
            composable(NavigationRoutes.CHAT_CREATE) { CreateGroupView(navController) }



            composable(
               NavigationRoutes.CHAT_DETAIL,
                arguments = listOf(navArgument("chatId") {
                    type = NavType.StringType
                    nullable = false
                })
            ) { backStackEntry ->
                val chatId = backStackEntry.arguments
                    ?.getString("chatId") ?: ""

                ChatDetailView(
                    nav = navController,
                    chatId = chatId
                )
            }



            composable(NavigationRoutes.ANNOUNCEMENTS) {
                // TODO: AnnouncementsView(navController) 
            }
        }
    }
}