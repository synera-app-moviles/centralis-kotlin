package com.example.centralis_kotlin.events.presentation.views

    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Add
    import androidx.compose.material.icons.filled.CalendarToday
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.style.TextOverflow
    import com.example.centralis_kotlin.events.model.EventResponse
    import com.example.centralis_kotlin.events.presentation.viewmodels.EventUiState
    import com.example.centralis_kotlin.common.di.DependencyFactory
    import java.util.UUID
    import com.example.centralis_kotlin.common.RetrofitClient
    import com.example.centralis_kotlin.common.SharedPreferencesManager
    import com.example.centralis_kotlin.profile.models.Position
    import kotlinx.coroutines.launch

    private fun formatDateSafe(dateObj: Any?): String {
        return try {
            val s = dateObj?.toString() ?: ""
            val replaced = s.replace('T', ' ')
            if (replaced.length > 16) replaced.substring(0, 16) else replaced
        } catch (_: Exception) { "" }
    }

   @Composable
   fun EventsScreen(
       isManager: Boolean,
       onAddEventClick: () -> Unit,
       onViewEventClick: (EventResponse) -> Unit
   ) {
       val context = LocalContext.current
       val eventViewModel = remember { DependencyFactory.createEventViewModel(context) }

       val uiState by eventViewModel.uiState.collectAsState()
       val events by eventViewModel.events.collectAsState()

       var isManagerLocal: Boolean? by remember { mutableStateOf(null) }
       val scope = rememberCoroutineScope()

       LaunchedEffect(Unit) {
           eventViewModel.loadVisibleEvents()
           scope.launch {
               try {
                   val prefs = SharedPreferencesManager(context)
                   val token = prefs.getToken()
                   val authHeader = if (token?.startsWith("Bearer ") == true) token else "Bearer ${token ?: ""}"
                   val resp = RetrofitClient.profileWebService.getAllProfiles(authHeader)
                   if (resp.isSuccessful) {
                       val profiles = resp.body() ?: emptyList()
                       val userIdStr = prefs.getUserId()
                       val myProfile = profiles.find { it.userId == userIdStr }
                       isManagerLocal = myProfile?.position == Position.MANAGER
                   } else {
                       isManagerLocal = null
                   }
               } catch (_: Exception) {
                   isManagerLocal = null
               }
           }
       }

       val effectiveIsManager = isManagerLocal ?: isManager

       Scaffold(
           topBar = { EventsTopBar(effectiveIsManager, onAddEventClick) },


           containerColor = Color(0xFF160F23)
       ) { padding ->
           Box(
               modifier = Modifier
                   .fillMaxSize()
                   .background(Color(0xFF160F23))
           ) {
               when (uiState) {
                   is EventUiState.Loading -> {
                       Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                           CircularProgressIndicator(color = Color.White)
                       }
                   }
                   is EventUiState.Error -> {
                       Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                           Column(horizontalAlignment = Alignment.CenterHorizontally) {
                               Text((uiState as EventUiState.Error).message, color = Color.Red)
                               Spacer(modifier = Modifier.height(8.dp))
                               Button(onClick = { eventViewModel.loadVisibleEvents() }) {
                                   Text("Reintentar")
                               }
                           }
                       }
                   }
                   else -> {
                       LazyColumn(
                           modifier = Modifier.padding(padding)
                       ) {
                           items(events) { event ->
                               EventItem(
                                   event = event,
                                   onViewClick = { onViewEventClick(event) }
                               )
                           }
                       }
                   }
               }
           }
       }
   }

   @Composable
   fun EventsTopBar(isManager: Boolean, onAddEventClick: () -> Unit) {
       Box(
           modifier = Modifier
               .fillMaxWidth()
               .padding(top = 16.dp, bottom = 8.dp)
               .padding(horizontal = 16.dp),
       ) {
           Text(
               text = "Events",
               color = Color.White,
               fontSize = 22.sp,
               fontWeight = FontWeight.Bold,
               modifier = Modifier.align(Alignment.Center)
           )
           IconButton(
               onClick = onAddEventClick,
               modifier = Modifier.align(Alignment.CenterEnd)
           ) {
               Icon(
                   imageVector = Icons.Default.Add,
                   contentDescription = "Add Event",
                   tint = Color.White
               )
           }
       }
   }

    @Composable
    fun EventItem(event: EventResponse, onViewClick: () -> Unit = {}) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFF30214A)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Event Icon",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = event.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Location: ${event.location ?: "Sin ubicación"}",
                        fontSize = 14.sp,
                        color = Color(0xFFB3B3B3)
                    )
                    Text(
                        text = "Attendees: ${event.recipientIds.size}",
                        fontSize = 14.sp,
                        color = Color(0xFFB3B3B3)
                    )
                    val dateStr = formatDateSafe(event.date)
                    Text(
                        text = dateStr,
                        fontSize = 14.sp,
                        color = Color(0xFFB3B3B3)
                    )
                }
            }
            Button(
                onClick = onViewClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF302149)
                ),
                modifier = Modifier
                    .height(40.dp)
                    .width(90.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Details",
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }