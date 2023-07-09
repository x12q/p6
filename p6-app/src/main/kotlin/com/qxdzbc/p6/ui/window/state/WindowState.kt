package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.common.Rse
import com.qxdzbc.common.WithSize
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.err.ErrorContainer
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorGenerator
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogState
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.qxdzbc.p6.ui.window.formula_bar.FormulaBarState
import com.qxdzbc.p6.ui.window.kernel_dialog.ShowDialogState
import com.qxdzbc.p6.ui.window.status_bar.StatusBarState
import com.qxdzbc.p6.ui.window.tool_bar.state.ToolBarState
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarState
import kotlinx.coroutines.CompletableDeferred
import java.nio.file.Path

interface WindowState : WithSize {

    val id:String

    val toolBarStateMs:Ms<ToolBarState>
    val toolBarState:ToolBarState

    /**
     * This is a reference to the common color generator shared by all windows.
     */
    val formulaColorGenerator: FormulaColorGenerator

    val focusStateMs:Ms<WindowFocusState>
    var focusState:WindowFocusState

    val statusBarStateMs:Ms<StatusBarState>
    var statusBarState:StatusBarState

    val openCommonFileDialog:Boolean
    val commonFileDialogJob: CompletableDeferred<Path?>?
    fun setCommonFileDialogJob(job:CompletableDeferred<Path?>?)
    fun removeCommonFileDialogJob()

    val showConnectToKernelDialogStateMs:Ms<ShowDialogState>
    var showConnectToKernelDialogState:ShowDialogState

    val showStartKernelDialogStateMs:Ms<ShowDialogState>
    var showStartKernelDialogState: ShowDialogState

    val wbStateContMs: Ms<WorkbookStateContainer>

    val formulaBarState:FormulaBarState



    /**
     * Check if this window contain workbook having [wbKey]
     */
    fun containWbKey(wbKey: WorkbookKey):Boolean

    /**
     * This is the globally shared workbook container in the app, not the sole container of this window state
     */
    val wbStateMsList: List<WorkbookState>
    val wbStateList: List<WorkbookState>
    val wbList: List<Workbook>


    fun removeWbState(wbKeyMs: St<WorkbookKey>)

    /**
     * A set of all workbook key in this window
     */
    val wbKeyMsSet:Set<Ms<WorkbookKey>>
    val wbKeySet:Set<WorkbookKey>

    fun addWbKey(wbKey: Ms<WorkbookKey>)
    fun addWbKeyRs(wbKey: Ms<WorkbookKey>): Rse<Unit>

    fun setWbKeySet(wbKeySet: Set<Ms<WorkbookKey>>)

    val activeWbPointerMs: Ms<ActiveWorkbookPointer>
    var activeWbPointer: ActiveWorkbookPointer
    val activeWbState: WorkbookState?
    val activeWbStateMs: WorkbookState?
    val activeWbKey:WorkbookKey? get() = activeWbPointer.wbKey

    val errorContainerMs: Ms<ErrorContainer>
    var errorContainer: ErrorContainer

    /**
     * TODO reconsider this
     */
    val wbTabBarState: WorkbookTabBarState

    val saveDialogStateMs: Ms<FileDialogState>
    val saveDialogState:FileDialogState

    val loadDialogStateMs: Ms<FileDialogState>
    var loadDialogState:FileDialogState

    /**
     * Window title is derived from the current active workbook name.
     */
    val windowTitle:String
}
