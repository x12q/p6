package com.qxdzbc.p6.ui.window.tool_bar

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.view.MRow
import com.qxdzbc.p6.ui.common.view.buttons.DropDownButton

@Composable
fun ToolBarDropDownMenuWithButton(
    header: @Composable () -> Unit,
    items: @Composable ColumnScope.(expandedMs: Ms<Boolean>) -> Unit,
) {
    ToolBarDropDownMenu(
        header = { expandedMs ->
            MRow {
                header()
                DropDownButton(
                    enabled = !expandedMs.value,
                    modifier = Modifier.size(30.dp),
                    onClick = {
                        expandedMs.value = true
                    }
                )
            }
        },
        items = { expandedMs ->
            DropdownMenu(
                expanded = expandedMs.value,
                onDismissRequest = {
                    expandedMs.value = false
                },
            ) {
                items(this, expandedMs)
            }
        }
    )
}


@Composable
fun ToolBarDropDownMenu(
    header: @Composable (expandedMs: Ms<Boolean>) -> Unit,
    items: @Composable ColumnScope.(expandedMs: Ms<Boolean>) -> Unit,
) {
    val expandedMs = rms(false)
    var expanded by expandedMs
    MRow {
        header(expandedMs)
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        },
    ) {
        items(this, expandedMs)
    }
}

