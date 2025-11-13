package com.example.centralis_kotlin.chat.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.centralis_kotlin.chat.presentation.viewmodels.ChatDetailViewModel
import com.example.centralis_kotlin.common.components.ImagePicker
import com.example.centralis_kotlin.common.config.ImageType
import com.example.centralis_kotlin.common.SharedPreferencesManager

// Paleta consistente
private val Bg = Color(0xFF160F23)
private val CardBg = Color(0xFF221733)
private val Muted = Color(0xFFA88ECC)
private val Accent = Color(0xFF8E3DF9)

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditGroupView(
    nav: NavHostController,
    groupId: String
) {
    val context = LocalContext.current
    val vm = remember { ChatDetailViewModel(context) }
    val prefs = remember { SharedPreferencesManager(context) }
    
    // Estados del formulario
    var groupName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var groupImageUrl by remember { mutableStateOf<String?>(null) }

    // Cargar información del grupo al entrar
    LaunchedEffect(groupId) {
        vm.load(groupId)
    }

    // Inicializar campos con datos actuales del grupo
    LaunchedEffect(vm.currentGroup) {
        vm.currentGroup?.let { group ->
            groupName = group.name
            description = group.description ?: ""
            groupImageUrl = group.imageUrl
        }
    }

    Scaffold(
        containerColor = Bg,
        topBar = {
            TopAppBar(
                title = { Text("Edit Group", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Bg
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Group name
            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group Name", color = Muted) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBg,
                    unfocusedContainerColor = CardBg,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Accent,
                    unfocusedLabelColor = Muted,
                    focusedBorderColor = Accent,
                    unfocusedBorderColor = Muted,
                    cursorColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description", color = Muted) },
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBg,
                    unfocusedContainerColor = CardBg,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Accent,
                    unfocusedLabelColor = Muted,
                    focusedBorderColor = Accent,
                    unfocusedBorderColor = Muted,
                    cursorColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
            )

            // Image picker for group icon
            Text(
                text = "Group Icon",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            ImagePicker(
                imageType = ImageType.AVATAR,
                currentImageUrl = groupImageUrl,
                onImageUploaded = { imageUrl ->
                    groupImageUrl = imageUrl
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Group icon preview
            if (!groupImageUrl.isNullOrEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Current Group Icon:",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(Modifier.height(8.dp))
                    
                    GlideImage(
                        model = groupImageUrl,
                        contentDescription = "Group icon preview",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Update button
            Button(
                onClick = {
                    if (groupName.isNotBlank()) {
                        vm.updateGroup(groupId, groupName, description.takeIf { it.isNotBlank() }, groupImageUrl)
                        nav.popBackStack()
                    }
                },
                enabled = !vm.isLoading && groupName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Accent),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (vm.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White, 
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Update Group", 
                        color = Color.White, 
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Error message
            vm.error?.let {
                Text(
                    text = it, 
                    color = Color.Red, 
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}