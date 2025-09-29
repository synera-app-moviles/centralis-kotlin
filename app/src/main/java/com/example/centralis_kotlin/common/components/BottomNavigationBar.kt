package com.example.centralis_kotlin.common.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.centralis_kotlin.common.navigation.NavigationRoutes

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF160F23), // Fondo oscuro como en el diseño
        modifier = Modifier.height(80.dp)
    ) {
        //  Announcements
        NavigationBarItem(
            icon = { 
                Icon(
                    Icons.Default.RecordVoiceOver,
                    contentDescription = "Announcements",
                    tint = if (currentRoute == NavigationRoutes.ANNOUNCEMENTS) 
                        Color(0xFF823DFA) else Color(0xFFA58ECC)
                ) 
            },
            selected = currentRoute == NavigationRoutes.ANNOUNCEMENTS,
            onClick = { onNavigate(NavigationRoutes.ANNOUNCEMENTS) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF823DFA),
                unselectedIconColor = Color(0xFFA58ECC),
                indicatorColor = Color.Transparent
            )
        )
        
        //  Events
        NavigationBarItem(
            icon = { 
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Events",
                    tint = if (currentRoute == NavigationRoutes.EVENTS) 
                        Color(0xFF823DFA) else Color(0xFFA58ECC)
                ) 
            },
            selected = currentRoute == NavigationRoutes.EVENTS,
            onClick = { onNavigate(NavigationRoutes.EVENTS) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF823DFA),
                unselectedIconColor = Color(0xFFA58ECC),
                indicatorColor = Color.Transparent
            )
        )
        
        //  Chat
        NavigationBarItem(
            icon = { 
                Icon(

                    Icons.Default.QuestionAnswer,
                    contentDescription = "Chat",
                    tint = if (currentRoute == NavigationRoutes.CHAT)
                        Color(0xFF823DFA) else Color(0xFFA58ECC)
                ) 
            },
            selected = currentRoute == NavigationRoutes.CHAT, 
            onClick = { onNavigate(NavigationRoutes.CHAT) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF823DFA),
                unselectedIconColor = Color(0xFFA58ECC),
                indicatorColor = Color.Transparent
            )
        )
        
        // Profile
        NavigationBarItem(
            icon = { 
                Icon(
                    Icons.Default.Person, 
                    contentDescription = "Profile",
                    tint = if (currentRoute == NavigationRoutes.PROFILE) 
                        Color(0xFF823DFA) else Color(0xFFA58ECC)
                ) 
            },
            selected = currentRoute == NavigationRoutes.PROFILE,
            onClick = { onNavigate(NavigationRoutes.PROFILE) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF823DFA),
                unselectedIconColor = Color(0xFFA58ECC),
                indicatorColor = Color.Transparent
            )
        )
    }
}