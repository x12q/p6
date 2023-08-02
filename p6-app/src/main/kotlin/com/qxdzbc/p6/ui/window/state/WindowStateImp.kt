package com.qxdzbc.p6.ui.window.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rse
import com.qxdzbc.common.ErrorUtils.getOrThrow

import com.qxdzbc.p6.di.status_bar.StatusBarStateQualifier
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.err.ErrorContainer
import com.qxdzbc.p6.app.err.ErrorContainerImp
import com.qxdzbc.p6.ui.window.di.DefaultFocusStateMs
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
import com.qxdzbc.p6.ui.window.status_bar.StatusBarState
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarState
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarStateImp
import com.github.michaelbull.result.*
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorGenerator
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
    @Assisted val wbKeyMsSetMs: Ms<Set<Ms<WorkbookKey>>>,
    @Assisted val commonFileDialogJobMs: Ms<CompletableDeferred<Path?>?> = ms(null),
    // ===========================================================================

    private val wbCont: WorkbookContainer,
    override val wbStateCont: WorkbookStateContainer,
    @StatusBarStateQualifier
    override val statusBarStateMs: Ms<StatusBarState>,
    private val wbStateFactory: WorkbookStateFactory,
    @DefaultFocusStateMs
    override val focusStateMs: Ms<WindowFocusState>,
    override val formulaColorGenerator: FormulaColorGenerator,

    ) : BaseWindowState() {

    override val commonFileDialogJob: CompletableDeferred<Path?>? by commonFileDialogJobMs

    override val wbKeyMsSet: Set<Ms<WorkbookKey>> by wbKeyMsSetMs

    override val wbKeySet: Set<WorkbookKey> get()= wbKeyMsSet.map{it.value}.toSet()

    override val wbStateMsList: List<WorkbookState>
        get() = wbKeySet.mapNotNull {
            wbStateCont.getWbState(
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

    override var activeWbPointer: ActiveWorkbookPointer by activeWbPointerMs

    override val saveDialogState: FileDialogState by saveDialogStateMs

    override val wbList: List<Workbook>
        get() = wbKeySet.mapNotNull {
            this.wbCont.getWb(it)
        }

    override val activeWbState: WorkbookState?
        get() {
            val activeWbKey:WorkbookKey? = this.activeWbPointer.wbKey
            if (activeWbKey!=null) {
                return this.wbStateCont.getWbState(activeWbKey)
            }
            return null
        }
    override val activeWbStateMs: WorkbookState?
        get() {
            return this.activeWbPointer.wbKey?.let{this.getWorkbookStateMs(it)}
        }

    private fun getWorkbookStateMs(workbookKey: WorkbookKey): WorkbookState? {
        return wbStateCont.getWbState(workbookKey)
    }

    private fun getWorkbookStateMsRs(workbookKey: WorkbookKey): Rse<WorkbookState> {
        val wbsMs = wbStateCont.getWbStateRs(workbookKey)
        return wbsMs
    }

    override var focusState: WindowFocusState by focusStateMs

    override var statusBarState: StatusBarState by statusBarStateMs

    override fun setCommonFileDialogJob(job: CompletableDeferred<Path?>?) {
        commonFileDialogJobMs.value = job
    }

    override fun removeCommonFileDialogJob() {
        commonFileDialogJobMs.value = null
    }

    override fun removeWbState(wbKeyMs: St<WorkbookKey>) {
        if (wbKeyMs in this.wbKeyMsSet) {
            if (this.activeWbPointer.isPointingTo(wbKeyMs)) {
                this.activeWbPointerMs.value = this.activeWbPointer.nullify()
            }
            wbKeyMsSetMs.value = wbKeyMsSet.filter{it!=wbKeyMs}.toSet()
        }
    }
    @Throws(Exception::class)
    override fun addWbKey(wbKey: Ms<WorkbookKey>) {
        addWbKeyRs(wbKey).getOrThrow()
    }

    override fun addWbKeyRs(wbKey: Ms<WorkbookKey>): Rse<Unit> {
        if (wbKey in this.wbKeyMsSet) {
            return Ok(Unit)
        } else {
            val wbStateMsRs = this.wbStateCont.getWbStateRs(wbKey)
            val rt = wbStateMsRs.map {wbStateMs->
                val wbk = wbStateMs.wbKeyMs
                val newWbKeySet = this.wbKeyMsSet + wbk
                wbKeyMsSetMs.value = newWbKeySet
            }
            return rt
        }
    }

    override fun setWbKeySet(wbKeySet: Set<Ms<WorkbookKey>>) {
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
        wbKeyMsSetMs.value = newWbKeySet
    }

    override var errorContainer: ErrorContainer by errorContainerMs

    override val wbTabBarState: WorkbookTabBarState
        get() = WorkbookTabBarStateImp(this)
}
