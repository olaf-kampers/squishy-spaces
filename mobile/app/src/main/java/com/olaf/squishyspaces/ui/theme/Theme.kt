package com.olaf.squishyspaces.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SquishyTealLight,
    secondary = SquishyLilacLight,
    tertiary = SquishyLilacLight,
)

private val LightColorScheme = lightColorScheme(
    primary = SquishyTeal,
    onPrimary = Color.White,
    secondary = SquishyLilac,
    onSecondary = Color.White,
    tertiary = SquishyLilacLight,
    background = SquishyNeutral,
    surface = Color.White,
    onBackground = SquishyOnDark,
    onSurface = SquishyOnDark,
)

@Composable
fun SquishySpacesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color overrides the Squishy palette — disabled intentionally
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
