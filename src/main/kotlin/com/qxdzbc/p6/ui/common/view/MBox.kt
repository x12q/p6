package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * The purpose of this Composable is to create a "strong" separation that prevents unnecessary re-composition.
 */
@Composable
fun MBox(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit = {}) {
    Box(modifier = modifier) {
        content()
    }
}
