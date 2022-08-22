package com.qxdzbc.p6.ui.script_editor.action

import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

class CodeEditorActionDoNothing : CodeEditorAction {
    override fun runCode(code: String) {
        println("run code: $code")
    }

    override fun updateCurrentCode(code: String) {
        println("updateCode: $code")
    }

    override fun openCode(key: ScriptEntryKey) {
        println("openScript")
    }

    override fun closeCode(key: ScriptEntryKey) {
        println("closeScript")
    }

    override fun showCode(key: ScriptEntryKey) {
        println("do nothing")
    }

    override fun setCurrentCodeKey(key: ScriptEntryKey) {
        println("do nothing")
    }

    override fun addNewScript(workbookKey: WorkbookKey?) {
        println("do nothing")
    }

    override fun selectTheClosestKey(currentKey: ScriptEntryKey) {
        println("do nothing")
    }

    override fun closeRenameScriptDialog() {
        println("do nothing")
    }

    override fun renameScript(key: ScriptEntryKey, newName: String) {
        println("do nothing")
    }
}
