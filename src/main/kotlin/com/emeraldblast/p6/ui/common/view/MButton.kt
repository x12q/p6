package com.emeraldblast.p6.ui.common.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emeraldblast.p6.ui.common.R
import com.emeraldblast.p6.ui.common.compose.TestApp
import com.emeraldblast.p6.ui.theme.P6AllWhiteColors

/**
 * Standard button
 */
@Composable
fun MButton(onClick: () -> Unit = {}, modifier: Modifier = Modifier, content: @Composable () -> Unit = {}) {
    Button(
        onClick = onClick,
        shape = R.shape.buttonShape,
        modifier = modifier.size(71.dp, 24.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colors.onSecondary)
    ) {
        content()
    }
}

fun main() {
    TestApp {
        MaterialTheme(colors =  P6AllWhiteColors) {
            Column {
                MButton {
                    BasicText("Ok")
                }
                Divider()
                MButton() {
                    BasicText("Cancel")
                }
                Divider()
                MButton() {
                    BasicText("Apply")
                }
            }
        }
    }
}
