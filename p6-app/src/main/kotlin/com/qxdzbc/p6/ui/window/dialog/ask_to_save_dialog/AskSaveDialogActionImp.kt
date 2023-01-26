package com.qxdzbc.p6.ui.window.dialog.ask_to_save_dialog

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.app.action.window.open_close_save_dialog.OpenCloseSaveDialogOnWindowAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.window.dialog.WindowDialogGroupState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class AskSaveDialogActionImp @Inject constructor(
    val stateContainerSt: St<@JvmSuppressWildcards StateContainer>,
    val openCloseSaveDialogAction: OpenCloseSaveDialogOnWindowAction,
    private val closeWbAction: CloseWorkbookAction,
) : AskSaveDialogAction {

    val sc by stateContainerSt

    override fun onCancel(windowId: String) {
        sc.getWindowStateById(windowId)?.also {
            close(windowId)
        }
    }

    override fun onSave(windowId: String, wbKeySt: St<WorkbookKey>) {
        sc.getWindowStateById(windowId)?.also {
//            it.dialogHostStateMs.value = it.dialogHostState.setIsAskSaveOpen(false)
            close(windowId)
            openCloseSaveDialogAction.openSaveFileDialog(windowId)
        }
    }

    override fun onDontSave(windowId: String) {
        sc.getWindowStateById(windowId)?.also {
            close(windowId)
            it.activeWbPointer.wbKeyMs?.also {wbKeyMs->
                closeWbAction.closeWb(wbKeyMs)
            }
        }
    }

    fun getDialogHostStateMs(windowId: String):Ms<WindowDialogGroupState>?{
//        return sc.getWindowStateById(windowId)?.dialogHostStateMs
        TODO()
    }

    override fun open(windowId: String) {
        getDialogHostStateMs(windowId)?.also {
            it.value = it.value.setIsAskSaveOpen(true)
        }
    }

    override fun close(windowId: String) {
        getDialogHostStateMs(windowId)?.also {
            it.value = it.value.setIsAskSaveOpen(false)
        }
    }
}
