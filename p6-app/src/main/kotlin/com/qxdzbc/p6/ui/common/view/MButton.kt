package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.testApp

import com.qxdzbc.p6.ui.theme.P6Theme

/**
 * Standard button used in the entire app
 */
@Composable
fun MButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Button(
        onClick = onClick,
        shape = P6Theme.shape.textFieldShape,
        modifier = modifier.height(24.dp).widthIn(71.dp),
        contentPadding = PaddingValues(horizontal = 5.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = P6Theme.color.uiColor.enabledButton,
        ),
        border = BorderStroke(1.dp, P6Theme.color.uiColor.buttonBorder)
    ) {
        content()
    }
}

@Composable
fun MButton(
    label: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
) {
    MButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(label)
    }
}

fun main() {
    testApp {
        P6Theme  {
            Column {
                MButton {
                    BasicText("Ok")
                }
                Divider()
                MButton {
                    BasicText("Cancel")
                }
                Divider()
                MButton {
                    BasicText("Apply")
                }
            }
        }
    }
}
