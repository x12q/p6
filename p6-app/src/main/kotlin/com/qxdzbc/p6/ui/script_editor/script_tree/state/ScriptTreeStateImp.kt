package com.qxdzbc.p6.ui.script_editor.script_tree.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.common.view.tree_view.state.TreeNodeState
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer

data class ScriptTreeStateImp constructor(
    private val centralScriptContainerMs: Ms<CentralScriptContainer>,
    override val nodeStateContainerMs: Ms<TreeNodeStateContainer>,
    override val currentNodePointerMs: Ms<CurrentNodePointer> = ms(CurrentNodePointer()),
) : ScriptTreeState {

    companion object {
        fun fromCodeContainer(
            centralScriptContainerMs: Ms<CentralScriptContainer>,
            checkWbExist:(wbKey:WorkbookKey)-> Boolean,
        ): ScriptTreeStateImp {
            val tc = TreeNodeStateContainerImp(
                centralScriptContainerMs = centralScriptContainerMs,
                checkWbExist = checkWbExist,
            )
            return ScriptTreeStateImp(
                centralScriptContainerMs = centralScriptContainerMs,
                nodeStateContainerMs = ms(tc),
            )
        }
    }

    override val centralScriptContainer: CentralScriptContainer by centralScriptContainerMs
    override var treeNodeStateCont: TreeNodeStateContainer by nodeStateContainerMs
    override var currentNodePointer: CurrentNodePointer by currentNodePointerMs
    override val currentNodeStateMs: Ms<TreeNodeState>?
        get() {
            val pointer = this.currentNodePointer
            if (pointer.isPointingToApp) {
                return this.treeNodeStateCont.getAppNodeStateMs()
            }
            if (pointer.wbKey != null) {
                return this.treeNodeStateCont.getWbNodeStateMs(pointer.wbKey)
            }
            if (pointer.scriptKey != null) {
                return this.treeNodeStateCont.getNodeStateMs(pointer.scriptKey)
            }
            return null
        }
    override val currentNodeState: TreeNodeState? get() = currentNodeStateMs?.value

    override fun removeWbNode(workbookKey: WorkbookKey): ScriptTreeState {
        this.treeNodeStateCont = this.treeNodeStateCont
            .removeWbNodeState(workbookKey)
            .removeScriptState(workbookKey)
        return this
    }

}
