package com.emeraldblast.p6.ui.common.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.emeraldblast.p6.ui.theme.P6DefaultTypoGraphy
import com.emeraldblast.p6.ui.theme.P6LightColors2

val PointerKeyboardModifiers.areAnyPressed: Boolean
    get() = isCtrlPressed || isAltPressed || isFunctionPressed || isAltGraphPressed || isShiftPressed || isMetaPressed || isFunctionPressed || isSymPressed
val PointerKeyboardModifiers.isNonePressed: Boolean
    get() = !areAnyPressed



fun makeRect(point1: Offset, point2: Offset): Rect {
    return Rect(
        left = minOf(point1.x, point2.x),
        right = maxOf(point1.x, point2.x),
        top = minOf(point1.y, point2.y),
        bottom = maxOf(point1.y, point2.y)
    )
}

fun addTestTag(enableTestTag: Boolean, tag: String): Modifier {
    return if (enableTestTag) Modifier.testTag(tag) else Modifier
}

fun testApp(size: DpSize = DpSize(1500.dp, 600.dp), content: @Composable ApplicationScope.() -> Unit) = application {
    val appScope = this
    val windowState = rememberWindowState(size = size)
    Window(onCloseRequest = ::exitApplication, windowState) {
        MaterialTheme(colors = P6LightColors2, typography = P6DefaultTypoGraphy){
            content(appScope)
        }
    }
}


