package com.qxdzbc.p6.app.action.app.set_wbkey

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.ui.window.state.WindowState

data class SetWbKeyResponse(
    val oldWbKey: WorkbookKey,
    val newWb: Workbook? = null,
    val newWbState: WorkbookState? = null,
    val newWbStateCont:WorkbookStateContainer?=null,
    val newWindowState: WindowState? = null,
    val windowStateMs: Ms<WindowState>? = null,
    val newCentralScriptCont:CentralScriptContainer?=null,
)
