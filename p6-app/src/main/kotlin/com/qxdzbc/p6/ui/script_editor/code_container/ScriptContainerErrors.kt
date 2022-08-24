package com.qxdzbc.p6.ui.script_editor.code_container

import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport

object ScriptContainerErrors {
    val prefix = "UI_CodeContainerErrors_"
    fun CantReplaceExistingKey(oldKey: ScriptEntryKey, newKey: ScriptEntryKey):ErrorReport{
        return ErrorHeader(
            errorCode = "${prefix}0",
            errorDescription = "Can't replace ${oldKey} with ${newKey} because the new key is already pointing to an existing script"
        ).toErrorReport()
    }
    fun ScriptKeyNotExist(oldKey: ScriptEntryKey):ErrorReport{
        return ErrorHeader(
            errorCode = "${prefix}1",
            errorDescription = "${oldKey} does not exist"
        ).toErrorReport()
    }
    object CantAddScriptBecauseItAlreadyExist {
        val header = ErrorHeader(
            errorCode = "${prefix}2",
            errorDescription = "script already exist"
        )
        fun report(scriptName:String):ErrorReport{
            return header.setDescription("Can't addd script \"${scriptName}\" because it already exists").toErrorReport()
        }
    }
    object ScriptNotExist {
        val header = ErrorHeader(
            errorCode = "${prefix}3",
            errorDescription = "script does not exist"
        )
        fun report(detail:String?):ErrorReport{
            return detail?.let { header.setDescription(it).toErrorReport() } ?: header.toErrorReport()
        }
    }
}
