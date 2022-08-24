package com.qxdzbc.p6.di.state.app_state

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.CapHashMap
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.script.ScriptContainer
import com.qxdzbc.p6.app.document.script.ScriptContainerImp
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.OddityContainer
import com.qxdzbc.p6.app.oddity.OddityContainerImp
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
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.state.CellEditorStateImp
import com.qxdzbc.p6.ui.kernel.MsKernelContext
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorAction
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorActionImp
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainerImp3
import com.qxdzbc.p6.ui.script_editor.script_tree.action.ScriptTreeAction
import com.qxdzbc.p6.ui.script_editor.script_tree.action.ScriptTreeActionImp
import com.qxdzbc.p6.ui.script_editor.script_tree.state.ScriptTreeState
import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
import com.qxdzbc.p6.ui.script_editor.state.SwingCodeEditorStateImp
import com.qxdzbc.p6.ui.window.state.WindowState
import dagger.Binds
import dagger.Provides

/**
 * provide objects related to the app state
 */
@dagger.Module
interface AppStateModule {

    @Binds
    fun CellEditorState(i: CellEditorStateImp): CellEditorState

    @Binds
    @P6Singleton
    @WbStateContSt
    fun WorkbookStateContSt(@WbStateContMs i: Ms<WorkbookStateContainer>): St<WorkbookStateContainer>

    @Binds
    @P6Singleton
    @DocumentContainerSt
    fun DocumentContainerSt(@DocumentContainerMs i:Ms<DocumentContainer>):St<DocumentContainer>

    @Binds
    @P6Singleton
    @SubAppStateContainerSt
    fun StateContainerSt(@SubAppStateContainerMs i:Ms<SubAppStateContainer>):St<SubAppStateContainer>

    @Binds
    fun TranslatorContainer(i:TranslatorContainerImp):TranslatorContainer

    @Binds
    fun DocumentContainer(i:DocumentContainerImp):DocumentContainer

    @Binds
    fun StateContainer(i: SubAppStateContainerImp):SubAppStateContainer

    @Binds
    @P6Singleton
    @MsKernelContextQualifier
    fun ReactiveKernel(i:MsKernelContext):KernelContext

    @Binds
    @P6Singleton
    fun CodeEditorAction(i: CodeEditorActionImp): CodeEditorAction

    @Binds
    @P6Singleton
    fun ScriptTreeAction(i: ScriptTreeActionImp): ScriptTreeAction

    companion object {
        @Provides
        @P6Singleton
        @StateContainerMs
        fun StateContainerMs(i:StateContainerImp):Ms<StateContainer>{
            return ms(i)
        }

        @Provides
        @P6Singleton
        @StateContainerSt
        fun StateContainerSt(@StateContainerMs i:Ms<StateContainer>):St<StateContainer>{
            return i
        }

        @Provides
        @P6Singleton
        @TranslatorContainerMs
        fun TranslatorContainerMs(i:TranslatorContainer):Ms<TranslatorContainer>{
            return ms(i)
        }

        @Provides
        @P6Singleton
        @DocumentContainerMs
        fun DocumentContainerMs(i:DocumentContainer):Ms<DocumentContainer>{
            return ms(i)
        }

        @Provides
        @P6Singleton
        @SubAppStateContainerMs
        fun SubAppStateContainerMs(i:SubAppStateContainer):Ms<SubAppStateContainer>{
            return ms(i)
        }

        @Provides
        @P6Singleton
        @WbStateContMs
        fun WorkbookStateContMs(wbStateFactory: WorkbookStateFactory): Ms<WorkbookStateContainer> {
            return ms(WorkbookStateContainerImp(wbStateFactory=wbStateFactory))
        }


        @Provides
        @P6Singleton
        @KernelStatusQualifier
        fun KernelStatusMs(kernel: KernelContext): Ms<KernelStatus> {
            return ms(kernel.kernelStatus)
        }

        @Provides
        @P6Singleton
        @AppWindowStateListMs
        fun WindowStateList(): Ms<List<Ms<WindowState>>> {
            return ms(listOf())
        }

        @Provides
        @P6Singleton
        @CodeEditorStateMs
        fun CodeEditorState(
            @WbContainerMs
            wbContMs: Ms<WorkbookContainer>,
            @CentralScriptContMs
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
        @ScriptTreeStateMs
        fun ScriptTreeState(@CodeEditorStateMs codeEditorStateMs: Ms<CodeEditorState>): Ms<ScriptTreeState> {
            return codeEditorStateMs.value.scriptTreeStateMs
        }

        @Provides
        @P6Singleton
        @AppOddityContMs
        fun AppOddityContainerMs(): Ms<OddityContainer> {
            return ms(OddityContainerImp())
        }

        @Provides
        @P6Singleton
        @WbContainerMs
        fun WbContainer(wb:WorkbookContainerImp): Ms<WorkbookContainer> {
            return ms(wb)
        }

        @Provides
        @P6Singleton
        @WindowActivePointerMs
        fun appActiveWindowPointer(): Ms<ActiveWindowPointer> {
            return ms(ActiveWindowPointerImp(null))
        }

        @Provides
        @P6Singleton
        @AppScriptContMs
        fun AppScriptCont(): Ms<ScriptContainer> {
            return ms(ScriptContainerImp())
        }

        @Provides
        @P6Singleton
        @CentralScriptContMs
        fun CentralScriptContainer(
            @AppScriptContMs s: Ms<ScriptContainer>,
            @WbStateContMs wc:Ms<WorkbookStateContainer>
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
        @AppStateMs
        fun appStateMs(i: AppStateImp): Ms<AppState> {
            return ms(i)
        }

        @Provides
        @P6Singleton
        @TranslatorMapMs
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
        @CellEditorStateMs
        fun CellEditorStateMs(i: CellEditorState):Ms<CellEditorState>{
            return ms(i)
        }
        @Provides
        @CellEditorInitCursorIdSt
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

