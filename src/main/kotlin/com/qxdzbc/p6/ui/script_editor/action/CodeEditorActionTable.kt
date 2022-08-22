package com.qxdzbc.p6.ui.script_editor.action

import com.qxdzbc.p6.ui.script_editor.action.CodeEditorAction
import com.qxdzbc.p6.ui.script_editor.script_tree.action.ScriptTreeAction

interface CodeEditorActionTable {
    val codeEditorAction: CodeEditorAction
    val scriptTreeAction:ScriptTreeAction
}
