package com.example.centralis_kotlin.events.presentation.views

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.rememberCoroutineScope

import kotlinx.coroutines.launch
import com.example.centralis_kotlin.common.RetrofitClient
import com.example.centralis_kotlin.common.SharedPreferencesManager
import com.example.centralis_kotlin.events.model.EventResponse

import java.util.UUID

sealed class Screen(val route: String) {
    object Events : Screen("events")
    object CreateEvent : Screen("create_event")
    object EventDetails : Screen("event_details")
    object UpdateEvent : Screen("update_event")
}

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Events.route,
        modifier = modifier
    ) {
        composable(Screen.Events.route) {
            EventsScreen(
                onAddEventClick = { navController.navigate(Screen.CreateEvent.route) },
                onViewEventClick = { event: EventResponse ->
                    navController.navigate("${Screen.EventDetails.route}/${event.id}")
                }
            )
        }
       composable(Screen.CreateEvent.route) {
           val scope = rememberCoroutineScope()
           val context = LocalContext.current
           val token = SharedPreferencesManager(context).getToken()
           val authHeader = "Bearer $token"
           val profileWebService = RetrofitClient.profileWebService
           val currentUserIdStr = SharedPreferencesManager(context).getUserId()
           val currentUserId = currentUserIdStr?.let { UUID.fromString(it) } ?: UUID.randomUUID()

           CreateEventScreen(
               onCreate = { newEventRequest ->
                   scope.launch {
                       try {
                           val resp = RetrofitClient.eventApiService.createEvent(
                               authHeader, newEventRequest)
                           if (resp.isSuccessful) {
                               navController.popBackStack(Screen.Events.route, inclusive = false)
                           } else {
                               navController.popBackStack()
                           }
                       } catch (e: Exception) {
                           navController.popBackStack()
                       }
                   }
               },
               onCancel = { navController.popBackStack() },
               profileWebService = profileWebService,
               authorization = authHeader,
               currentUserId = currentUserId
           )
        }

        composable("${Screen.EventDetails.route}/{eventId}") { backStackEntry ->
            val context = LocalContext.current
            val eventIdStr = backStackEntry.arguments?.getString("eventId") ?: ""
            val scope = rememberCoroutineScope()
            var isLoading by remember { mutableStateOf(true) }
            var error by remember { mutableStateOf<String?>(null) }

            var title by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var location by remember { mutableStateOf("") }
            var attendees by remember { mutableStateOf(0) }
            var dateTime by remember { mutableStateOf("") }

            LaunchedEffect(eventIdStr) {
                isLoading = true
                error = null
                try {
                    val token = SharedPreferencesManager(context).getToken()
                    val authHeader = "Bearer $token"
                    val uuid = UUID.fromString(eventIdStr)
                    val resp = RetrofitClient.eventApiService.getEventById(authHeader, uuid)
                    if (resp.isSuccessful) {
                        val ev = resp.body()
                        if (ev != null) {
                            title = ev.title
                            description = ev.description
                            location = ev.location ?: ""
                            attendees = ev.recipientIds.size
                            dateTime = try { ev.date.toString() } catch (_: Exception) { "" }
                        } else {
                            error = "Evento no encontrado"
                        }
                    } else {
                        error = "Error ${resp.code()}: ${resp.message()}"
                    }
                } catch (e: Exception) {
                    error = "Error de conexión: ${e.message}"
                } finally {
                    isLoading = false
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(error ?: "Error desconocido", color = Color.Red)
                }
            } else {

                EventDetailsScreen(
                    title = title,
                    description = description,
                    location = location,
                    attendees = attendees,
                    dateTime = dateTime,
                    onEditEvent = { navController.navigate("${Screen.UpdateEvent.route}/$eventIdStr") },
                    onDeleteEvent = {
                        scope.launch {
                            val token = SharedPreferencesManager(context).getToken()
                            val authHeader = "Bearer $token"
                            val uuid = UUID.fromString(eventIdStr)
                            val resp = RetrofitClient.eventApiService.deleteEvent(authHeader, uuid)
                            if (resp.isSuccessful) {
                                navController.popBackStack(Screen.Events.route, inclusive = false)
                            }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable("${Screen.UpdateEvent.route}/{eventId}") { backStackEntry ->
            val context = LocalContext.current
            val eventIdStr = backStackEntry.arguments?.getString("eventId") ?: ""
            val scope = rememberCoroutineScope()
            val token = SharedPreferencesManager(context).getToken()
            val authHeader = "Bearer $token"

            var isLoading by remember { mutableStateOf(true) }
            var currentTitle by remember { mutableStateOf("") }
            var currentDescription by remember { mutableStateOf("") }
            var currentDateTime by remember { mutableStateOf("") }
            var currentLocation by remember { mutableStateOf<String?>(null) }
            var ev by remember { mutableStateOf<EventResponse?>(null) }

            LaunchedEffect(eventIdStr) {
                try {
                    val uuid = UUID.fromString(eventIdStr)
                    val resp = RetrofitClient.eventApiService.getEventById(authHeader, uuid)
                    if (resp.isSuccessful) {
                        ev = resp.body()
                        if (ev != null) {
                            currentTitle = ev?.title ?: ""
                            currentDescription = ev?.description ?: ""
                            currentDateTime = ev?.date ?: ""
                            currentLocation = ev?.location
                        }
                    }
                } finally {
                    isLoading = false
                }
            }

            if (!isLoading && ev != null) {
                UpdateEventScreen(
                    eventId = UUID.fromString(eventIdStr),
                    currentTitle = currentTitle,
                    currentDescription = currentDescription,
                    currentDateTime = currentDateTime,
                    currentLocation = currentLocation,
                    initialRecipients = ev?.recipientIds ?: emptyList(),
                    onUpdate = { eventId, updateRequest ->
                        scope.launch {
                            try {
                                val resp = RetrofitClient.eventApiService.updateEvent(authHeader, eventId, updateRequest)
                                if (resp.isSuccessful) {
                                    navController.popBackStack()
                                }
                            } catch (e: Exception) { }
                        }
                    },
                    onBack = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() },
                    profileWebService = RetrofitClient.profileWebService,
                    authorization = authHeader
                )
            }
        }
    }
}