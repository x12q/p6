package com.emeraldblast.p6.ui.common.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emeraldblast.p6.ui.common.compose.testApp

/**
 * X button to close a window
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun XButton(
    onClick: () -> Unit = {},
) {
    Button(onClick = onClick, modifier = Modifier
        .size(30.dp)
    ) {
        Text("X")
    }

}

fun main() {
    testApp {
        XButton()
    }
}
