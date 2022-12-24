package com.qxdzbc.p6.ui.common.view.buttons

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.qxdzbc.common.compose.view.MBox


/**
 * Drop down button for the header of a dropdown menu
 */
@Composable
fun DropDownButton(
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    MBox(modifier = modifier.clickable {
        if (enabled) {
            onClick()
        }
    }) {
        IconBox(
            icon = Icons.Filled.ArrowDropDown,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
