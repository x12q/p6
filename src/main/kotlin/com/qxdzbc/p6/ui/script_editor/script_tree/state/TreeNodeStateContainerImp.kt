package com.qxdzbc.p6.ui.script_editor.script_tree.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.common.utils.Utils.replaceKey
import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.common.view.tree_view.state.TreeNodeState
import com.qxdzbc.p6.ui.common.view.tree_view.state.TreeNodeStateImp
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

data class TreeNodeStateContainerImp(
    private val centralScriptContainerMs: Ms<CentralScriptContainer>,
    private val checkWbExist:(wbKey:WorkbookKey)-> Boolean,
    private val appNodeStateMs: Ms<TreeNodeState> = ms(TreeNodeStateImp(isExpandable = true)),
    var wbNodeStateMsMap: Map<WorkbookKey, Ms<TreeNodeState>> = emptyMap(),
    var scriptNodeStateMsMap: Map<ScriptEntryKey, Ms<TreeNodeState>> = emptyMap(),
) : TreeNodeStateContainer {

    private var centralScriptContainer: CentralScriptContainer by centralScriptContainerMs

    override fun replaceWorkbookKey(oldWbKey: WorkbookKey, newWbKey: WorkbookKey): TreeNodeStateContainerImp {

        val wbNodesMap: Map<WorkbookKey, Ms<TreeNodeState>> = this.wbNodeStateMsMap.replaceKey(oldWbKey, newWbKey)
        val newScriptNodeMap = scriptNodeStateMsMap.mapKeys {
            if (it.key.wbKey == oldWbKey) {
                it.key.setWbKey(newWbKey)
            } else {
                it.key
            }
        }
        return this.copy(
            wbNodeStateMsMap = wbNodesMap,
            scriptNodeStateMsMap = newScriptNodeMap
        )
    }

    override fun getNodeStateMs(scriptKey: ScriptEntryKey): Ms<TreeNodeState>? {
        val script = centralScriptContainer.getScript(scriptKey)
        if (script != null) {
            val state = scriptNodeStateMsMap[scriptKey] ?: run {
                val s: Ms<TreeNodeState> = ms(TreeNodeStateImp(isExpandable = false))
                scriptNodeStateMsMap = scriptNodeStateMsMap + (scriptKey to s)
                s
            }
            return state
        }
        return null
    }

    override fun getWbNodeStateMs(wbKey: WorkbookKey): Ms<TreeNodeState>? {
        val wbExist = checkWbExist(wbKey)
        if (wbExist) {
            val wbNodeState = this.wbNodeStateMsMap[wbKey] ?: run {
                val s: Ms<TreeNodeState> = ms(TreeNodeStateImp(isExpandable = true))
                this.wbNodeStateMsMap = this.wbNodeStateMsMap + (wbKey to s)
                s
            }
            return wbNodeState
        } else {
            return null
        }
    }

    override fun getAppNodeStateMs(): Ms<TreeNodeState> {
        return appNodeStateMs
    }

    override fun removeWbNodeState(wbKey: WorkbookKey): TreeNodeStateContainer {
        return this.copy(
            wbNodeStateMsMap = this.wbNodeStateMsMap - wbKey
        )
    }

    override fun removeScriptState(scriptKey: ScriptEntryKey): TreeNodeStateContainer {
        return this.copy(
            scriptNodeStateMsMap = this.scriptNodeStateMsMap - scriptKey
        )
    }

    override fun removeScriptState(wbKey: WorkbookKey): TreeNodeStateContainer {
        return this.copy(
            scriptNodeStateMsMap = scriptNodeStateMsMap.filter { it.key.wbKey != wbKey }
        )
    }

    override fun replaceScriptKey(
        oldKey: ScriptEntryKey,
        newKey: ScriptEntryKey
    ): Result<TreeNodeStateContainer, ErrorReport> {
        val oldState = this.scriptNodeStateMsMap[oldKey]
        if (oldState != null) {
            return Ok(
                this.copy(
                    scriptNodeStateMsMap = this.scriptNodeStateMsMap - oldKey + (newKey to oldState)
                )
            )
        } else {
            return Ok(this)
        }
    }
}
