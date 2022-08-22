package com.qxdzbc.p6.ui.script_editor.script_tree.action

import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

interface ScriptTreeAction {
    fun clickOnScriptItem(key:ScriptEntryKey?)
    fun clickOnAppNode()
    fun clickOnWbNode(wbKey:WorkbookKey)
    fun doubleClickOnItem(key:ScriptEntryKey?)
    fun enterKey(key:ScriptEntryKey?)
    fun addNewScript(workbookKey: WorkbookKey?)
    fun removeScript(scriptEntryKey: ScriptEntryKey)
    fun openRenameScriptDialog(scriptEntryKey: ScriptEntryKey)
}
