package com.qxdzbc.p6.ui.document.workbook.di.comp

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.FalseMs
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateImp
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import javax.inject.Qualifier

@Module
interface WbModule{

    @Binds
    @WbScope
    fun wbState(i:WorkbookStateImp):WorkbookState

    companion object{

        @Provides
        @WbScope
        @WindowIdMsInWb
        fun windowIdMs(): Ms<String?> = ms(null)

        @Provides
        @WbScope
        @WsStateMapMs
        fun wsStateMapMs():Ms<Map<St<String>, WorksheetState>> = ms(emptyMap())

    }
}