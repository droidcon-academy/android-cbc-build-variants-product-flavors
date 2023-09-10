package com.droidcon.wealthbuddy.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

interface ColorBank {
    val primary: Color
    val onPrimary: Color
    val primaryContainer: Color
    val onPrimaryContainer: Color
    val secondary: Color
    val onSecondary: Color
    val secondaryContainer: Color
    val onSecondaryContainer: Color
    val tertiary: Color
    val onTertiary: Color
    val tertiaryContainer: Color
    val background: Color
    val onBackground: Color

    val primaryDark: Color
    val onPrimaryDark: Color
    val primaryContainerDark: Color
    val onPrimaryContainerDark: Color
    val secondaryDark: Color
    val onSecondaryDark: Color
    val secondaryContainerDark: Color
    val onSecondaryContainerDark: Color
    val tertiaryDark: Color
    val onTertiaryDark: Color
    val tertiaryContainerDark: Color
    val backgroundDark: Color
    val onBackgroundDark: Color

    fun toColorScheme(isDarkTheme: Boolean) = if (isDarkTheme)
        darkColorScheme(
            primary = primaryDark,
            onPrimary = onPrimaryDark,
            primaryContainer = primaryContainerDark,
            onPrimaryContainer = onPrimaryContainerDark,
            secondary = secondaryDark,
            onSecondary = onSecondaryDark,
            secondaryContainer = secondaryContainerDark,
            onSecondaryContainer = onSecondaryContainerDark,
            tertiary = tertiaryDark,
            onTertiary = onTertiaryDark,
            tertiaryContainer = tertiaryContainerDark,
            background = backgroundDark,
            onBackground = onBackgroundDark
        )
    else lightColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        background = background,
        onBackground = onBackground
    )

}

//object DefaultColorBank : ColorBank {
//    override val primary = Color(0xFF6750A4)
//    override val onPrimary = Color(0xFFFFFFFF)
//    override val primaryContainer = Color(0xFFEADDFF)
//    override val onPrimaryContainer = Color(0xFF21005D)
//    override val secondary = Color(0xFF625B71)
//    override val onSecondary = Color(0xFFFFFFFF)
//    override val secondaryContainer = Color(0xFFE8DEF8)
//    override val onSecondaryContainer = Color(0xFF1D192B)
//    override val tertiary = Color(0xFF7D5260)
//    override val onTertiary = Color(0xFFFFFFFF)
//    override val tertiaryContainer = Color(0xFFFFD8E4)
//    override val background = Color(0xFFFFFBFE)
//    override val onBackground = Color(0xFF1C1B1F)
//
//    override val primaryDark = Color(0xFFD0BCFF)
//    override val onPrimaryDark = Color(0xFF381E72)
//    override val primaryContainerDark = Color(0xFF4F378B)
//    override val onPrimaryContainerDark = Color(0xFFEADDFF)
//    override val secondaryDark = Color(0xFFCCC2DC)
//    override val onSecondaryDark = Color(0xFF332D41)
//    override val secondaryContainerDark = Color(0xFF4A4458)
//    override val onSecondaryContainerDark = Color(0xFFE8DEF8)
//    override val tertiaryDark = Color(0xFFEFB8C8)
//    override val onTertiaryDark = Color(0xFF492532)
//    override val tertiaryContainerDark = Color(0xFF633B48)
//    override val backgroundDark = Color(0xFF1C1B1F)
//    override val onBackgroundDark = Color(0xFFE6E1E5)
//}
