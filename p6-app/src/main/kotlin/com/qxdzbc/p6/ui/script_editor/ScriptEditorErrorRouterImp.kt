package com.qxdzbc.p6.ui.script_editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.message.api.message.sender.exception.SenderErrors
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
import javax.inject.Inject

class ScriptEditorErrorRouterImp @Inject constructor(
    val codeEditorStateMs: Ms<CodeEditorState>,
    val errorRouter: ErrorRouter,
) : ScriptEditorErrorRouter {
    private var codeEditorState by codeEditorStateMs
    private var oddityContInCodeEditor by codeEditorState.errorContainerMs
    override fun toOutputPanel(errorReport: ErrorReport) {
        val err = errorReport
        if (err.isType(SenderErrors.IOPubExecuteError.header)) {
            try{
                val data = err.data as SenderErrors.IOPubExecuteError.Data
                val content = data.messageContent
                codeEditorState = codeEditorState.addOutput(
                    output = content.traceBackAsStr()
                )
            }catch(e:Exception){
                codeEditorState = codeEditorState.addOutput(
                    output = err.data.toString()
                )
            }
        }
        else if (err.isType(SenderErrors.CodeError.header)) {
            try{
                val data = err.data as SenderErrors.CodeError.Data
                val content = data.messageContent
                println("err1")
                codeEditorState = codeEditorState.addOutput(
                    output = content.traceBackAsStr()
                )
            }catch (e:Exception){
                codeEditorState = codeEditorState.addOutput(
                    output = err.data.toString()
                )
            }
        } else {
            // route this error to code window by default
            this.toCodeEditorWindow(err)
        }
    }

    override fun toCodeEditorWindow(errorReport: ErrorReport) {
        oddityContInCodeEditor = oddityContInCodeEditor.addErrorReport(errorReport)
    }

    override fun toApp(errorReport: ErrorReport) {
        errorRouter.publishToApp(errorReport)
    }
}
