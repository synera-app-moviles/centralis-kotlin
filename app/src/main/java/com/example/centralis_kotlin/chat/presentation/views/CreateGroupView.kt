package com.example.centralis_kotlin.chat.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
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
import com.example.centralis_kotlin.chat.presentation.viewmodels.ChatViewModel
import com.example.centralis_kotlin.chat.domain.models.GroupVisibility
import com.example.centralis_kotlin.common.components.ImagePicker
import com.example.centralis_kotlin.common.config.ImageType
import com.example.centralis_kotlin.profile.presentation.viewmodels.ProfileViewModel

// Paleta
private val Bg = Color(0xFF160F23)
private val CardBg = Color(0xFF221733)
private val Muted = Color(0xFFA88ECC)
private val Accent = Color(0xFF8E3DF9)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CreateGroupView(
    nav: NavHostController,
    onCreate: () -> Unit = {}
) {
    val context = LocalContext.current

    // VM de chat (crear grupo)
    val chatVm = remember { ChatViewModel(context) }
    // VM de perfiles (para listar participantes reales)
    val profileVm = remember { ProfileViewModel(context) }

    var groupName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    var groupImageUrl by remember { mutableStateOf<String?>(null) }
    var visibility by remember { mutableStateOf(GroupVisibility.PUBLIC) } // si luego quieres selector

    // 1) Cargar perfiles reales
    LaunchedEffect(Unit) { profileVm.getAllProfiles() }

    // 2) Adaptar perfiles → items de la UI
    val allPeople = profileVm.profilesList
    val filtered = remember(query, allPeople) {
        if (query.isBlank()) allPeople
        else allPeople.filter {
            it.fullName.contains(query, true) ||
                    it.position.name.contains(query, true)
        }
    }

    // 3) Selección de participantes (userIds reales)
    val selectedIds = remember { mutableStateListOf<String>() }

    // 4) Cerrar cuando el grupo se creó
    LaunchedEffect(chatVm.createResult) {
        if (chatVm.createResult != null) {
            onCreate()
            nav.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        // Header fijo (no scrolleable)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { nav.popBackStack() }) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
            Text(
                "Nuevo grupo",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.size(58.dp))
        }

        // Contenido scrolleable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

        Spacer(Modifier.height(12.dp))

        // Nombre
        OutlinedTextField(
            value = groupName,
            onValueChange = { groupName = it },
            placeholder = { Text("Group name") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardBg,
                unfocusedContainerColor = CardBg,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedPlaceholderColor = Muted,
                unfocusedPlaceholderColor = Muted
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Descripción
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Descripción") },
            minLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardBg,
                unfocusedContainerColor = CardBg,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedPlaceholderColor = Muted,
                unfocusedPlaceholderColor = Muted
            ),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
        )

        Spacer(Modifier.height(12.dp))

        // Image picker for group icon
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
            Spacer(Modifier.height(16.dp))
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Group Icon Preview:",
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
        Text("Select participants", color = Color.White, fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(8.dp))

        // Buscador
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search for employees") },
            trailingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Muted) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardBg,
                unfocusedContainerColor = CardBg,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedPlaceholderColor = Muted,
                unfocusedPlaceholderColor = Muted
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Lista real de participantes (desde Profile)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp), // Altura máxima fija para permitir scroll
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered, key = { it.userId }) { profile ->
                val checked = selectedIds.contains(profile.userId)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val avatar = profile.avatarUrl ?: "https://i.pinimg.com/1200x/b1/fc/14/b1fc14cb1cf646a3a25ad517706831de.jpg"
                    GlideImage(
                        model = avatar,
                        contentDescription = "${profile.fullName} avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(profile.fullName, color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text(profile.position.name, color = Muted, fontSize = 12.sp)
                    }
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { isChecked ->
                            if (isChecked) selectedIds.add(profile.userId)
                            else selectedIds.remove(profile.userId)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Accent,
                            uncheckedColor = Muted
                        )
                    )
                }
            }
        }

        // Crear grupo (llama al WS real)
        Button(
            onClick = {
                if (groupName.isNotBlank()) {
                    chatVm.createGroup(
                        name = groupName.trim(),
                        description = description.trim().ifBlank { null },
                        imageUrl = groupImageUrl, // Incluir la imagen subida
                        visibility = GroupVisibility.PUBLIC,
                        selectedUserIds = selectedIds.toList()
                    )
                }
            },
            enabled = !chatVm.isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Accent),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            if (chatVm.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text("Create group", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        // Errores de creación (opcional)
        chatVm.error?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }
        
        Spacer(Modifier.height(16.dp)) // Espacio adicional al final
        
        } 
    }
}
