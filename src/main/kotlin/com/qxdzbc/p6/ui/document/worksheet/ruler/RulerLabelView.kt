package com.qxdzbc.p6.ui.document.worksheet.ruler

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.qxdzbc.p6.ui.common.compose.TestApp
import com.qxdzbc.p6.ui.common.view.MBox

@Composable
fun RulerLabelView(
    label: String,
    itemModifier: Modifier = Modifier,
) {
    MBox( modifier = itemModifier.fillMaxSize()) {
        BasicText(label, modifier = Modifier.align(Alignment.Center))
    }
}


fun main() {
    TestApp {
        RulerLabelView("AAAA")
    }
}
