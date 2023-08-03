package com.qxdzbc.p6.ui.document.workbook.state

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

interface WorkbookStateFactory {

    fun createAndRefresh(
        wbMs: Ms<Workbook>,
        windowId: String? = null,
    ): WorkbookState

    fun create(
        wbMs: Ms<Workbook>,
    ): WorkbookState

}
