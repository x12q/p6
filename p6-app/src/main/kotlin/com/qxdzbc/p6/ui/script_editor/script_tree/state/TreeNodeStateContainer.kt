package com.qxdzbc.p6.ui.script_editor.script_tree.state

import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.common.view.tree_view.state.TreeNodeState
import com.github.michaelbull.result.Result

interface TreeNodeStateContainer {
    /**
     * Replace [oldWbKey] with [newWbKey], everything else is kept as they were, including order of elements
     */
    fun replaceWorkbookKey(oldWbKey: WorkbookKey, newWbKey: WorkbookKey):TreeNodeStateContainer
    fun getNodeStateMs(scriptKey:ScriptEntryKey):Ms<TreeNodeState>?
    fun getWbNodeStateMs(wbKey:WorkbookKey):Ms<TreeNodeState>?
    fun getAppNodeStateMs():Ms<TreeNodeState>

    fun removeWbNodeState(wbKey: WorkbookKey):TreeNodeStateContainer
    fun removeScriptState(scriptKey: ScriptEntryKey):TreeNodeStateContainer
    fun removeScriptState(wbKey: WorkbookKey):TreeNodeStateContainer

    /**
     * Replace [oldKey] with [newKey], everything else is kept as they were, including order of elements
     */
    fun replaceScriptKey(oldKey: ScriptEntryKey, newKey: ScriptEntryKey): Result<TreeNodeStateContainer, ErrorReport>
}
