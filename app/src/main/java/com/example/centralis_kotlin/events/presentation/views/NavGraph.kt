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
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.centralis_kotlin.common.RetrofitClient
import com.example.centralis_kotlin.common.SharedPreferencesManager
import com.example.centralis_kotlin.events.model.EventResponse
import com.example.centralis_kotlin.common.di.DependencyFactory
import com.example.centralis_kotlin.profile.models.ProfileResponse
import com.example.centralis_kotlin.profile.models.Position
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

sealed class Screen(val route: String) {
    object Events : Screen("events")
    object CreateEvent : Screen("create_event")
    object EventDetails : Screen("event_details")
    object UpdateEvent : Screen("update_event")
}

private fun String?.toAuthHeader(): String {
    return this?.let { if (it.startsWith("Bearer ")) it else "Bearer $it" } ?: ""
}

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Events.route,
        modifier = modifier
    ) {
        composable(Screen.Events.route) {
            val context = LocalContext.current
            val token = SharedPreferencesManager(context).getToken()
            val authHeader = token.toAuthHeader()
            val scope = rememberCoroutineScope()
            var isLoading by remember { mutableStateOf(true) }
            var fallbackIsManager by remember { mutableStateOf(false) }

            val currentUserIdStr = SharedPreferencesManager(context).getUserId()

            LaunchedEffect(currentUserIdStr) {
                isLoading = true
                try {
                    val resp = try {
                        RetrofitClient.profileWebService.getAllProfiles(authHeader)
                    } catch (_: Exception) {
                        null
                    }
                    val profiles = resp?.body() ?: emptyList<ProfileResponse>()
                    val myProfile = currentUserIdStr?.let { id -> profiles.find { it.userId == id } }
                    fallbackIsManager = myProfile?.position == Position.MANAGER
                } finally {
                    isLoading = false
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                EventsScreen(
                    isManager = fallbackIsManager,
                    onAddEventClick = { navController.navigate(Screen.CreateEvent.route) },
                    onViewEventClick = { event: EventResponse ->
                        navController.navigate("${Screen.EventDetails.route}/${event.id}")
                    }
                )
            }
        }

        composable(Screen.CreateEvent.route) {
            val context = LocalContext.current
            val token = SharedPreferencesManager(context).getToken()
            val authHeader = token.toAuthHeader()
            val profileWebService = RetrofitClient.profileWebService
            val currentUserIdStr = SharedPreferencesManager(context).getUserId()
            val currentUserId = currentUserIdStr?.let { UUID.fromString(it) } ?: UUID.randomUUID()

            val eventViewModel = remember { DependencyFactory.createEventViewModel(context) }

            CreateEventScreen(
                eventViewModel = eventViewModel,
                onCancel = { navController.popBackStack() },
                onSuccess = {
                    navController.popBackStack(Screen.Events.route, inclusive = false)
                },
                profileWebService = profileWebService,
                authorization = authHeader,
                currentUserId = currentUserId
            )
        }

        composable(
            route = "${Screen.EventDetails.route}/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val context = LocalContext.current
            val eventIdStr = backStackEntry.arguments?.getString("eventId") ?: ""
            val scope = rememberCoroutineScope()
            var isLoading by remember { mutableStateOf(true) }
            var error by remember { mutableStateOf<String?>(null) }

            var title by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var location by remember { mutableStateOf("") }
            var dateTime by remember { mutableStateOf("") }
            var attendeesProfiles by remember { mutableStateOf<List<ProfileResponse>>(emptyList()) }
            var isManager by remember { mutableStateOf(false) }

            LaunchedEffect(eventIdStr) {
                isLoading = true
                error = null
                try {
                    val token = SharedPreferencesManager(context).getToken()
                    val authHeader = token.toAuthHeader()
                    val uuid = UUID.fromString(eventIdStr)
                    val resp = RetrofitClient.eventApiService.getEventById(authHeader, uuid)
                    if (resp.isSuccessful) {
                        val ev = resp.body()
                        if (ev != null) {
                            title = ev.title
                            description = ev.description
                            location = ev.location ?: ""
                            dateTime = ev.date

                            val profilesResp = RetrofitClient.profileWebService.getAllProfiles(authHeader)
                            val allProfiles = if (profilesResp.isSuccessful) {
                                profilesResp.body() ?: emptyList()
                            } else emptyList()

                            val recipientIdStrings = ev.recipientIds.map { it.toString() }.toSet()
                            attendeesProfiles = allProfiles.filter { p ->
                                val uid = p.userId
                                uid in recipientIdStrings || (ev.createdBy.toString() == uid)
                            }

                            val currentUserIdStr = SharedPreferencesManager(context).getUserId()
                            val currentProfile = allProfiles.find { it.userId == currentUserIdStr }
                            isManager = currentProfile?.position == Position.MANAGER || (ev.createdBy.toString() == currentUserIdStr)
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
                    CircularProgressIndicator(color = Color.White)
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
                    attendeesProfiles = attendeesProfiles,
                    dateTime = dateTime,
                    isManager = isManager,
                    onEditEvent = { navController.navigate("${Screen.UpdateEvent.route}/$eventIdStr") },
                    onDeleteEvent = {
                        scope.launch {
                            val token = SharedPreferencesManager(context).getToken()
                            val authHeader = token.toAuthHeader()
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

        composable(
            route = "${Screen.UpdateEvent.route}/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val context = LocalContext.current
            val eventIdStr = backStackEntry.arguments?.getString("eventId") ?: ""
            val scope = rememberCoroutineScope()
            val token = SharedPreferencesManager(context).getToken()
            val authHeader = token.toAuthHeader()

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
                        val fetched = resp.body()
                        ev = fetched
                        currentTitle = fetched?.title ?: ""
                        currentDescription = fetched?.description ?: ""
                        currentDateTime = fetched?.date ?: ""
                        currentLocation = fetched?.location
                    }
                } catch (_: Exception) { }
                finally {
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
                            } catch (_: Exception) { }
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