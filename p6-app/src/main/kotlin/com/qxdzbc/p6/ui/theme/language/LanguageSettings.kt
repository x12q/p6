package com.qxdzbc.p6.ui.theme.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


object AppLanguage {
    val uiText: P6UIText
        @Composable get() = P6UIText.local.current
}

@Composable
fun AppLanguage(
    uiLanguage: UILanguage = UILanguage.English,
    content: @Composable () -> Unit,
) {
    val uiText = when (
        uiLanguage
    ) {
        UILanguage.English -> P6UIText.english()
        else -> P6UIText.english()
    }

    CompositionLocalProvider(
        P6UIText.local provides uiText,
        content = content
    )
}