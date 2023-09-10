package com.droidcon.wealthbuddy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

/*private val DarkColorScheme = darkColorScheme(
    primary = LightTheme.colorPrimary,
    onPrimary = LightTheme.colorOnPrimary,
    secondary = LightTheme.colorSecondary,
    onSecondary = LightTheme.colorOnSecondary,
    tertiary = LightTheme.colorTertiary,
    onTertiary = LightTheme.colorOnTertiary
)

private val LightColorScheme = lightColorScheme(
    primary = DarkTheme.colorPrimary,
    onPrimary = DarkTheme.colorOnPrimary,
    secondary = DarkTheme.colorSecondary,
    onSecondary = DarkTheme.colorOnSecondary,
    tertiary = DarkTheme.colorTertiary,
    onTertiary = DarkTheme.colorOnTertiary

    *//* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    *//*
)*/

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> ColorBankContainer.toColorScheme(darkTheme)
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
