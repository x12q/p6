package com.qxdzbc.p6.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.qxdzbc.p6.ui.theme.color.P6Color
import com.qxdzbc.p6.ui.theme.color.UIColor
import com.qxdzbc.p6.ui.theme.language.AppLanguage
import com.qxdzbc.p6.ui.theme.typography.P6TypoGraphy

object P6Theme {
    val color: P6Color
        @Composable get() = P6Color.local.current

    val typography: P6TypoGraphy
        @Composable get() = P6TypoGraphy.local.current

    val shape: P6Shape
        @Composable get() = P6Shape.local.current

    val canvas: Canvas = Canvas

    val icons:Icons =Icons
}

@Composable
fun P6Theme(
    isDark: Boolean = false,
    content: @Composable () -> Unit,
) {
    val color = if (isDark) {
        P6Color.dark()
    } else {
        P6Color.light()
    }

    val typography = if (isDark) {
        P6TypoGraphy.dark()
    } else {
        P6TypoGraphy.light()
    }

    val uiColor = if(isDark){
        UIColor.dark()
    }else{
        UIColor.light()
    }

    AppLanguage{
        UseP6TextSelectionColor {
            CompositionLocalProvider(
                P6Color.local provides color,
                UIColor.local provides uiColor,
                P6TypoGraphy.local provides typography,
                Canvas.local provides Canvas,
                content = content
            )
        }
    }
}