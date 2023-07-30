package com.qxdzbc.p6.ui.window.workbook_tab.tab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.p6.ui.theme.P6Theme

@Composable
fun WorkbookTabCloseButton(
    onClick:()->Unit,
) {
    BorderBox(
        borderStyle = BorderStyle.ALL,
        modifier = Modifier
            .background(P6Theme.color.uiColor.enabledButton)
            .size(DpSize(18.dp, 18.dp))
            .clickable {
                onClick()
            }

    ) {
        Icon(
            Icons.Filled.Close,
            contentDescription = "Localized description"
        )
    }
}