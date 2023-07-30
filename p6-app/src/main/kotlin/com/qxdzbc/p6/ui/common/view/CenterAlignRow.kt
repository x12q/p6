package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.HSpacer
import com.qxdzbc.common.compose.view.testApp

/**
 * Identical to [Row] but with [verticalAlignment] default to center.
 */
@Composable
fun CenterAlignRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement=horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        content()
    }
}


fun main(){
    testApp {
        BorderBox {
            CenterAlignRow(Modifier.height(150.dp)){
                Text("A1")
                HSpacer(30.dp)
                Text("B1")
            }
        }
    }
}

