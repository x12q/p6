package com.qxdzbc.p6.ui.document.workbook.state

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface WorkbookStateFactory {
    fun create(
        @Assisted("1") wbMs: Ms<Workbook>,
        @Assisted("2") windowIdMs: Ms<String?> = ms(null),
    ): WorkbookStateImp

    companion object {
        /**
         * Create a new workbook state using [WorkbookStateFactory], and refresh it immediately.
         * Refreshing will create state object for worksheets and cells that does not have a state.
         */
        fun WorkbookStateFactory.createAndRefresh(
            wbMs: Ms<Workbook>,
            windowId:String?=null,
        ): WorkbookStateImp {
            return this.create(
                wbMs,
                ms(windowId),
            ).apply {
                refresh()
            }
        }
    }
}
