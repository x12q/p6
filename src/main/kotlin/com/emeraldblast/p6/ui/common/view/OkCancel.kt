package com.emeraldblast.p6.ui.common.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.emeraldblast.p6.ui.common.R
import com.emeraldblast.p6.ui.common.compose.TestApp


@Composable
fun OkButton(
    modifier: Modifier = Modifier,
    onOk: () -> Unit = {},
) {
    MButton(onClick = onOk,modifier=modifier){
        BasicText("OK")
    }
}

@Composable
fun CancelButton(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = {},
) {
    MButton(onClick = onCancel,modifier=modifier){
        BasicText("Cancel")
    }
}

/**
 * A panel of Ok and Cancel button
 */
@Composable
fun OkCancel(
    okModifier: Modifier = Modifier,
    cancelModifier: Modifier = Modifier,
    onOk: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    Row {
        OkButton(okModifier, onOk)
        Spacer(modifier = Modifier.width(R.padding.value.betweenButton))
        CancelButton(cancelModifier, onCancel)
    }
}


fun main() {
    TestApp {
        OkCancel()
    }
}
