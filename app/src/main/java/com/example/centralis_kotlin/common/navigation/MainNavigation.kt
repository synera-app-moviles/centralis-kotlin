package com.example.centralis_kotlin.common.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
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
import com.example.centralis_kotlin.events.presentation.views.AppNavGraph
import com.example.centralis_kotlin.profile.presentation.views.ProfileView
import com.example.centralis_kotlin.notification.presentation.screens.NotificationScreen
import com.example.centralis_kotlin.common.di.DependencyFactory
import com.example.centralis_kotlin.announcement.presentation.view.*
import com.example.centralis_kotlin.chat.presentation.views.ChatDetailView
import com.example.centralis_kotlin.chat.presentation.views.ChatView
import com.example.centralis_kotlin.chat.presentation.views.CreateGroupView

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
            startDestination = NavigationRoutes.ANNOUNCEMENTS,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Profile
            composable(NavigationRoutes.PROFILE) {
                ProfileView(
                    nav = navController,
                    onLogout = onLogout
                )
            }
            // Events
            composable(NavigationRoutes.EVENTS) {
                val eventsNavController = rememberNavController()
                AppNavGraph(navController = eventsNavController)
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


            // Announcements (lista principal)
            // onSelect ahora recibe el ID (String)
            composable(NavigationRoutes.ANNOUNCEMENTS) {
                AnnouncementListScreen(navController = navController)
            }


            // Announcements -> Detalle
            composable("${NavigationRoutes.ANNOUNCEMENT_DETAIL}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                if (id != null) {
                    AnnouncementDetailScreen(
                        announcementId = id,
                        onBack = { navController.popBackStack() },
                        navController = navController
                    )
                }
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

            // Announcements -> Editar
            composable("${NavigationRoutes.ANNOUNCEMENT_EDIT}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                if (id != null) {
                    EditAnnouncementScreen(
                        announcementId = id,
                        onBack = { navController.popBackStack() },
                        onUpdated = { navController.popBackStack() }
                    )
                }
            }





            // Announcements -> Crear
            composable(NavigationRoutes.ANNOUNCEMENT_CREATE) {
                CreateAnnouncementScreen(
                    onCreated = { navController.popBackStack() }
                )
            }
            composable(NavigationRoutes.NOTIFICATIONS) {
                val context = androidx.compose.ui.platform.LocalContext.current
                val viewModel = remember { DependencyFactory.createNotificationViewModel(context) }
                
                NotificationScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}
