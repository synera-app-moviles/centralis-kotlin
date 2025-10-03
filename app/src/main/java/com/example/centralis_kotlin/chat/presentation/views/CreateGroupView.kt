package com.example.centralis_kotlin.chat.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.centralis_kotlin.chat.domain.models.Participant

// Paleta consistente con tu app
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
    var groupName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }

    // Mock de participantes
    val all = remember {
        listOf(
            Participant("1","Lucas","Desarrollador","https://i.pinimg.com/564x/79/60/02/7960020f6dba83b3d44a9846075ab28b.jpg"),
            Participant("2","Sofía","Diseñadora","https://i.pinimg.com/564x/b4/b2/9c/b4b29c0b1e1a9a8c7b3ba6a0d98e9f2a.jpg"),
            Participant("3","Martín","Marketing","https://i.pinimg.com/564x/f7/1d/6e/f71d6ee62f9d1a1b0f5d1d1b17a4e9c1.jpg"),
            Participant("4","Valentina","Ventas","https://i.pinimg.com/564x/1f/63/42/1f63423a7b3f7a2c31e9e7a2a9a9c1e9.jpg"),
        )
    }
    val filtered = remember(query, all) {
        if (query.isBlank()) all else all.filter {
            it.name.contains(query, true) || it.role.contains(query, true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(16.dp)
    ) {
        // Header
        Row(
            Modifier.fillMaxWidth(),
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
            Spacer(Modifier.size(58.dp)) // balance visual
        }

        Spacer(Modifier.height(12.dp))

        // Nombre del grupo
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

        // Upload image (mock)
        OutlinedButton(
            onClick = { /* no-op: selector mock */ },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = CardBg),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Default.Image, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Upload image", color = Color.White)
        }

        Spacer(Modifier.height(16.dp))
        Text("Select participants", color = Color.White, fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(8.dp))

        // Buscador
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search for employees or groups") },
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

        // Lista de participantes (checkbox mock)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered) { p ->
                var checked by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GlideImage(
                        model = p.avatar,
                        contentDescription = "${p.name} avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(p.name, color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text(p.role, color = Muted, fontSize = 12.sp)
                    }
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Accent,
                            uncheckedColor = Muted
                        )
                    )
                }
            }
        }

        Button(
            onClick = onCreate,
            colors = ButtonDefaults.buttonColors(containerColor = Accent),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Create group", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}


