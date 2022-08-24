package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.compose.TestApp


/**
 * A column that keeps track of an index number than can be increase with arrow down key, and decrease with arrow up key.
 * This index can be used as indicator of the currently selected value.
 * The index is default to 0 at the start.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SingleCursorNavigableColumn(
    startIndex: Int = 0,
    endIndex: Int,
    content: @Composable ColumnScope.(currentIndex: Int) -> Unit
) {
    var itemIndex by rms(startIndex)
    val fr = remember { FocusRequester() }
    Column(modifier = Modifier.focusRequester(fr).focusable(true).onPreviewKeyEvent {
            if (it.type == KeyEventType.KeyDown) {
                when (it.key) {
                    Key.DirectionDown -> {

                            itemIndex = minOf(itemIndex+1,endIndex)

                        true
                    }
                    Key.DirectionUp -> {
                        itemIndex = maxOf(itemIndex-1, 0)
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }) {
        content(itemIndex)
    }
    DisposableEffect(Unit) {
        fr.requestFocus()
        onDispose { }
    }
}

fun main() {
    TestApp {
        val l = remember { (1..6).toList() }
        SingleCursorNavigableColumn(3, l.size - 1) { currentIndex ->
            for ((id, i) in l.withIndex()) {
                MBox(modifier = Modifier.background(if (id == currentIndex) Color.Cyan else Color.Transparent)) {
                    println("render ${i}")
                    Text("number ${i}")
                }
            }
        }
    }
}
