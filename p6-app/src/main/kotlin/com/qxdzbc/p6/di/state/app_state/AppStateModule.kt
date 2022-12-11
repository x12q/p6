package com.qxdzbc.p6.di.state.app_state

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.CapHashMap
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.script.ScriptContainer
import com.qxdzbc.p6.app.document.script.ScriptContainerImp
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.ErrorContainer
import com.qxdzbc.p6.app.oddity.ErrorContainerImp
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContext
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelStatus
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
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.format.*
import com.qxdzbc.p6.ui.format.attr.BoolAttr
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainerImp3
import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
import com.qxdzbc.p6.ui.script_editor.state.SwingCodeEditorStateImp
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

    @Binds
    @P6Singleton
    fun StateContainerSt(i:Ms<SubAppStateContainer>):St<SubAppStateContainer>

    @Binds
    @P6Singleton
    fun CellFormatTableSt(i:Ms<CellFormatTable>):St<CellFormatTable>


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
        fun SubAppStateContainerMs(i:SubAppStateContainer):Ms<SubAppStateContainer>{
            return ms(i)
        }

        @Provides
        @P6Singleton
        fun WorkbookStateContMs(wbStateFactory: WorkbookStateFactory): Ms<WorkbookStateContainer> {
            return ms(WorkbookStateContainerImp(wbStateFactory=wbStateFactory))
        }


        @Provides
        @P6Singleton
        fun KernelStatusMs(kernel: KernelContext): Ms<KernelStatus> {
            return ms(kernel.kernelStatus)
        }

        @Provides
        @P6Singleton
        fun WindowStateMap(): Ms<Map<String, Ms<OuterWindowState>>> {
            return ms(emptyMap())
        }

        @Provides
        @P6Singleton
        fun CodeEditorState(
            wbContMs: Ms<WorkbookContainer>,
            centralScriptContainerMs: Ms<CentralScriptContainer>
        ): Ms<CodeEditorState> {
            return ms(
                SwingCodeEditorStateImp(
                    wbContMs = wbContMs,
                    centralScriptContainerMs = centralScriptContainerMs
                )
            )
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
        fun InitActiveWindowPointer(): Ms<ActiveWindowPointer> {
            return ms(ActiveWindowPointerImp(null))
        }

        @Provides
        @P6Singleton
        fun AppScriptCont(): Ms<ScriptContainer> {
            return ms(ScriptContainerImp())
        }

        @Provides
        @P6Singleton
        fun CentralScriptContainer(
            s: Ms<ScriptContainer>,
            wc:Ms<WorkbookStateContainer>
        ): Ms<CentralScriptContainer> {
            return ms(
                CentralScriptContainerImp3(
                    appScriptContainerMs = s,
                    wbStateContMs = wc
                )
            )
        }

        @Provides
        @P6Singleton
        fun appStateMs(i: AppStateImp): Ms<AppState> {
            return ms(i)
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
        fun CellEditorInitCursorIdSt():St<CursorStateId>?{
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

