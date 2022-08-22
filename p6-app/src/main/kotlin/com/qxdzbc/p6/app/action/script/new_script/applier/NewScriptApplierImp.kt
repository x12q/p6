package com.qxdzbc.p6.app.action.script.new_script.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.common_data_structure.ErrorIndicator
import com.qxdzbc.p6.app.action.script.new_script.NewScriptNotification
import com.qxdzbc.p6.app.action.script.new_script.NewScriptResponse
import com.qxdzbc.p6.di.state.app_state.CodeEditorStateMs
import com.qxdzbc.p6.app.document.script.ScriptEntry
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.script_editor.ScriptEditorErrorRouter
import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
import javax.inject.Inject

class NewScriptApplierImp @Inject constructor(
    @CodeEditorStateMs private val codeEditorStateMs: Ms<CodeEditorState>,
    private val ceErrRouter: ScriptEditorErrorRouter,
) : NewScriptApplier {

    private var codeEditorState by codeEditorStateMs

    override fun applyNewScript(res: NewScriptResponse?) {
        if (res != null) {
            res.errorReport?.also {
                ceErrRouter.toCodeEditorWindow(it)
            }
        }
    }

    internal fun applyNewScript(newScriptEntry: ScriptEntry, errorIndicator: ErrorIndicator) {
        if (errorIndicator.isOk) {
            codeEditorState.centralScriptContainer = codeEditorState.centralScriptContainer.addOrOverwriteScript(newScriptEntry)
            val wbKey = newScriptEntry.key.wbKey
            // x: expand the container node
            if (wbKey == null) {
                val appNodeStateMs = codeEditorState.treeNodeStateContainer.getAppNodeStateMs()
                appNodeStateMs.value = appNodeStateMs.value.expand()
            } else {
                val wbNodeStateMs = codeEditorState.treeNodeStateContainer.getWbNodeStateMs(wbKey)
                if (wbNodeStateMs != null) {
                    wbNodeStateMs.value = wbNodeStateMs.value.expand()
                }
            }

            // x: set the current script to the new script
            codeEditorState = codeEditorState
                .openScript(newScriptEntry.key)
                .setCurrentScriptKey(newScriptEntry.key)

        } else {
            errorIndicator.errorReport?.also {
                ceErrRouter.toCodeEditorWindow(it)
            }
        }
    }

    override fun applyNewScriptNotif(notif: NewScriptNotification) {
        val errorIndicator = notif.errorIndicator
        if (errorIndicator.isOk) {
            codeEditorState.centralScriptContainer = codeEditorState.centralScriptContainer.addOrOverwriteMultiScripts(notif.scriptEntries)
            val wbKeyList = notif.scriptEntries.map{it.key.wbKey}
            for(workbookKey in wbKeyList){
                // x: expand the container node
                if (workbookKey == null) {
                    val appNodeStateMs = codeEditorState.treeNodeStateContainer.getAppNodeStateMs()
                    appNodeStateMs.value = appNodeStateMs.value.expand()
                } else {
                    val wbNodeStateMs = codeEditorState.treeNodeStateContainer.getWbNodeStateMs(workbookKey)
                    if (wbNodeStateMs != null) {
                        wbNodeStateMs.value = wbNodeStateMs.value.expand()
                    }
                }
            }
        } else {
            errorIndicator.errorReport?.also {
                ceErrRouter.toCodeEditorWindow(it)
            }
        }
    }
}
