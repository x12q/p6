package com.qxdzbc.p6.di.state.app_state

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.CapHashMap
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.err.ErrorContainer
import com.qxdzbc.p6.app.err.ErrorContainerImp
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.TranslatorMap
import com.qxdzbc.p6.translator.TranslatorMapImp
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.ActiveWindowPointerImp
import com.qxdzbc.p6.ui.app.state.*
//import com.qxdzbc.p6.ui.app.state.AppStateFactory
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainerImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import dagger.Binds
import dagger.Provides

/**
 * provide objects related to the app state
 */
@dagger.Module
interface AppStateModule {

    @Binds
    @P6Singleton
    fun WorkbookStateContSt(
        i: Ms<WorkbookStateContainer>
    ): St<WorkbookStateContainer>

    @Binds
    @P6Singleton
    fun DocumentContainerSt(i:Ms<DocumentContainer>):St<DocumentContainer>

    companion object {

        @Provides
        @P6Singleton
        fun StateContainerMs(i:StateContainerImp):Ms<StateContainer>{
            return ms(i)
        }

        @Provides
        @P6Singleton
        fun StateContainerSt(i:Ms<StateContainer>):St<StateContainer>{
            return i
        }

        @Provides
        @P6Singleton
        fun TranslatorContainerMs(i:TranslatorContainer):Ms<TranslatorContainer>{
            return ms(i)
        }

        @Provides
        @P6Singleton
        fun TranslatorContainerSt(i:Ms<TranslatorContainer>):St<TranslatorContainer>{
            return i
        }

        @Provides
        @P6Singleton
        fun DocumentContainerMs(i:DocumentContainer):Ms<DocumentContainer>{
            return ms(i)
        }

        @Provides
        @P6Singleton
        fun WorkbookStateContMs(wbStateFactory: WorkbookStateFactory): Ms<WorkbookStateContainer> {
            return ms(WorkbookStateContainerImp(wbStateFactory=wbStateFactory))
        }

        @Provides
        @P6Singleton
        fun WindowStateMap(): Ms<Map<String, Ms<OuterWindowState>>> {
            return ms(emptyMap())
        }

        @Provides
        @P6Singleton
        @AppErrorContMs
        fun AppOddityContainerMs(): Ms<ErrorContainer> {
            return ms(ErrorContainerImp())
        }

        @Provides
        @P6Singleton
        fun WbContainer(wb:WorkbookContainerImp): Ms<WorkbookContainer> {
            return ms(wb)
        }

        @Provides
        @P6Singleton
        fun TranslatorMapMs(): Ms<TranslatorMap> {
            return ms(TranslatorMapImp())
        }

        @Provides
        @InitSingleTranslatorMap
        fun InitSingleTranslatorMap(): Map<Pair<WorkbookKey,String>,P6Translator<ExUnit>> {
            return CapHashMap(30)
        }

        @Provides
        @P6Singleton
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

