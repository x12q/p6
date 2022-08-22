package com.qxdzbc.p6.ui.script_editor.action

import com.qxdzbc.p6.ui.script_editor.script_tree.action.ScriptTreeAction
import javax.inject.Inject

class CodeEditorActionTableImp  @Inject constructor(
    override val codeEditorAction: CodeEditorAction,
    override val scriptTreeAction: ScriptTreeAction
) : CodeEditorActionTable {

}