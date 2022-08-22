package com.qxdzbc.p6.ui.script_editor

import com.qxdzbc.p6.common.exception.error.ErrorHeader
import com.qxdzbc.p6.common.exception.error.ErrorReport

object ScriptEditorErrors {
    val prefix = "UI_ScriptEditorErrors_"
    fun EmptyScriptName(): ErrorReport {
        return ErrorHeader(
            errorCode = "${prefix}_0",
            errorDescription = "Script name can't be empty"
        ).toErrorReport()
    }
}
