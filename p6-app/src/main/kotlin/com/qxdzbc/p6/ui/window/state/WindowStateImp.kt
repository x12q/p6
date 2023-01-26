package com.qxdzbc.p6.ui.window.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rse
import com.qxdzbc.common.ErrorUtils.getOrThrow

import com.qxdzbc.p6.di.status_bar.StatusBarStateQualifier
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.ErrorContainer
import com.qxdzbc.p6.app.oddity.ErrorContainerImp
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.di.state.window.DefaultFocusStateMs
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogState
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogStateImp
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.qxdzbc.p6.ui.window.formula_bar.FormulaBarState
import com.qxdzbc.p6.ui.window.formula_bar.FormulaBarStateImp
import com.qxdzbc.p6.ui.window.kernel_dialog.ShowDialogState
import com.qxdzbc.p6.ui.window.kernel_dialog.ShowDialogStateImp
import com.qxdzbc.p6.ui.window.status_bar.StatusBarState
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarState
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarStateImp
import com.github.michaelbull.result.*
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorGenerator
import com.qxdzbc.p6.ui.window.dialog.WindowDialogGroupState
import com.qxdzbc.p6.ui.window.tool_bar.state.ToolBarState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CompletableDeferred
import java.nio.file.Path
import java.util.*

data class WindowStateImp @AssistedInject constructor(
    @Assisted override val toolBarStateMs: Ms<ToolBarState>,
    @Assisted override val activeWbPointerMs: Ms<ActiveWorkbookPointer>,
    @Assisted override val errorContainerMs: Ms<ErrorContainer> = ms(ErrorContainerImp()),
    @Assisted("saveDialogStateMs")
    override val saveDialogStateMs: Ms<FileDialogState> = ms(FileDialogStateImp()),
    @Assisted("loadDialogStateMs")
    override val loadDialogStateMs: Ms<FileDialogState> = ms(FileDialogStateImp()),
    @Assisted override val id: String = UUID.randomUUID().toString(),
    @Assisted override val wbKeyMsSet: Set<Ms<WorkbookKey>>,
    @Assisted("showStartKernelDialogStateMs")
    override val showStartKernelDialogStateMs: Ms<ShowDialogState> = ms(
        ShowDialogStateImp()
    ),
    @Assisted("showConnectToKernelDialogStateMs")
    override val showConnectToKernelDialogStateMs: Ms<ShowDialogState> = ms(
        ShowDialogStateImp()
    ),
    @Assisted override val commonFileDialogJob: CompletableDeferred<Path?>? = null,
//    @Assisted override val dialogHostStateMs: Ms<WindowDialogGroupState>,
    // ===========================================================================

    override val wbContMs: Ms<WorkbookContainer>,
    override val wbStateContMs: Ms<WorkbookStateContainer>,
    @StatusBarStateQualifier
    override val statusBarStateMs: Ms<StatusBarState>,
    private val wbStateFactory: WorkbookStateFactory,
    @DefaultFocusStateMs
    override val focusStateMs: Ms<WindowFocusState>,
    override val formulaColorGenerator: FormulaColorGenerator,

    ) : BaseWindowState() {
    override val wbKeySet: Set<WorkbookKey> get()= wbKeyMsSet.map{it.value}.toSet()
    override var showStartKernelDialogState: ShowDialogState by showStartKernelDialogStateMs

    override val wbStateMsList: List<Ms<WorkbookState>>
        get() = wbKeySet.mapNotNull {
            wbStateContMs.value.getWbStateMs(
                it
            )
        }

    /**
     * special note: FormulaBarState is a fully derivative state, therefore it does not have a Ms<>
     */
    override val formulaBarState: FormulaBarState get()= FormulaBarStateImp(this)
    override fun containWbKey(wbKey: WorkbookKey): Boolean {
        return this.wbKeySet.contains(wbKey)
    }

    override var loadDialogState: FileDialogState by loadDialogStateMs
    override val activeWbPointer: ActiveWorkbookPointer by activeWbPointerMs
    override val saveDialogState: FileDialogState by saveDialogStateMs

    private var wbStateCont by wbStateContMs

    override val activeWbState: WorkbookState?
        get() {
            val activeWbKey:WorkbookKey? = this.activeWbPointer.wbKey
            if (activeWbKey!=null) {
                return this.wbStateCont.getWbState(activeWbKey)
            }
            return null
        }
    override val activeWbStateMs: Ms<WorkbookState>?
        get() {
            return this.activeWbPointer.wbKey?.let{this.getWorkbookStateMs(it)}
        }

    private fun getWorkbookStateMs(workbookKey: WorkbookKey): Ms<WorkbookState>? {
        return wbStateCont.getWbStateMs(workbookKey)
    }

    private fun getWorkbookStateMsRs(workbookKey: WorkbookKey): Result<Ms<WorkbookState>, ErrorReport> {
        val wbsMs = wbStateCont.getWbStateMsRs(workbookKey)
        return wbsMs
    }

    override var focusState: WindowFocusState by focusStateMs

    override var statusBarState: StatusBarState by statusBarStateMs

    override fun setCommonFileDialogJob(job: CompletableDeferred<Path?>?): WindowState {
        return this.copy(commonFileDialogJob = job)
    }

    override fun removeCommonFileDialogJob(): WindowState {
        return this.copy(commonFileDialogJob = null)
    }

    override var showConnectToKernelDialogState: ShowDialogState by showConnectToKernelDialogStateMs

    override fun removeWbState(wbKeyMs: St<WorkbookKey>): WindowState {
        if (wbKeyMs in this.wbKeyMsSet) {
            if (this.activeWbPointer.isPointingTo(wbKeyMs)) {
                this.activeWbPointerMs.value = this.activeWbPointer.nullify()
            }
            val wbStateMs = this.wbStateCont.getWbStateMs(wbKeyMs)
            if (wbStateMs != null) {
                wbStateMs.value = wbStateMs.value.setWindowId(null)
            }
            return this.copy(wbKeyMsSet = wbKeyMsSet.filter{it!=wbKeyMs}.toSet())
        } else {
            return this
        }
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun addWbKey(wbKey: Ms<WorkbookKey>): WindowState {
        return addWbKeyRs(wbKey).getOrThrow()
    }

    override fun addWbKeyRs(wbKey: Ms<WorkbookKey>): Rse<WindowState> {
        if (wbKey in this.wbKeyMsSet) {
            return Ok(this)
        } else {
            val wbStateMsRs = this.wbStateCont.getWbStateMsRs(wbKey)
            val rt = wbStateMsRs.map {wbStateMs->
                val wbk = wbStateMs.value.wbKeyMs
                val newWbKeySet = this.wbKeyMsSet + wbk
                wbStateMs.value =wbStateMs.value.setWindowId(this.id)
                this.copy(wbKeyMsSet = newWbKeySet)
            }
            return rt
        }
    }

    override fun setWbKeySet(wbKeySet: Set<Ms<WorkbookKey>>): WindowState {
        val newWbKeySet = wbKeySet
        // x: update active workbook pointer
        if (newWbKeySet.isEmpty()) {
            this.activeWbPointerMs.value = this.activeWbPointer.nullify()
        } else {
            val k = this.activeWbState?.wb?.keyMs
            if (k == null || k !in newWbKeySet) {
                this.activeWbPointerMs.value = this.activeWbPointer.pointTo(newWbKeySet.first())
            }
        }
        // x: update window id of each wb state
        for (wbk in newWbKeySet) {
            wbStateCont.getWbStateMs(wbk)?.also {
                it.value = it.value.setWindowId(this.id)
            }
        }
        return this.copy(wbKeyMsSet = newWbKeySet)
    }

    override var errorContainer: ErrorContainer by errorContainerMs

    override val wbTabBarState: WorkbookTabBarState
        get() = WorkbookTabBarStateImp(this)
}
