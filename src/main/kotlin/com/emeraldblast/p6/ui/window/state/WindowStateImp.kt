package com.emeraldblast.p6.ui.window.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.common.utils.ErrorUtils.getOrThrow
import com.emeraldblast.p6.app.common.utils.Utils.findAndReplace
import com.emeraldblast.p6.di.state.app_state.MsKernelContextQualifier
import com.emeraldblast.p6.di.state.app_state.WbContainerMs
import com.emeraldblast.p6.di.state.app_state.WbStateContMs
import com.emeraldblast.p6.di.status_bar.StatusBarStateQualifier
import com.emeraldblast.p6.app.document.wb_container.WorkbookContainer
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.oddity.OddityContainer
import com.emeraldblast.p6.app.oddity.OddityContainerImp
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.di.state.window.FocusStateMs
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelContext
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookStateFactory
import com.emeraldblast.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.emeraldblast.p6.ui.window.file_dialog.state.FileDialogState
import com.emeraldblast.p6.ui.window.file_dialog.state.FileDialogStateImp
import com.emeraldblast.p6.ui.window.focus_state.WindowFocusState
import com.emeraldblast.p6.ui.window.formula_bar.FormulaBarState
import com.emeraldblast.p6.ui.window.formula_bar.FormulaBarStateImp
import com.emeraldblast.p6.ui.window.kernel_dialog.ShowDialogState
import com.emeraldblast.p6.ui.window.kernel_dialog.ShowDialogStateImp
import com.emeraldblast.p6.ui.window.status_bar.StatusBarState
import com.emeraldblast.p6.ui.window.workbook_tab.bar.WorkbookTabBarState
import com.emeraldblast.p6.ui.window.workbook_tab.bar.WorkbookTabBarStateImp
import com.github.michaelbull.result.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CompletableDeferred
import java.nio.file.Path
import java.util.*

