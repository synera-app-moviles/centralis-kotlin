package com.example.centralis_kotlin.ui.notification

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
        // Asegúrate de tener estos íconos en res/drawable
        val iconRes = when (notification.type) {
            NotificationType.ANNOUNCEMENT -> R.drawable.ic_announcement // Debes agregar este recurso
            NotificationType.EVENT -> R.drawable.ic_event // Debes agregar este recurso
            NotificationType.CHAT -> R.drawable.ic_chat // Debes agregar este recurso
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
