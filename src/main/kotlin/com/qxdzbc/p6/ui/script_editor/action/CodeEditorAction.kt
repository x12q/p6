package com.qxdzbc.p6.ui.script_editor.action

import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

interface CodeEditorAction {
    fun runCode(code: String)
    fun updateCurrentCode(code:String)
    fun openCode(key:ScriptEntryKey)
    fun closeCode(key: ScriptEntryKey)
    fun showCode(key:ScriptEntryKey)
    fun setCurrentCodeKey(key:ScriptEntryKey)
    fun addNewScript(workbookKey: WorkbookKey?)
    fun selectTheClosestKey(currentKey: ScriptEntryKey)
    fun closeRenameScriptDialog()
    fun renameScript(key:ScriptEntryKey, newName:String)
}
