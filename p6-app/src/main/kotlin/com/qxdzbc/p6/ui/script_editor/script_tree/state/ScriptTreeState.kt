package com.qxdzbc.p6.ui.script_editor.script_tree.state

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.common.view.tree_view.state.TreeNodeState
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer

interface ScriptTreeState {
    val centralScriptContainer: CentralScriptContainer

    val nodeStateContainerMs:Ms<TreeNodeStateContainer>
    var treeNodeStateCont:TreeNodeStateContainer

    val currentNodePointerMs:Ms<CurrentNodePointer>
    var currentNodePointer:CurrentNodePointer
    val currentNodeStateMs:Ms<TreeNodeState>?
    val currentNodeState:TreeNodeState?
    fun removeWbNode(workbookKey:WorkbookKey):ScriptTreeState
}
