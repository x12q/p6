package com.qxdzbc.p6.ui.window.menu.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class FileMenuActionImp @Inject constructor(
    private val windowAction: WindowAction,
    private val stateContMs:Ms<StateContainer>,
    private val closeWbAct:CloseWorkbookAction,
) : FileMenuAction {

    private var sc by stateContMs

    override fun save(windowId:String) {
        sc.getWindowStateMsById(windowId)?.value?.also { windowState->
            val activeWBState = windowState.activeWbState
            if(activeWBState!=null){
                // if path is already present, sve it
                val wbPath = activeWBState.wbKey.path
                if(wbPath!=null){
                    windowAction.saveActiveWorkbook(wbPath, windowState.id)
                }else{
                    // if no path => open dialog, dialog will take care of saving
                    windowAction.openSaveFileDialog(windowState.id)
                }
            }
        }
    }

    override fun saveAs(windowId:String) {
        sc.getWindowStateMsById(windowId)?.value?.also { windowState->
            val activeWBState = windowState.activeWbState
            if(activeWBState!=null){
                windowAction.openSaveFileDialog(windowState.id)
            }
        }
    }

    override fun open(windowId:String) {
        windowAction.openLoadFileDialog(windowId)
    }

    override fun closeActiveWorkbook(windowId: String) {
        sc.getActiveWorkbook()?.also {
            closeWbAct.closeWb(it.keyMs)
        }
    }

    override fun newWorkbook(windowId:String) {
        windowAction.createNewWorkbook(windowId)
    }
}
