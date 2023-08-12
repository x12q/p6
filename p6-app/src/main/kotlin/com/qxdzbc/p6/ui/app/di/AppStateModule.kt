package com.qxdzbc.p6.ui.app.di

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.CapHashMap
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.err.ErrorContainer
import com.qxdzbc.p6.app.err.ErrorContainerImp
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.TranslatorMap
import com.qxdzbc.p6.translator.TranslatorMapImp
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
//import com.qxdzbc.p6.ui.app.state.AppStateFactory
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.app.di.qualifiers.*
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import dagger.Binds
import dagger.Provides
import dagger.Module
import javax.inject.Singleton

/**
 * provide objects related to the app state
 */
@Module
interface AppStateModule {

    @Binds
    @Singleton
    fun WbContainer(wb:WorkbookContainerImp): WorkbookContainer

    companion object {

        @Provides
        @Singleton
        fun WindowStateMap(): Ms<Map<String, Ms<OuterWindowState>>> {
            return ms(emptyMap())
        }

        @Provides
        @Singleton
        @AppErrorContMs
        fun AppOddityContainerMs(): Ms<ErrorContainer> {
            return ms(ErrorContainerImp())
        }

        @Provides
        @Singleton
        fun TranslatorMapMs(): Ms<TranslatorMap> {
            return ms(TranslatorMapImp())
        }

        @Provides
        @InitSingleTranslatorMap
        fun InitSingleTranslatorMap(): Map<Pair<WorkbookKey,String>,P6Translator<ExUnit>> {
            return CapHashMap(30)
        }

        @Provides
        @Singleton
        fun CellEditorStateMs(i: CellEditorState):Ms<CellEditorState>{
            return ms(i)
        }
        @Provides
        @InitCellEditorInitCursorIdSt
        fun CellEditorInitCursorIdSt():St<CursorId>?{
            return null
        }

        @Provides
        @DefaultNullCellAddress
        fun DefaultNullCellAddress():CellAddress?{
            return null
        }

        @Provides
        @NullTextFieldValue
        fun NullTextFieldValue():TextFieldValue?{
            return null
        }
    }
}

