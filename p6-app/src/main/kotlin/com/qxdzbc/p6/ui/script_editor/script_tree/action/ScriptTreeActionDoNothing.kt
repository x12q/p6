package com.qxdzbc.p6.ui.script_editor.script_tree.action

import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

class ScriptTreeActionDoNothing : ScriptTreeAction {
    override fun clickOnScriptItem(key: ScriptEntryKey?) {
        println("do nothing")
    }

    override fun clickOnAppNode() {
        println("do nothing")
    }

    override fun clickOnWbNode(wbKey: WorkbookKey) {
        println("do nothing")
    }

    override fun doubleClickOnItem(key: ScriptEntryKey?) {
        println("do nothing")
    }

    override fun enterKey(key: ScriptEntryKey?) {
        println("do nothing")
    }

    override fun addNewScript(workbookKey: WorkbookKey?) {
        println("do nothing")
    }

    override fun removeScript(scriptEntryKey: ScriptEntryKey) {
        println("do nothing")
    }

    override fun openRenameScriptDialog(scriptEntryKey: ScriptEntryKey) {
        println("do nothing")
    }
}
