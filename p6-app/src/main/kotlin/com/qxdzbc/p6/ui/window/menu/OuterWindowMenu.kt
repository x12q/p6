package com.qxdzbc.p6.ui.window.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import com.qxdzbc.p6.ui.window.menu.action.CodeMenuAction
import com.qxdzbc.p6.ui.window.menu.action.FileMenuAction
import com.qxdzbc.p6.ui.window.state.OuterWindowState


@Composable
fun FrameWindowScope.OuterWindowMenu(
    fileMenuAction: FileMenuAction?,
    codeMenuAction: CodeMenuAction?,
    outerWindowState: OuterWindowState
) {
    WindowMenu(fileMenuAction,codeMenuAction,outerWindowState.innerWindowState)
}
