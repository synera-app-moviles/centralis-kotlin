package com.example.centralis_kotlin.common.navigation

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.centralis_kotlin.announcement.presentation.view.*
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute?.destination?.route ?: "",
                onNavigate = { route ->
                    navController.navigate(route) {
                        // Evita duplicados al volver a navegar
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.PROFILE,
            modifier = Modifier.padding(paddingValues)
        ) {
            //  Profile
            composable(NavigationRoutes.PROFILE) {
                ProfileView(
                    nav = navController,
                    onLogout = onLogout
                )
            }

            //  Events
            composable(NavigationRoutes.EVENTS) {
                // TODO: EventsView(navController)
            }

            // Chat
            composable(NavigationRoutes.CHAT) {
                // TODO: ChatView(navController)
            }

            //  Announcements (lista principal)
            composable(route = NavigationRoutes.ANNOUNCEMENTS) {
                AnnouncementListScreen(
                    onSelect = { announcement ->
                        navController.navigate("${NavigationRoutes.ANNOUNCEMENT_DETAIL}/${announcement.id}")
                    }
                )
            }

            //  Announcements -> Detalle
            composable("${NavigationRoutes.ANNOUNCEMENT_DETAIL}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                if (id != null) {
                    AnnouncementDetailScreen(
                        announcementId = id,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            //  Announcements -> Crear
            composable(NavigationRoutes.ANNOUNCEMENT_CREATE) {
                CreateAnnouncementScreen(
                    onCreated = { navController.popBackStack() }
                )
            }
        }
    }
}
