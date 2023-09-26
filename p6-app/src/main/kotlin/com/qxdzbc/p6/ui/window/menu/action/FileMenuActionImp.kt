package com.qxdzbc.p6.ui.window.menu.action

import com.qxdzbc.p6.composite_actions.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.composite_actions.window.WindowAction
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class FileMenuActionImp @Inject constructor(
    private val windowAction: WindowAction,
    private val stateCont:StateContainer,
    private val closeWbAct:CloseWorkbookAction,
) : FileMenuAction {

    private val sc = stateCont

    override fun save(windowId:String) {
        sc.getWindowStateMsById(windowId)?.also { windowState->
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
        sc.getWindowStateMsById(windowId)?.also { windowState->
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
