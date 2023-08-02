package com.qxdzbc.p6.ui.document.workbook.di.comp

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory.Companion.createAndRefresh
import dagger.Module
import dagger.Provides

@Module
interface WbModule{
    companion object{
        @Provides
        @WbScope
        fun wbState(wbStateFactory: WorkbookStateFactory, wbMs: Ms<Workbook>): WorkbookState {
            return wbStateFactory.createAndRefresh(wbMs)
        }
    }
}