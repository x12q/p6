package com.emeraldblast.p6.di.state.app_state

import androidx.compose.ui.text.input.TextFieldValue
import com.emeraldblast.p6.app.common.utils.CapHashMap
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.script.ScriptContainer
import com.emeraldblast.p6.app.document.script.ScriptContainerImp
import com.emeraldblast.p6.app.document.wb_container.WorkbookContainer
import com.emeraldblast.p6.app.document.wb_container.WorkbookContainerImp2
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.oddity.OddityContainer
import com.emeraldblast.p6.app.oddity.OddityContainerImp
import com.emeraldblast.p6.di.P6Singleton
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelContext
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelStatus
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.TranslatorMap
import com.emeraldblast.p6.translator.TranslatorMapImp
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.app.ActiveWindowPointer
import com.emeraldblast.p6.ui.app.ActiveWindowPointerImp
import com.emeraldblast.p6.ui.app.state.*
//import com.emeraldblast.p6.ui.app.state.AppStateFactory
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookStateFactory
import com.emeraldblast.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.emeraldblast.p6.ui.document.workbook.state.cont.WorkbookStateContainerImp
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorStateImp
import com.emeraldblast.p6.ui.kernel.MsKernelContext
import com.emeraldblast.p6.ui.script_editor.action.CodeEditorAction
import com.emeraldblast.p6.ui.script_editor.action.CodeEditorActionImp
import com.emeraldblast.p6.ui.script_editor.code_container.CentralScriptContainer
import com.emeraldblast.p6.ui.script_editor.code_container.CentralScriptContainerImp3
import com.emeraldblast.p6.ui.script_editor.script_tree.action.ScriptTreeAction
import com.emeraldblast.p6.ui.script_editor.script_tree.action.ScriptTreeActionImp
import com.emeraldblast.p6.ui.script_editor.script_tree.state.ScriptTreeState
import com.emeraldblast.p6.ui.script_editor.state.CodeEditorState
import com.emeraldblast.p6.ui.script_editor.state.SwingCodeEditorStateImp
import com.emeraldblast.p6.ui.window.state.WindowState
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
    @StateContainerSt
    fun StateContainerSt(@StateContainerMs i:Ms<SubAppStateContainer>):St<SubAppStateContainer>

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
        @StateContainerMs
        fun StateContainerMs(i:SubAppStateContainer):Ms<SubAppStateContainer>{
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
        fun WbContainer(wb:WorkbookContainerImp2): Ms<WorkbookContainer> {
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

