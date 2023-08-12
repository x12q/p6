package com.qxdzbc.p6.ui.workbook.di.comp

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.workbook.active_sheet_pointer.ActiveWorksheetPointerImp
import com.qxdzbc.p6.ui.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.workbook.state.WorkbookStateImp
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultActiveWorksheetPointer
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface WbModule{

    @Binds
    @WbScope
    fun wbState(i:WorkbookStateImp):WorkbookState

    companion object{

        @Provides
        @WbScope
        @DefaultActiveWorksheetPointer
        fun DefaultActiveWorksheetPointer(): Ms<ActiveWorksheetPointer> {
            return ms(ActiveWorksheetPointerImp())
        }

        @Provides
        @WbScope
        @WindowIdMsInWbState
        fun windowIdMs(): Ms<String?> = ms(null)

        @Provides
        @WbScope
        @WsStateMapMs
        fun wsStateMapMs():Ms<Map<St<String>, WorksheetState>> = ms(emptyMap())

        @Provides
        @WbScope
        @NeedSave
        fun needSaveMs():Ms<Boolean>{
            return ms(false)
        }

    }
}