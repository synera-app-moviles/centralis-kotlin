package com.example.centralis_kotlin.events.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.centralis_kotlin.events.model.UpdateEventRequest
import com.example.centralis_kotlin.profile.models.Position
import com.example.centralis_kotlin.profile.models.ProfileResponse
import com.example.centralis_kotlin.profile.services.ProfileWebService
import com.example.centralis_kotlin.ui.theme.CentralisOnPrimary
import com.example.centralis_kotlin.ui.theme.CentralisPrimary
import androidx.compose.ui.text.font.FontWeight
import java.util.UUID
import com.example.centralis_kotlin.events.presentation.views.DateTimePickerField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateEventScreen(
    eventId: UUID,
    currentTitle: String,
    currentDescription: String,
    currentDateTime: String,
    currentLocation: String?,

    initialRecipients: List<UUID>,
    onUpdate: (UUID, UpdateEventRequest) -> Unit,
    onBack: () -> Unit,
    onCancel: () -> Unit,
    profileWebService: ProfileWebService,
    authorization: String
) {
    var title by remember { mutableStateOf(currentTitle) }
    var description by remember { mutableStateOf(currentDescription) }
    var dateTime by remember { mutableStateOf(currentDateTime) }
    var location by remember { mutableStateOf(currentLocation ?: "") }
    var search by remember { mutableStateOf("") }
    var selectedRecipients by remember { mutableStateOf(initialRecipients) }
    var profiles by remember { mutableStateOf<List<ProfileResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val response = profileWebService.getAllProfiles(authorization)
        if (response.isSuccessful) {
            profiles = response.body()?.filter { it.position == Position.EMPLOYEE } ?: emptyList()
        }
        isLoading = false
    }

    val filteredProfiles = profiles.filter {
        it.firstName.contains(search, ignoreCase = true) ||
        it.lastName.contains(search, ignoreCase = true)
    }

    fun toIsoLike(input: String): String {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return trimmed
        return if (trimmed.contains(' ')) {
            val t = trimmed.replace(' ', 'T')
            if (t.length <= 16) "$t:00" else t
        } else if (trimmed.contains('T')) {
            if (trimmed.length <= 16) "$trimmed:00" else trimmed
        } else {
            trimmed
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Update Event",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(x = (-24).dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF170F24)
                )
            )
        },
        containerColor = Color(0xFF170F24)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Event title", color = Color(0xFFA68FCC)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF30214A),
                    unfocusedContainerColor = Color(0xFF30214A),
                    focusedTextColor = Color(0xFFA68FCC),
                    unfocusedTextColor = Color(0xFFA68FCC),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Event description", color = Color(0xFFA68FCC)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF30214A),
                    unfocusedContainerColor = Color(0xFF30214A),
                    focusedTextColor = Color(0xFFA68FCC),
                    unfocusedTextColor = Color(0xFFA68FCC),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )


            DateTimePickerField(
                value = dateTime,
                onValueChange = { dateTime = it },
                selectedColor = Color(0xFFA68FCC),
                headerBackground = Color(0xFF302149),
                textColor = Color.White
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location", color = Color(0xFFA68FCC)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF30214A),
                    unfocusedContainerColor = Color(0xFF30214A),
                    focusedTextColor = Color(0xFFA68FCC),
                    unfocusedTextColor = Color(0xFFA68FCC),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Buscar empleado", color = Color(0xFFA68FCC)) },
                modifier = Modifier.fillMaxWidth()
            )

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(filteredProfiles) { profile ->
                        val uuid = UUID.fromString(profile.userId)
                        val isSelected = selectedRecipients.contains(uuid)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedRecipients = if (isSelected) {
                                        selectedRecipients - uuid
                                    } else {
                                        selectedRecipients + uuid
                                    }
                                }
                                .background(if (isSelected) Color(0xFFA68FCC).copy(alpha = 0.2f) else Color.Transparent)
                                .padding(8.dp)
                        ) {
                            val avatar = profile.avatarUrl ?: "https://i.pravatar.cc/100?u=${profile.userId}"
                            AsyncImage(
                                model = avatar,
                                contentDescription = "${profile.firstName} ${profile.lastName} avatar",
                                modifier = Modifier.size(40.dp).clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "${profile.firstName} ${profile.lastName}",
                                color = Color(0xFFFCFCFC),
                                modifier = Modifier.weight(1f)
                            )
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = {
                                    selectedRecipients = if (isSelected) {
                                        selectedRecipients - uuid
                                    } else {
                                        selectedRecipients + uuid
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val formattedDate = toIsoLike(dateTime)
                    val request = UpdateEventRequest(
                        title = title,
                        description = description,
                        date = formattedDate,
                        location = location.ifBlank { null },
                        recipientIds = selectedRecipients
                    )
                    onUpdate(eventId, request)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CentralisPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    "Update Event",
                    color = CentralisOnPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}