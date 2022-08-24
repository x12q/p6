package com.qxdzbc.p6.ui.script_editor.state

import androidx.compose.runtime.MutableState
import com.qxdzbc.p6.app.document.script.ScriptEntry
import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.OddityContainer
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.ui.script_editor.script_tree.state.ScriptTreeState
import com.qxdzbc.p6.ui.script_editor.script_tree.state.TreeNodeStateContainer
import com.github.michaelbull.result.Result

interface CodeEditorState {
    fun removeWorkbook(workbookKey:WorkbookKey):CodeEditorState

    fun replaceWorkbookKey(oldWbKey:WorkbookKey, newWbKey:WorkbookKey):CodeEditorState

    fun replaceScriptKey(oldKey:ScriptEntryKey, newKey: ScriptEntryKey): Result<CodeEditorState, ErrorReport>

    val renameTarget:ScriptEntryKey?
    fun setRenameTarget(key:ScriptEntryKey):CodeEditorState

    val renameScriptDialogIsOpen:Boolean
    fun openRenameScriptDialog():CodeEditorState
    fun closeRenameScriptDialog():CodeEditorState

    val hasOpenedCodes:Boolean

    val currentCodeKey:ScriptEntryKey?
    fun setCurrentScriptKey(scriptKey:ScriptEntryKey?):CodeEditorState

    val currentScriptEntry:ScriptEntry?
    val currentScript:String?
    fun updateCurrentScript(newCode:String):CodeEditorState

    val scriptTreeStateMs:Ms<ScriptTreeState>
    var scriptTreeState: ScriptTreeState

    val centralScriptContainerMs:Ms<CentralScriptContainer>
    var centralScriptContainer: CentralScriptContainer

    val treeNodeStateContainerMs:Ms<TreeNodeStateContainer>
    var treeNodeStateContainer:TreeNodeStateContainer

    val openedScriptsMs:Ms<Set<ScriptEntryKey>>
    var openedScripts:Set<ScriptEntryKey>
    fun openScript(scriptKey: ScriptEntryKey):CodeEditorState
    fun alreadyOpen(key:ScriptEntryKey):Boolean
    fun removeOpenScript(key:ScriptEntryKey):CodeEditorState

    /**
     * oddity container
     */
    val oddityContainerMs: MutableState<OddityContainer>
    var oddityContainer:OddityContainer


    /**
     * output shown in the output panel
     */
    val outputList:List<String>
    val output:String
    fun addOutput(output:String):CodeEditorState
    fun clearOutput():CodeEditorState
}
