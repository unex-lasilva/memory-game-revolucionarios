package com.example.memorygamerevolucionarios.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color.White,
    surface = Color(0xFFF8F8F8),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun MemoryGameRevolucionariosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    hideNavigationBar: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    LocalView.current.let { view ->
        if (!view.isInEditMode) {
            SideEffect {
                (view.context as Activity).window.let { window ->
                    window.statusBarColor = colorScheme.primary.toArgb()
                    WindowCompat.getInsetsController(window, view).apply {
                        isAppearanceLightStatusBars = !darkTheme
                        if (hideNavigationBar) {
                            hide(WindowInsetsCompat.Type.navigationBars())
                            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        } else {
                            show(WindowInsetsCompat.Type.navigationBars())
                        }
                    }
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}