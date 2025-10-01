package com.example.centralis_kotlin.chat.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.centralis_kotlin.chat.domain.models.MessageUI

// Paleta consistente con tus pantallas
private val Bg = Color(0xFF160F23)
private val BubbleMine = Color(0xFF8E3DF9)
private val BubbleOther = Color(0xFF2B1E3F)
private val FieldBg = Color(0xFF221733)
private val Muted = Color(0xFFA88ECC)



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChatDetailView(
    nav: NavHostController,
    chatId: String,
) {
    // Mock: título por id (en real vendría de repo/VM)
    val title = when (chatId) {
        "1" -> "Marketing Team"
        "2" -> "Product Team"
        "3" -> "Sales Team"
        "4" -> "Finance Team"
        else -> "Advertising Team"
    }

    // Mock de mensajes
    val messages = remember {
        listOf(
            MessageUI("1","Marketing Team","Hey, how are you doing?",
                "https://i.pinimg.com/564x/79/60/02/7960020f6dba83b3d44a9846075ab28b.jpg", false),
            MessageUI("2","Product Team","Let's schedule a meeting",
                "https://i.pinimg.com/564x/f7/1d/6e/f71d6ee62f9d1a1b0f5d1d1b17a4e9c1.jpg", true),
            MessageUI("3","Sales Team","I'll send you the report",
                "https://i.pinimg.com/564x/b4/b2/9c/b4b29c0b1e1a9a8c7b3ba6a0d98e9f2a.jpg", false),
            MessageUI("4","Finance Team","We need to discuss the budget",
                "https://i.pinimg.com/564x/1f/63/42/1f63423a7b3f7a2c31e9e7a2a9a9c1e9.jpg", true),
            MessageUI("5","Advertising Team","The new campaign is live!",
                "https://i.pinimg.com/564x/5c/6a/2b/5c6a2b8a9a9c3b1f2e4d1a6b7c8d9e0f.jpg", false),
        )
    }

    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        // Header
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { nav.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            // Avatar del grupo (mock: primer avatar de la lista)
            GlideImage(
                model = messages.firstOrNull { !it.isMe }?.avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* menú overflow */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
            }
        }

        // Lista de mensajes
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (msg.isMe) Alignment.End else Alignment.Start
                ) {
                    // Etiqueta de autor (como en el Figma)
                    Text(
                        msg.author,
                        color = Muted,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        if (!msg.isMe) {
                            GlideImage(
                                model = msg.avatar,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(Modifier.width(8.dp))
                        } else {
                            Spacer(Modifier.width(28.dp)) // equilibrar layout
                        }
                        Surface(
                            color = if (msg.isMe) BubbleMine else BubbleOther,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                msg.text,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Barra de input
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Write a message", color = Muted) },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FieldBg,
                    unfocusedContainerColor = FieldBg,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                trailingIcon = {
                    Icon(Icons.Default.AttachFile, contentDescription = "Attach", tint = Muted)
                }
            )
            Spacer(Modifier.width(8.dp))
            FilledIconButton(
                onClick = { /* send mock */ },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = BubbleMine)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}
