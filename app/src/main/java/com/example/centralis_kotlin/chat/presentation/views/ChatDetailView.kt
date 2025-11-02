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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.centralis_kotlin.chat.domain.models.MessageStatus
import com.example.centralis_kotlin.chat.presentation.viewmodels.ChatDetailViewModel
import com.example.centralis_kotlin.common.SharedPreferencesManager
import com.example.centralis_kotlin.profile.presentation.viewmodels.ProfileViewModel

// Paleta (igual a la tuya)
import com.example.centralis_kotlin.chat.domain.models.MessageUI
import com.example.centralis_kotlin.chat.domain.models.SseConnectionState

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
    val context = LocalContext.current
    val vm = remember { ChatDetailViewModel(context) }
    val prefs = remember { SharedPreferencesManager(context) }
    val myUserId = remember { prefs.getUserId() }

    // 1) Cargar mensajes del grupo
    LaunchedEffect(chatId) { vm.load(chatId) }

    // 2) Estados expuestos por el VM
    val messages = vm.messages                // List<MessageResponse>
    val isLoading = vm.isLoading
    val sending = vm.sending
    val error = vm.error
    val connectionState by vm.connectionState.collectAsState()



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


            // Avatar del grupo (misma imagen que en ChatView)
            GlideImage(
                model = vm.currentGroup?.imageUrl ?: "https://i.imgur.com/xDofyTr.png",
                contentDescription = "Group avatar",
                modifier = Modifier.size(36.dp).clip(CircleShape)
            )

            Spacer(Modifier.width(10.dp))


            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vm.currentGroup?.name ?: "Chat", // o pásalo por parámetro si lo tienes
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }

            // Botón de reintentar si hay error de conexión
            if (connectionState is SseConnectionState.Error) {
                IconButton(onClick = { vm.retryConnection() }) {
                    Icon(
                        imageVector = Icons.Default.Send, // Usar un icono de refresh si tienes
                        contentDescription = "Reintentar conexión",
                        tint = Color(0xFFFF5252)
                    )
                }
            }

            IconButton(onClick = { /* menú overflow */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
            }
        }

        // Loading / Error (opcionales)
        if (isLoading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // Lista de mensajes (MessageResponse)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.messageId }) { msg ->
                val isMe = msg.senderId == vm.currentUserId
                val profile = vm.profileOf(msg.senderId)

                // 1) Nombre del autor SIEMPRE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Text(
                        text = profile?.fullName ?: msg.senderId.take(8),
                        color = Muted,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                // 2) Burbuja + avatar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    if (!isMe) {
                        GlideImage(
                            model = profile?.avatarUrl
                                ?: "https://i.pinimg.com/736x/e5/c1/c3/e5c1c34fe65d23b9a876b3dcdfd27ba7.jpg",
                            contentDescription = null,
                            modifier = Modifier.size(28.dp).clip(CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                    }

                    Surface(
                        color = if (isMe) BubbleMine else BubbleOther,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = msg.body, // <- viene del backend
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }

                    if (isMe) {
                        Spacer(Modifier.width(8.dp))
                        GlideImage(
                            model = vm.profileOf(vm.currentUserId ?: "")?.avatarUrl
                                ?: "https://i.pinimg.com/736x/e5/c1/c3/e5c1c34fe65d23b9a876b3dcdfd27ba7.jpg",
                            contentDescription = null,
                            modifier = Modifier.size(28.dp).clip(CircleShape)
                        )
                    }
                }
            }

        }

        // Barra de input: ahora envía usando el VM
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
                onClick = {
                    vm.send(chatId, input)
                    input = ""
                },
                enabled = input.isNotBlank() && !sending,
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = BubbleMine)
            ) {
                if (sending) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    }
}
