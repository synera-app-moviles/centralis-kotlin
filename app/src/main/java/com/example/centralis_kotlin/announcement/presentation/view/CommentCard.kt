package com.example.centralis_kotlin.announcement.presentation.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.centralis_kotlin.announcement.model.Comment
import com.example.centralis_kotlin.profile.models.ProfileResponse
import java.text.SimpleDateFormat
import java.util.*

private val COMMENT_DATE_FORMATTER = SimpleDateFormat("d MMM yyyy, HH:mm", Locale("es", "ES"))

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CommentCard(
    comment: Comment,
    userProfile: ProfileResponse? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 2.dp,
        color = Color(0xFF4A2B61)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Fila con avatar + nombre + fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar del usuario
                GlideImage(
                    model = userProfile?.avatarUrl 
                        ?: "https://i.pinimg.com/736x/e5/c1/c3/e5c1c34fe65d23b9a876b3dcdfd27ba7.jpg",
                    contentDescription = "Avatar de ${userProfile?.fullName ?: comment.employeeId}",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Nombre del usuario (o employeeId si no se encuentra el perfil)
                    Text(
                        text = userProfile?.fullName ?: comment.employeeId.take(8),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    
                    // Fecha
                    val createdAtText = try {
                        COMMENT_DATE_FORMATTER.format(comment.createdAt)
                    } catch (e: Throwable) {
                        comment.createdAt.toString()
                    }

                    Text(
                        text = createdAtText,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Contenido del comentario
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                lineHeight = 20.sp
            )
        }
    }
}
