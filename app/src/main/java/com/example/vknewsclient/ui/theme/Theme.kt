package com.example.vknewsclient.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
//    primary = BlackDark,
//    secondary = BlackDark,
//    tertiary = BlackDark,
//    background = BlackDark,
//    surface = BlackLight,
//    onPrimary = Color.White,
//    onSecondary = Accent,
//    onTertiary = GrayDark,
//    onBackground = Color.White,
//    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
//    primary = WhiteDark,
//    secondary = WhiteDark,
//    tertiary = WhiteDark,
//    background = WhiteDark,
//    surface = WhiteLight,
//    onPrimary = Color.Black,
//    onSecondary = Accent,
//    onTertiary = GrayLight,
//    onBackground = Color.Black,
//    onSurface = Color.Black
)

@Composable
fun VkNewsClientTheme(
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

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}