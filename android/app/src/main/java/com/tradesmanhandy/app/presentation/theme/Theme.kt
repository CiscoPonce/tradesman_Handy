package com.tradesmanhandy.app.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFFB800), // Yellow for primary actions
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFFFB800), // Yellow for containers
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFFFFB800),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFFB800),
    onSecondaryContainer = Color.Black,
    tertiary = Color(0xFFFFB800),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFFB800), // Yellow for booking sections
    onTertiaryContainer = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color.White,
    onSurfaceVariant = Color.Black,
    outline = Color.Gray
)

private val DarkColorScheme = LightColorScheme // Using light scheme for dark theme too to maintain consistency

@Composable
fun TradesmanHandyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // Always use light scheme for consistency
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
