package com.qxdzbc.p6.ui.worksheet.ruler

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.common.compose.view.testApp

@Composable
fun RulerLabel(
    label: String,
    itemModifier: Modifier = Modifier,
) {
    MBox( modifier = itemModifier.fillMaxSize()) {
        BasicText(label, modifier = Modifier.align(Alignment.Center))
    }
}


fun main() {
    testApp {
        RulerLabel("AAAA")
    }
}
