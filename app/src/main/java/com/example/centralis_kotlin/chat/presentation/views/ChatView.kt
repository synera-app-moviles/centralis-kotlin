package com.example.centralis_kotlin.chat.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.centralis_kotlin.chat.domain.models.ChatItem
import com.example.centralis_kotlin.common.navigation.NavigationRoutes


private val DarkBg = Color(0xFF160F23)
private val SubtleText = Color(0xFFA88ECC)
private val TitleColor = Color(0xFFFFFFFF)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChatView(
    nav: NavHostController,
    onNewChat: () -> Unit = {},
    onOpenChat: (ChatItem) -> Unit = {}
) {
    // Mock de datos (luego reemplazas por ViewModel / API)
    val chats = listOf(
        ChatItem(
            id = "1",
            name = "Marketing Team",
            lastMessage = "Hey, how are you doing?",
            avatarUrl = "https://i.pinimg.com/564x/79/60/02/7960020f6dba83b3d44a9846075ab28b.jpg"
        ),
        ChatItem(
            id = "2",
            name = "Product Team",
            lastMessage = "Let’s schedule a meeting",
            avatarUrl = "https://i.pinimg.com/564x/f7/1d/6e/f71d6ee62f9d1a1b0f5d1d1b17a4e9c1.jpg"
        ),
        ChatItem(
            id = "3",
            name = "Sales Team",
            lastMessage = "I'll send you the report",
            avatarUrl = "https://i.pinimg.com/564x/b4/b2/9c/b4b29c0b1e1a9a8c7b3ba6a0d98e9f2a.jpg"
        ),
        ChatItem(
            id = "4",
            name = "Finance Team",
            lastMessage = "We need to discuss the budget",
            avatarUrl = "https://i.pinimg.com/564x/1f/63/42/1f63423a7b3f7a2c31e9e7a2a9a9c1e9.jpg"
        ),
        ChatItem(
            id = "5",
            name = "Advertising Team",
            lastMessage = "The new campaign is live!",
            avatarUrl = "https://i.pinimg.com/564x/5c/6a/2b/5c6a2b8a9a9c3b1f2e4d1a6b7c8d9e0f.jpg"
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(horizontal = 16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
        ) {
            Text(
                text = "Chats",
                color = TitleColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = { nav.navigate(NavigationRoutes.CHAT_CREATE) },
                modifier = Modifier.align(Alignment.CenterEnd)


            ) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "New chat",
                    tint = TitleColor)

            }
        }


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(chats) { chat ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            nav.navigate("chatDetail/${chat.id}")
                        }
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlideImage(
                        model = chat.avatarUrl,
                        contentDescription = "${chat.name} avatar",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = chat.name,
                            color = TitleColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = chat.lastMessage,
                            color = SubtleText,
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
