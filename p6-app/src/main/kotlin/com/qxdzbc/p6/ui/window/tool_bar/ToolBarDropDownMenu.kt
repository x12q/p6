package com.qxdzbc.p6.ui.window.tool_bar

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.view.CenterAlignRow
import com.qxdzbc.p6.ui.common.view.buttons.DropDownButton

@Composable
fun ToolBarDropDownMenuWithButton(
    expanded: Boolean,
    header: @Composable () -> Unit,
    onButtonClick: () -> Unit,
    items: @Composable ColumnScope.() -> Unit,
    onDismiss: () -> Unit,
) {
    ToolBarDropDownMenu(
        expanded = expanded,
        header = {
            CenterAlignRow {
                header()
                DropDownButton(
                    enabled = !expanded,
                    modifier = Modifier.size(30.dp),
                    onClick = onButtonClick
                )
            }
        },
        items = {
            items()
        },
        onDismiss = onDismiss
    )
}


@Composable
fun ToolBarDropDownMenu(
    expanded: Boolean,
    header: @Composable () -> Unit,
    items: @Composable ColumnScope.() -> Unit,
    onDismiss: () -> Unit,
) {
    CenterAlignRow {
        header()
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        items(this)
    }
}

