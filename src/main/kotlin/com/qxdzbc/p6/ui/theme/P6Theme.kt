package com.qxdzbc.p6.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val c = P6Color
val P6DarkColors = darkColors(
    primary = c.Blue80,
    onPrimary = c.Blue20,
    secondary = c.DarkBlue80,
    onSecondary = c.DarkBlue20,
    error = c.Red80,
    onError = c.Red20,
    background = c.Grey10,
    onBackground = c.Grey90,
    surface = c.Grey10,
    onSurface = c.Grey80,
)

val P6GrayColors = darkColors(
    primary = c.Gray,
    primaryVariant = c.DarkGray,
    onPrimary = c.Black,
    secondary = c.Cyan,
    secondaryVariant = c.Cyan,
    onSecondary = c.Black,
    error = c.Red80,
    onError = c.White,
    background = c.LightGray,
    onBackground = c.Black,
    surface = c.LightGray,
    onSurface = c.Black,
)


val P6AllWhiteColors = darkColors(
    primary = c.Black,
    primaryVariant = c.White,
    onPrimary = c.Black,
    secondary = c.White,
    secondaryVariant = c.White,
    onSecondary = c.Black,
    error = c.Red40,
    onError = c.White,
    background = c.White,
    onBackground = c.Black,
    surface = c.White,
    onSurface = c.White,
)


val P6LightColors = lightColors(
    primary = c.Blue40,
    onPrimary = Color.White,
    secondary = c.DarkBlue40,
    onSecondary = Color.White,
    error = c.Red40,
    onError = Color.White,
    background = c.Grey99,
    onBackground = c.Grey10,
    surface = c.Grey99,
    onSurface = c.Grey10,
)

val P6LightColors2 = lightColors(
    primary =  Color(0xFFFAFAFA), //near-white
    primaryVariant = Color(0xFFC9C9C9), //near white
    onPrimary = Color.Black,
    secondary = Color(0xFFcfd8dc),
    secondaryVariant = Color(0xFF90a4ae),
    onSecondary = Color.Black,
    error = c.Red40,
    onError = Color.Black,
    background = c.Grey99,
    onBackground = Color.Black,
    surface = c.Grey99,
    onSurface = Color.Black,
)

enum class ThemeType {
    LIGHT, DARK, GRAY
}

@Composable
fun P6Theme(
    themeType: ThemeType = ThemeType.LIGHT,
    content: @Composable () -> Unit,
) {
    val myColorScheme = when (themeType) {
        ThemeType.DARK -> P6DarkColors
        ThemeType.LIGHT -> P6LightColors
        ThemeType.GRAY -> P6GrayColors
    }

    MaterialTheme(
        colors = myColorScheme,
        typography = P6DefaultTypoGraphy
    ) {
        content()
    }
}