data class WindowStateImp @AssistedInject constructor(
    @Assisted override val activeWorkbookPointerMs: Ms<ActiveWorkbookPointer>,
    @Assisted override val oddityContainerMs: Ms<OddityContainer> = ms(OddityContainerImp()),
    @Assisted("saveDialogStateMs")
    override val saveDialogStateMs: Ms<FileDialogState> = ms(FileDialogStateImp()),
    @Assisted("loadDialogStateMs")
    override val loadDialogStateMs: Ms<FileDialogState> = ms(FileDialogStateImp()),
    @Assisted override val id: String = UUID.randomUUID().toString(),
    @Assisted override val wbKeySet: Set<WorkbookKey> = emptySet(),
    @Assisted("showStartKernelDialogStateMs")
    override val showStartKernelDialogStateMs: Ms<ShowDialogState> = ms(
        ShowDialogStateImp()
    ),
    @Assisted("showConnectToKernelDialogStateMs")
    override val showConnectToKernelDialogStateMs: Ms<ShowDialogState> = ms(
        ShowDialogStateImp()
    ),
    @Assisted override val commonFileDialogJob: CompletableDeferred<Path?>? = null,

    @WbContainerMs
    override val globalWbContMs: Ms<WorkbookContainer>,
    @WbStateContMs
    override val globalWbStateContMs: Ms<WorkbookStateContainer>,
    @StatusBarStateQualifier
    override val statusBarStateMs: Ms<StatusBarState>,
    @MsKernelContextQualifier
    override val kernel: KernelContext,
    private val wbStateFactory: WorkbookStateFactory,
    @FocusStateMs
    override val focusStateMs: Ms<WindowFocusState>,
) : BaseWindowState() {
    companion object {
        /**
         * factory method with default behavior
         */
//        fun create(
//            wbContMs: Ms<WorkbookContainer>,
//            wbStateContMs: Ms<WorkbookStateContainer>,
//            wbKeys: Collection<WorkbookKey> = emptySet(),
//            id: String = UUID.randomUUID().toString(),
//            statusBarStateMs: Ms<StatusBarState>,
//            kernel: KernelContext,
//        ): WindowStateImp {
//            val activeWbPointerMs: Ms<ActiveWorkbookPointer> = ms(ActiveWorkbookPointerImp(wbKeys.firstOrNull()))
//            return WindowStateImp(
//                globalWbContMs = wbContMs,
//                globalWbStateContMs = wbStateContMs,
//                activeWorkbookPointerMs = activeWbPointerMs,
//                wbKeySet = wbKeys.toSet(),
//                id = id,
//                statusBarStateMs = statusBarStateMs,
//                kernel = kernel
//            )
//        }
    }

    override var showStartKernelDialogState: ShowDialogState by showStartKernelDialogStateMs

    override val workbookStateMsList: List<Ms<WorkbookState>>
        get() = wbKeySet.mapNotNull {
            globalWbStateContMs.value.getWbStateMs(
                it
            )
        }

    /**
     * special note: FormulaBarState is a fully derivative state, therefore it does not have a Ms<>
     */
    override val formulaBarState: FormulaBarState = FormulaBarStateImp(this)
    override fun containWbKey(wbKey: WorkbookKey): Boolean {
        return this.wbKeySet.contains(wbKey)
    }

    override var loadDialogState: FileDialogState by loadDialogStateMs
    override var activeWorkbookPointer: ActiveWorkbookPointer by activeWorkbookPointerMs
    override val saveDialogState: FileDialogState by saveDialogStateMs

    /**
     * Replace workbook key of an existing workbook with a new workbook key
     * - update active wb pointer if it was pointing to the old key
     */
    override fun replaceWorkbookKey(oldWbKey: WorkbookKey, newWbKey: WorkbookKey): WindowState {
        if (oldWbKey == newWbKey) {
            return this
        } else {
            if (this.activeWorkbookPointer.isPointingTo(oldWbKey)) {
                this.activeWorkbookPointer = this.activeWorkbookPointer.pointTo(newWbKey)
            }
            val newKeySet = this.wbKeySet.toList().findAndReplace(oldWbKey, newWbKey).toSet()
            return this.copy(wbKeySet = newKeySet)
        }
    }

    private var globalWbStateCont by globalWbStateContMs

    override val activeWorkbookState: WorkbookState?
        get() {
            val activeWbKey:WorkbookKey? = this.activeWorkbookPointer.wbKey
            if (activeWbKey!=null) {
                return this.globalWbStateCont.getWbState(activeWbKey)
            }
            return null
        }
    override val activeWorkbookStateMs: Ms<WorkbookState>?
        get() {
            val activeWbKey = this.activeWorkbookPointer.wbKey
            if (activeWbKey!=null) {
                return this.getWorkbookStateMs(activeWbKey)
            }
            return null
        }



    private fun getWorkbookStateMs(workbookKey: WorkbookKey): Ms<WorkbookState>? {
        return globalWbStateCont.getWbStateMs(workbookKey)
    }

    private fun getWorkbookStateMsRs(workbookKey: WorkbookKey): Result<Ms<WorkbookState>, ErrorReport> {
        val wbsMs = globalWbStateCont.getWbStateMsRs(workbookKey)
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


    override fun removeWorkbookState(wbKey: WorkbookKey): WindowState {
        if (wbKey in this.wbKeySet) {
            if (this.activeWorkbookPointer.isPointingTo(wbKey)) {
                this.activeWorkbookPointer = this.activeWorkbookPointer.nullify()
            }
            val wbStateMs = this.getWorkbookStateMs(wbKey)
            if (wbStateMs != null) {
                wbStateMs.value = wbStateMs.value.setWindowId(null)
            }
            return this.copy(wbKeySet = wbKeySet - wbKey)
        } else {
            return this
        }
    }

    override fun addWbKey(wbKey: WorkbookKey): WindowState {
        return addWbKeyRs(wbKey).getOrThrow()
    }

    override fun addWbKeyRs(wbKey: WorkbookKey): Rse<WindowState> {
        if (wbKey in this.wbKeySet) {
            return Ok(this)
        } else {
            val wbStateMsRs = this.globalWbStateCont.getWbStateMsRs(wbKey)
            val rt = wbStateMsRs.map {wbStateMs->
                val wbk = wbStateMs.value.wbKey
                val newWbKeySet = this.wbKeySet + wbk
                wbStateMs.value =wbStateMs.value.setWindowId(this.id)
                this.copy(wbKeySet = newWbKeySet)
            }
            return rt
        }
    }

    override fun setWorkbookList(workbookList: List<Workbook>): WindowState {
        val newWbKeySet: Set<WorkbookKey> = workbookList.map { it.key }.toSet()
        // x: select new active workbook if have to, otherwise keep the old pointer
        return this.setWbKeySet(newWbKeySet)
    }

    override fun setWbKeySet(wbKeySet: Set<WorkbookKey>): WindowState {
        val newWbKeySet = wbKeySet
        // x: update active workbook pointer
        if (newWbKeySet.isEmpty()) {
            this.activeWorkbookPointer = this.activeWorkbookPointer.nullify()
        } else {
            val k = this.activeWorkbookState?.wb?.key
            if (k == null || k !in newWbKeySet) {
                this.activeWorkbookPointer = this.activeWorkbookPointer.pointTo(newWbKeySet.first())
            }
        }
        // x: update window id of each wb state
        for (wbk in newWbKeySet) {
            globalWbStateCont.getWbStateMs(wbk)?.also {
                it.value = it.value.setWindowId(this.id)
            }
        }
        return this.copy(wbKeySet = newWbKeySet)
    }

    override var oddityContainer: OddityContainer by oddityContainerMs

    override val workbookTabBarState: WorkbookTabBarState
        get() = WorkbookTabBarStateImp(this)
}
