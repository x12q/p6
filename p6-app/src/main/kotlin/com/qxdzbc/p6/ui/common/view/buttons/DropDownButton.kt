package com.qxdzbc.p6.ui.common.view.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


/**
 * Drop down button for the header of a drop down menu
 */
@Composable
fun DropDownButton(
    enabled: Boolean = true,
    modifier: Modifier=Modifier,
    onClick: () -> Unit
) {
    IconBox(
        icon = Icons.Filled.ArrowDropDown,
        modifier = modifier,
        enable = enabled,
        onClick = onClick,
    )
//    MIconButton(
//        icon = Icons.Filled.ArrowDropDown,
//        onClick = onClick,
//        enabled = enabled,
//    )
}
