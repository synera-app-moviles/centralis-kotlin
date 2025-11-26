package com.example.centralis_kotlin.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.centralis_kotlin.chat.domain.models.ChatImageResponse
import com.example.centralis_kotlin.profile.models.ProfileResponse
import java.text.SimpleDateFormat
import java.util.*

// Paleta de colores para el chat
private val BubbleMine = Color(0xFF8E3DF9)
private val BubbleOther = Color(0xFF2B1E3F)
private val Muted = Color(0xFFA88ECC)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChatImageItem(
    image: ChatImageResponse,
    isMe: Boolean,
    profile: ProfileResponse?
) {
    Column {
        // Nombre del autor
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
        ) {
            Text(
                text = profile?.fullName ?: image.senderId.take(8),
                color = Muted,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }

        // Imagen + avatar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
        ) {
            // Avatar del otro usuario (solo si no soy yo)
            if (!isMe) {
                GlideImage(
                    model = profile?.avatarUrl
                        ?: "https://i.pinimg.com/736x/e5/c1/c3/e5c1c34fe65d23b9a876b3dcdfd27ba7.jpg",
                    contentDescription = null,
                    modifier = Modifier.size(28.dp).clip(CircleShape)
                )
                Spacer(Modifier.width(8.dp))
            }

            // Burbuja con imagen
            Surface(
                color = if (isMe) BubbleMine else BubbleOther,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.widthIn(max = 280.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    GlideImage(
                        model = image.imageUrl,
                        contentDescription = "Imagen compartida",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 250.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                    
                    // Timestamp opcional
                    Text(
                        text = formatTime(image.sentAt),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Avatar del usuario actual (solo si soy yo)
            if (isMe) {
                Spacer(Modifier.width(8.dp))
                GlideImage(
                    model = profile?.avatarUrl
                        ?: "https://i.pinimg.com/736x/e5/c1/c3/e5c1c34fe65d23b9a876b3dcdfd27ba7.jpg",
                    contentDescription = null,
                    modifier = Modifier.size(28.dp).clip(CircleShape)
                )
            }
        }
    }
}

@Composable
fun ChatImagesSection(
    images: List<ChatImageResponse>,
    currentUserId: String?,
    profileProvider: (String) -> ProfileResponse?
) {
    if (images.isEmpty()) return
    
    Column {
        Text(
            text = "Imágenes compartidas",
            color = Muted,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            items(images) { image ->
                ChatImageItem(
                    image = image,
                    isMe = image.senderId == currentUserId,
                    profile = profileProvider(image.senderId)
                )
            }
        }
    }
}

private fun formatTime(timestamp: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(timestamp)
        date?.let { outputFormat.format(it) } ?: ""
    } catch (e: Exception) {
        ""
    }
}