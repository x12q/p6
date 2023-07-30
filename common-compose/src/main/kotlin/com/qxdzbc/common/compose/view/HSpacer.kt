package com.qxdzbc.common.compose.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * Horizontal spacer that stretches horizontally
 */
@Composable
fun HSpacer(width: Dp, modifier: Modifier = Modifier) {
    Spacer(Modifier.width(width).then(modifier))
}