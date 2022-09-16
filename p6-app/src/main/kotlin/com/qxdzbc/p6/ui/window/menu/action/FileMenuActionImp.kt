package com.qxdzbc.p6.ui.window.menu.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.window.WindowAction
import javax.inject.Inject

class FileMenuActionImp @Inject constructor(
    private val windowAction: WindowAction,
    @AppStateMs private val appStateMs:Ms<AppState>
) : FileMenuAction {

    private var appState by appStateMs


    override fun save(windowId:String) {
        appState.getWindowStateMsById(windowId)?.value?.also {windowState->
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
        appState.getWindowStateMsById(windowId)?.value?.also {windowState->
            val activeWBState = windowState.activeWbState
            if(activeWBState!=null){
                windowAction.openSaveFileDialog(windowState.id)
            }
        }
    }

    override fun open(windowId:String) {
        windowAction.openLoadFileDialog(windowId)
    }

    override fun newWorkbook(windowId:String) {
        windowAction.createNewWorkbook(windowId)
    }
}
