package com.qxdzbc.p6.ui.window.workbook_tab.tab.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.qxdzbc.p6.ui.theme.common.P6CommonUIModifiers

@Composable
fun WorkbookTabNameText(
    text:String
) {
    Text(
        text = text,
        modifier = Modifier
            .then(P6CommonUIModifiers.smallBoxPadding),
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}