package com.qxdzbc.p6.ui.document.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.view.MBox

@Composable
fun EmptyCellView(
    boxModifier: Modifier = Modifier,
) {
    val color = Color.Transparent
    MBox(
        modifier = boxModifier
            .fillMaxSize()
            .background(color)
    ) {
    }
}
