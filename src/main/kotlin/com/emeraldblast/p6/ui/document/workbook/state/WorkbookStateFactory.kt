package com.emeraldblast.p6.ui.document.workbook.state

import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.ui.common.compose.Ms
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface WorkbookStateFactory {
    fun create(
        @Assisted("1") wbMs: Ms<Workbook>,
        @Assisted("2") windowId: String? = null,
    ): WorkbookStateImp

    companion object {
        fun WorkbookStateFactory.createRefresh(
            wbMs: Ms<Workbook>,
            windowId: String? = null
        ): WorkbookState {
            return this.create(
                wbMs,
                windowId
            )
                .refresh()
        }
    }
}
