package com.example.ytindex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.ytindex.data.local.AppThemeMode

private val LightColorScheme = lightColorScheme(
    primary = PrimaryTeal,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryTealLight,
    surface = SurfaceLight,
    surfaceContainer = SurfaceContainerLight,
    onSurface = OnSurfaceLight,
    outline = OutlineLight,
    error = ErrorRed,
    errorContainer = ErrorContainer,
    onError = OnError
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryTealLight,
    onPrimary = OnSurfaceLight,
    primaryContainer = PrimaryTealDark,
    surface = SurfaceDark,
    surfaceContainer = SurfaceContainerDark,
    onSurface = OnSurfaceDark,
    outline = OutlineDark,
    error = ErrorRed,
    errorContainer = ErrorContainer,
    onError = OnError
)

@Composable
fun YTIndexTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
