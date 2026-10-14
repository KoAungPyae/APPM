package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CinematicDarkColorScheme = darkColorScheme(
    primary = StudioPurple,
    onPrimary = StudioBackground,
    primaryContainer = StudioPurpleDark,
    onPrimaryContainer = StudioPurpleLight,

    secondary = StudioBlue,
    onSecondary = StudioBackground,
    secondaryContainer = StudioBlueDark,
    onSecondaryContainer = StudioBlueLight,

    tertiary = StudioCyan,
    onTertiary = StudioBackground,
    tertiaryContainer = StudioSurfaceElevated,
    onTertiaryContainer = StudioCyan,

    background = StudioBackground,
    onBackground = StudioTextPrimary,

    surface = StudioSurface,
    onSurface = StudioTextPrimary,
    surfaceVariant = StudioSurfaceVariant,
    onSurfaceVariant = StudioTextSecondary,
    surfaceContainerHigh = StudioSurfaceElevated,

    outline = StudioBorder,
    outlineVariant = StudioBorder.copy(alpha = 0.5f),

    error = StudioRose,
    onError = StudioBackground
)

@Composable
fun VideoStudioTheme(
    darkTheme: Boolean = true, // Always default to cinematic dark theme for creative studio look
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CinematicDarkColorScheme,
        typography = Typography,
        content = content
    )
}
