package com.example.centralis_kotlin.notification.navigation

object NotificationRoutes {
    const val NOTIFICATION_SCREEN = "notification_screen"
}

// Extensión para facilitar la navegación a notificaciones
fun androidx.navigation.NavController.navigateToNotifications() {
    navigate(NotificationRoutes.NOTIFICATION_SCREEN)
}
