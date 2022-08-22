package com.qxdzbc.p6.app.action.request_maker

import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response
import com.qxdzbc.p6.proto.CommonProtos.ErrorReportProto
import com.qxdzbc.p6.ui.app.ErrorRouter
import com.qxdzbc.p6.ui.common.msg_api.isError
import com.qxdzbc.p6.ui.script_editor.ScriptEditorErrorRouter
import javax.inject.Inject

class P6ResponseErrorHandlerImp @Inject constructor(
    private val errorRouter: ErrorRouter,
    private val scriptErrorRouter: ScriptEditorErrorRouter,
) : P6ResponseErrorHandler {
    override fun publishErrResponseOnScriptWindow(p6Response: P6Response) {
        this.extractErrorReport(p6Response){
            scriptErrorRouter.toCodeEditorWindow(it)
        }
    }

    override fun publishErrResponseOnWindow(p6Res: P6Response, workbookKey: WorkbookKey?) {
        this.extractErrorReport(p6Res) {
            errorRouter.publishToWindow(it, workbookKey)
        }
    }

    override fun publishErrResponseOnWindow(p6Res: P6Response, windowId: String?, workbookKey: WorkbookKey?) {
        this.extractErrorReport(p6Res){
            errorRouter.publishToWindow(it,windowId, workbookKey)
        }
    }

    override fun publishErrResponseOnApp(p6Res: P6Response) {
        this.extractErrorReport(p6Res) {
            errorRouter.publishToApp(it)
        }
    }

    override fun publishErrResponseOnWindow(p6Res: P6Response, windowId: String?) {
        this.extractErrorReport(p6Res) {
            errorRouter.publishToWindow(it, windowId)
        }
    }

    private fun extractErrorReport(p6Res: P6Response, handleFunction: (ErrorReport) -> Unit) {
        if (p6Res.isError()) {
            val errorReport = ErrorReportProto
                .newBuilder()
                .mergeFrom(p6Res.data)
                .build().toModel()
            handleFunction(errorReport)
        }
    }
}
