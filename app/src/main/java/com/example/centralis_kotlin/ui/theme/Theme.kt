package com.example.centralis_kotlin.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val CentralisDarkColorScheme = darkColorScheme(
    primary = CentralisPrimary,
    secondary = CentralisPlaceholder,
    tertiary = Pink80,
    background = CentralisBackground,
    surface = CentralisSurface,
    onPrimary = CentralisOnPrimary,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = CentralisOnBackground,
    onSurface = CentralisOnBackground
)

private val CentralisLightColorScheme = lightColorScheme(
    primary = CentralisPrimary,
    secondary = CentralisPlaceholder,
    tertiary = Pink40,
    background = CentralisBackground,
    surface = CentralisSurface,
    onPrimary = CentralisOnPrimary,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = CentralisOnBackground,
    onSurface = CentralisOnBackground
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = CentralisSecondary,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun CentraliskotlinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Deshabilitado para usar colores de Centralis
    content: @Composable () -> Unit
) {
    // Siempre usar el esquema de colores de Centralis
    val colorScheme = if (darkTheme) CentralisDarkColorScheme else CentralisLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}