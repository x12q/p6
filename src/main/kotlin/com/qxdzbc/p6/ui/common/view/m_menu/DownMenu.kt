package com.qxdzbc.p6.ui.common.view.m_menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.qxdzbc.p6.ui.common.compose.StateUtils.rms

@Composable
fun DownMenu(
    label: String, content: @Composable MenuScope.() -> Unit
) {
    var showMenu by rms(false)
    val thisScope: MenuScope = remember {
        object : MenuScope {
            override fun close() {
                showMenu = false
            }
        }
    }
    Column {
        BasicText(label, modifier = Modifier.clickable { showMenu = !showMenu })
        DropdownMenu(expanded = showMenu, onDismissRequest = {
            showMenu = false
        }) {
            content(thisScope)
        }
    }
}
