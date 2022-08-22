package com.qxdzbc.p6.ui.script_editor

import com.qxdzbc.p6.common.exception.error.ErrorReport

interface ScriptEditorErrorRouter {
    /**
     * output the error to the output panel
     */
    fun toOutputPanel(errorReport: ErrorReport)

    /**
     * output the error to the code editor window
     */
    fun toCodeEditorWindow(errorReport: ErrorReport)

    /**
     * output the error to the app
     */
    fun toApp(errorReport: ErrorReport)
}
