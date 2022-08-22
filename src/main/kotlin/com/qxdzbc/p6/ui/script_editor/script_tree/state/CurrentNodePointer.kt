package com.qxdzbc.p6.ui.script_editor.script_tree.state

import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

data class CurrentNodePointer(
    val isPointingToApp: Boolean = true,
    val wbKey: WorkbookKey? = null,
    val scriptKey: ScriptEntryKey? = null,
) {
    fun pointToApp(): CurrentNodePointer {
        return this.copy(isPointingToApp = true, wbKey = null, scriptKey = null)
    }

    fun pointToWb(wbKey: WorkbookKey): CurrentNodePointer {
        return this.copy(wbKey = wbKey, scriptKey = null, isPointingToApp = false)
    }

    fun pointToScript(scriptKey: ScriptEntryKey): CurrentNodePointer {
        return this.copy(scriptKey = scriptKey, wbKey = null, isPointingToApp = false)
    }
}
