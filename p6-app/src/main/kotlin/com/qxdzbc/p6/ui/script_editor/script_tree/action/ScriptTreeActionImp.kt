package com.qxdzbc.p6.ui.script_editor.script_tree.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.di.state.app_state.CodeEditorStateMs
import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorAction
import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
import javax.inject.Inject

class ScriptTreeActionImp @Inject constructor(
    val codeEditorAction: CodeEditorAction,
    @CodeEditorStateMs val codeEditorStateMs: Ms<CodeEditorState>,
) : ScriptTreeAction {

    private var codeEditorState by codeEditorStateMs
    private var nodePointer by codeEditorState.scriptTreeState.currentNodePointerMs

    override fun clickOnScriptItem(key: ScriptEntryKey?) {
        if(key!=null){
            nodePointer = nodePointer.pointToScript(key)
        }
    }

    override fun clickOnAppNode() {
        nodePointer = nodePointer.pointToApp()
    }

    override fun clickOnWbNode(wbKey: WorkbookKey) {
        nodePointer = nodePointer.pointToWb(wbKey)
    }

    override fun doubleClickOnItem(key: ScriptEntryKey?) {
        if (key != null) {
            clickOnScriptItem(key)
            if (!codeEditorState.alreadyOpen(key)) {
                codeEditorAction.openCode(key)
            }
            codeEditorAction.showCode(key)
        }
    }

    override fun enterKey(key: ScriptEntryKey?) {
        this.doubleClickOnItem(key)
    }

    override fun addNewScript(workbookKey: WorkbookKey?) {
        codeEditorAction.addNewScript(workbookKey)
    }

    override fun removeScript(scriptEntryKey: ScriptEntryKey) {
        codeEditorAction.closeCode(scriptEntryKey)
        codeEditorState.centralScriptContainer = codeEditorState.centralScriptContainer.removeScript(scriptEntryKey)
        codeEditorState.treeNodeStateContainer = codeEditorState.treeNodeStateContainer.removeScriptState(scriptEntryKey)
    }

    override fun openRenameScriptDialog(scriptEntryKey: ScriptEntryKey) {
        codeEditorState = codeEditorState
            .setRenameTarget(scriptEntryKey)
            .openRenameScriptDialog()
    }

}
