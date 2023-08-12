package com.qxdzbc.p6.composite_actions.app.close_wb

import com.qxdzbc.p6.document_data_layer.wb_container.WorkbookContainer
import com.qxdzbc.p6.ui.window.state.WindowState

data class CloseWbState(
    val wbCont: WorkbookContainer,
    val respectiveWindowState: WindowState?,
)
