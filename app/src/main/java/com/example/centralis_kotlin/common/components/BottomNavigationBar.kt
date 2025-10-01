package com.example.centralis_kotlin.common.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.centralis_kotlin.common.navigation.NavigationRoutes
import com.example.centralis_kotlin.ui.theme.CentralisPrimary
import com.example.centralis_kotlin.ui.theme.CentralisPlaceholder
import com.example.centralis_kotlin.ui.theme.CentralisSecondary

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = CentralisSecondary, // Fondo oscuro de la barra
        modifier = Modifier.height(80.dp)
    ) {
        //  Announcements
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Campaign,
                    contentDescription = "Announcements",
                    tint = if (currentRoute == NavigationRoutes.ANNOUNCEMENTS)
                        CentralisPrimary else CentralisPlaceholder
                )
            },
            selected = currentRoute == NavigationRoutes.ANNOUNCEMENTS,
            onClick = { onNavigate(NavigationRoutes.ANNOUNCEMENTS) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CentralisPrimary,
                unselectedIconColor = CentralisPlaceholder,
                indicatorColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )

        //  Events
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Events",
                    tint = if (currentRoute == NavigationRoutes.EVENTS)
                        CentralisPrimary else CentralisPlaceholder
                )
            },
            selected = currentRoute == NavigationRoutes.EVENTS,
            onClick = { onNavigate(NavigationRoutes.EVENTS) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CentralisPrimary,
                unselectedIconColor = CentralisPlaceholder,
                indicatorColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )

        //  Chat
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.QuestionAnswer,
                    contentDescription = "Chat",
                    tint = if (currentRoute == NavigationRoutes.CHAT)
                        CentralisPrimary else CentralisPlaceholder
                )
            },
            selected = currentRoute == NavigationRoutes.CHAT,
            onClick = { onNavigate(NavigationRoutes.CHAT) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CentralisPrimary,
                unselectedIconColor = CentralisPlaceholder,
                indicatorColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )

        // Profile
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = if (currentRoute == NavigationRoutes.PROFILE)
                        CentralisPrimary else CentralisPlaceholder
                )
            },
            selected = currentRoute == NavigationRoutes.PROFILE,
            onClick = { onNavigate(NavigationRoutes.PROFILE) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CentralisPrimary,
                unselectedIconColor = CentralisPlaceholder,
                indicatorColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
    }
}