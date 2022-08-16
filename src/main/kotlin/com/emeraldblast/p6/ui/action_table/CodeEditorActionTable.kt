package com.emeraldblast.p6.ui.action_table

import com.emeraldblast.p6.ui.script_editor.action.CodeEditorAction
import com.emeraldblast.p6.ui.script_editor.script_tree.action.ScriptTreeAction

interface CodeEditorActionTable {
    val codeEditorAction: CodeEditorAction
    val scriptTreeAction:ScriptTreeAction
}
