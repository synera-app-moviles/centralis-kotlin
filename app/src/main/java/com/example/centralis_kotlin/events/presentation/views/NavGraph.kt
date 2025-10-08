package com.example.centralis_kotlin.events.presentation.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier

sealed class Screen(val route: String) {
    object Events : Screen("events")
    object CreateEvent : Screen("create_event")
    object EventDetails : Screen("event_details")
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
                    navController.navigate("${Screen.EventDetails.route}/${event.title}/${event.location}/${event.attendees}/${event.dateTime}")
                }
            )
        }

        composable(Screen.CreateEvent.route) {
            CreateEventScreen(
                onCreate = { newEvent ->
                    navController.navigate(
                        "${Screen.EventDetails.route}/${newEvent.title}/${newEvent.location}/${newEvent.attendees}/${newEvent.dateTime}"
                    ) {
                        popUpTo(Screen.Events.route) { inclusive = false }
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable(
            "${Screen.EventDetails.route}/{title}/{location}/{attendees}/{dateTime}"
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val location = backStackEntry.arguments?.getString("location") ?: ""
            val attendees = backStackEntry.arguments?.getString("attendees")?.toIntOrNull() ?: 0
            val dateTime = backStackEntry.arguments?.getString("dateTime") ?: ""

            EventDetailsScreen(
                title = title,
                location = location,
                attendees = attendees,
                dateTime = dateTime,
                onConfirmRead = { navController.navigate(Screen.Events.route) {
                    popUpTo(Screen.Events.route) { inclusive = true }
                } }
            )
        }
    }
}
