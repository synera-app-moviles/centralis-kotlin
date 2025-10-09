package com.example.centralis_kotlin.notification.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.centralis_kotlin.notification.components.NotificationScreen

fun NavGraphBuilder.notificationNavigation(navController: NavController) {
    composable(NotificationRoutes.NOTIFICATION_SCREEN) {
        NotificationScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun NotificationNavigation(
    navController: NavController,
    onNavigateBack: () -> Unit
) {
    NotificationScreen(
        onNavigateBack = onNavigateBack
    )
}
