package com.emeraldblast.p6.ui.action_table

import com.emeraldblast.p6.ui.script_editor.action.CodeEditorAction
import com.emeraldblast.p6.ui.script_editor.script_tree.action.ScriptTreeAction
import javax.inject.Inject

class CodeEditorActionTableImp  @Inject constructor(
    override val codeEditorAction: CodeEditorAction,
    override val scriptTreeAction: ScriptTreeAction
) : CodeEditorActionTable {

}
