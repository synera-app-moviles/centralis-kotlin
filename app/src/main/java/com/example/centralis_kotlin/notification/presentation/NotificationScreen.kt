package com.example.centralis_kotlin.notification.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.centralis_kotlin.R
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Modelo de UI para la notificación
data class NotificationUiModel(
    val id: String,
    val title: String,
    val message: String,
    val date: String, // Usar String para compatibilidad con minSdk 24
    val type: NotificationType
)

enum class NotificationType {
    ANNOUNCEMENT, EVENT, CHAT
}

class NotificationViewModel : ViewModel() {
    private val _notifications = MutableStateFlow(
        listOf(
            NotificationUiModel(
                id = "1",
                title = "Remote Work Policy Update",
                message = "",
                date = "May 20, 2024",
                type = NotificationType.ANNOUNCEMENT
            ),
            NotificationUiModel(
                id = "2",
                title = "New Employee Wellness",
                message = "",
                date = "May 15, 2024",
                type = NotificationType.EVENT
            ),
            NotificationUiModel(
                id = "3",
                title = "Announcement of the Next",
                message = "",
                date = "May 10, 2024",
                type = NotificationType.ANNOUNCEMENT
            )
        )
    )
    val notifications: StateFlow<List<NotificationUiModel>> = _notifications
}

@Composable
fun NotificationScreen(viewModel: NotificationViewModel) {
    val notifications by viewModel.notifications.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF18102A))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Notification",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))
        notifications.forEach { notification ->
            NotificationItem(notification)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF231A3A), RoundedCornerShape(12.dp))
            .padding(16.dp)
            .clickable { /* Acción al hacer click */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconRes = when (notification.type) {
            NotificationType.ANNOUNCEMENT -> R.drawable.ic_announcement
            NotificationType.EVENT -> R.drawable.ic_event
            NotificationType.CHAT -> R.drawable.ic_chat
            else -> R.drawable.ic_announcement // fallback
        }
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color(0xFFB9A4FF),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = notification.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
            Text(
                text = notification.date,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFB9A4FF)
            )
        }
    }
}
