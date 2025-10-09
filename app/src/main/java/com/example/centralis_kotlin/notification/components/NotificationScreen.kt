package com.example.centralis_kotlin.notification.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.centralis_kotlin.notification.model.Notification
import com.example.centralis_kotlin.notification.model.NotificationType
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    onNavigateBack: () -> Unit,
    viewModel: NotificationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1B2E))
    ) {
        // Top Bar exacto como en la imagen
        TopAppBar(
            title = {
                Text(
                    text = "Notification",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1A1B2E)
            )
        )

        // Content
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF4FC3F7))
            }
        } else {
            NotificationList(
                notifications = uiState.notifications,
                onNotificationClick = { notification ->
                    if (!notification.isRead) {
                        viewModel.markAsRead(notification.id)
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        // Bottom Navigation Bar como en la imagen
        BottomNavigationBar()
    }
}

@Composable
private fun NotificationList(
    notifications: List<Notification>,
    onNotificationClick: (Notification) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(notifications) { notification ->
            NotificationItem(
                notification = notification,
                onClick = { onNotificationClick(notification) }
            )
        }
    }
}

@Composable
private fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit
) {
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Icon con diseño exacto de la imagen
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(getTypeColor(notification.type).copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getTypeIcon(notification.type),
                contentDescription = null,
                tint = getTypeColor(notification.type),
                modifier = Modifier.size(24.dp)
            )
        }

        // Content exacto como en la imagen
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = notification.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = dateFormatter.format(notification.timestamp),
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        // Indicador de no leído
        if (!notification.isRead) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4FC3F7))
            )
        }
    }
}

@Composable
private fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFF1A1B2E))
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Iconos de la barra inferior como en la imagen
        Icon(
            imageVector = Icons.Default.Campaign,
            contentDescription = "Announcements",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Events",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.Chat,
            contentDescription = "Chat",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

private fun getTypeIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.POLICY_UPDATE -> Icons.Default.Policy
        NotificationType.WELLNESS -> Icons.Default.FavoriteBorder
        NotificationType.ANNOUNCEMENT -> Icons.Default.Campaign
        NotificationType.SYSTEM -> Icons.Default.Settings
        NotificationType.REMINDER -> Icons.Default.Schedule
    }
}

private fun getTypeColor(type: NotificationType): Color {
    return when (type) {
        NotificationType.POLICY_UPDATE -> Color(0xFF4FC3F7)
        NotificationType.WELLNESS -> Color(0xFF81C784)
        NotificationType.ANNOUNCEMENT -> Color(0xFFFFB74D)
        NotificationType.SYSTEM -> Color(0xFF90A4AE)
        NotificationType.REMINDER -> Color(0xFFF06292)
    }
}
