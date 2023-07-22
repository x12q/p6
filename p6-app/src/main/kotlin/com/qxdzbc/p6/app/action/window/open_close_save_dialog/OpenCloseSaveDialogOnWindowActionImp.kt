package com.qxdzbc.p6.app.action.window.open_close_save_dialog

import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class OpenCloseSaveDialogOnWindowActionImp @Inject constructor(
    val stateContainerSt:StateContainer,
) : OpenCloseSaveDialogOnWindowAction {

    val sc  = stateContainerSt

    override fun openSaveFileDialog(windowId: String) {
        sc.getWindowStateMsById(windowId)?.also {
            val windowState = it
            windowState.saveDialogStateMs.value = windowState.saveDialogState.setOpen(true)
        }
    }

    override fun closeSaveFileDialog(windowId: String) {
        sc.getWindowStateMsById(windowId)?.also {
            val windowState = it
            windowState.saveDialogStateMs.value = windowState.saveDialogState.setOpen(false)
        }
    }
}
