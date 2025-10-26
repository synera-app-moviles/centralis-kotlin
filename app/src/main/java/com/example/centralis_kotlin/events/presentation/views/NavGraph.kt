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
                            onViewEventClick = { event ->
                                navController.navigate("${Screen.EventDetails.route}/${event.id}")
                            }
                        )
                    }

                   composable(Screen.CreateEvent.route) {
                        val scope = rememberCoroutineScope()
                        val context = LocalContext.current

                        CreateEventScreen(
                            onCreate = { newEventRequest ->
                                scope.launch {
                                    try {
                                        val token = SharedPreferencesManager(context).getToken()
                                        val authHeader = "Bearer $token"
                                        val resp = RetrofitClient.eventApiService.createEvent(authHeader, newEventRequest)

                                        println("🔍 Response code: ${resp.code()}")
                                        println("🔍 Response body: ${resp.body()}")
                                        println("🔍 Is successful: ${resp.isSuccessful}")

                                        if (resp.isSuccessful) {
                                            println("Evento creado: ${resp.body()?.title}")
                                            navController.popBackStack(Screen.Events.route, inclusive = false)
                                        } else {
                                            println("Error creando evento: ${resp.code()}")
                                            navController.popBackStack()
                                        }
                                    } catch (e: Exception) {
                                        println("Excepción creando evento: ${e.message}")
                                        navController.popBackStack()
                                    }
                                }
                            },
                            onCancel = { navController.popBackStack() }
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

                        var isLoading by remember { mutableStateOf(true) }
                        var currentTitle by remember { mutableStateOf("") }
                        var currentDescription by remember { mutableStateOf("") }
                        var currentDateTime by remember { mutableStateOf("") }
                        var currentLocation by remember { mutableStateOf<String?>(null) }

                        LaunchedEffect(eventIdStr) {
                            try {
                                val token = SharedPreferencesManager(context).getToken()
                                val authHeader = "Bearer $token"
                                val uuid = UUID.fromString(eventIdStr)
                                val resp = RetrofitClient.eventApiService.getEventById(authHeader, uuid)
                                if (resp.isSuccessful) {
                                    val ev = resp.body()
                                    if (ev != null) {
                                        currentTitle = ev.title
                                        currentDescription = ev.description
                                        currentDateTime = ev.date
                                        currentLocation = ev.location
                                    }
                                }
                            } finally {
                                isLoading = false
                            }
                        }

                        if (!isLoading) {
                            UpdateEventScreen(
                                eventId = UUID.fromString(eventIdStr),
                                currentTitle = currentTitle,
                                currentDescription = currentDescription,
                                currentDateTime = currentDateTime,
                                currentLocation = currentLocation,
                                onUpdate = { eventId, updateRequest ->
                                    scope.launch {
                                        try {
                                            val token = SharedPreferencesManager(context).getToken()
                                            val authHeader = "Bearer $token"
                                            val resp = RetrofitClient.eventApiService.updateEvent(authHeader, eventId, updateRequest)
                                            if (resp.isSuccessful) {
                                                navController.popBackStack()
                                            }
                                        } catch (e: Exception) {

                                        }
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }