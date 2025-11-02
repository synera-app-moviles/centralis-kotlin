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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.example.centralis_kotlin.chat.domain.models.ChatItem
import com.example.centralis_kotlin.chat.presentation.viewmodels.ChatViewModel
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

    val context = LocalContext.current
    val vm = remember { ChatViewModel(context) }

    // 1) cargar grupos al entrar
    LaunchedEffect(Unit) { vm.loadMyGroups() }

    // 2) mapear GroupResponse -> tu ChatItem de UI
    val chats: List<ChatItem> = remember(vm.groups) {
        vm.groups.map { g ->
            ChatItem(
                id = g.id,
                name = g.name,
                lastMessage = "", // si luego quieres, trae el último mensaje
                avatarUrl = g.imageUrl ?: "https://i.imgur.com/xDofyTr.png"
            )
        }
    }

    // 3) estados de carga/error (opcionales)
    val isLoading = vm.isLoading
    val error = vm.error


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
        // indicador de carga / error (opcionales)
        if (isLoading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TitleColor)
            }
        }
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
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
