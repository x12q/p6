package com.qxdzbc.p6.ui.script_editor.action

import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.script_editor.script_tree.action.ScriptTreeAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
class CodeEditorActionTableImp  @Inject constructor(
    override val codeEditorAction: CodeEditorAction,
    override val scriptTreeAction: ScriptTreeAction
) : CodeEditorActionTable {

}
