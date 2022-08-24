package com.qxdzbc.p6.ui.script_editor

import com.qxdzbc.common.error.ErrorReport

class ScriptEditorErrorRouterDoNothing : ScriptEditorErrorRouter {
    override fun toOutputPanel(errorReport: ErrorReport) {
        println("do nothing")
    }

    override fun toCodeEditorWindow(errorReport: ErrorReport) {
        println("do nothing")
    }

    override fun toApp(errorReport: ErrorReport) {
        println("do nothing")
    }
}
