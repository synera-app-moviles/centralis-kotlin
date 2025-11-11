package com.example.centralis_kotlin.announcement.presentation.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.centralis_kotlin.announcement.model.Comment
import java.text.SimpleDateFormat
import java.util.*

private val COMMENT_DATE_FORMATTER = SimpleDateFormat("d MMM yyyy, HH:mm", Locale("es", "ES"))

@Composable
fun CommentCard(comment: Comment) {
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
            // Nombre del empleado + fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                /*Text(
                    text = comment.employeeId, // podrías mapearlo a nombre real con IAM
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )*/

                val createdAtText = try {
                    COMMENT_DATE_FORMATTER.format(comment.createdAt)
                } catch (e: Throwable) {
                    comment.createdAt.toString()
                }

                Text(
                    text = createdAtText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Contenido del comentario
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}
