package com.qxdzbc.p6.rpc.document.workbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import com.google.protobuf.Int64Value
import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.app.set_wbkey.SetWbKeyRequest
import com.qxdzbc.p6.app.action.app.set_wbkey.SetWbKeyResponse
import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.action.global.GlobalAction
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetRequest.Companion.toModel
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse2
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetWithIndexRequest
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetRequest.Companion.toModel
import com.qxdzbc.p6.app.common.utils.Utils.onNextAndComplete
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.state.app_state.DocumentContainerMs
import com.qxdzbc.p6.di.state.app_state.SubAppStateContainerMs
import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.WorkbookProtos
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.proto.rpc.WorkbookServiceGrpc
import com.qxdzbc.p6.rpc.document.workbook.msg.*
import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetRequest.Companion.toModel
import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetResponse
import com.qxdzbc.p6.rpc.document.worksheet.msg.WorksheetIdWithIndexPrt.Companion.toModel
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import io.grpc.stub.StreamObserver
import javax.inject.Inject

class WorkbookRpcService @Inject constructor(
//    @AppStateMs
//    private val appStateMs: Ms<AppState>,
    private val translatorContainer: TranslatorContainer,
    private val globalAction: GlobalAction,
    @DocumentContainerMs
    private val documentContMs: Ms<DocumentContainer>,
    @SubAppStateContainerMs
    private val stateContMs: Ms<SubAppStateContainer>
) : WorkbookServiceGrpc.WorkbookServiceImplBase() {

    //    private var appState by appStateMs
    private var documentCont by documentContMs
    private var stateCont by stateContMs

    override fun getWorksheet(
        request: WorksheetProtos.WorksheetIdWithIndexProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetWorksheetResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val req = request.toModel()
            val wsIndex = req.wsIndex
            val wbk = req.wbKey
            val wsName = req.wsName
            val wb = documentCont.getWb(wbk)
            val ws = if (wsName != null) {
                wb?.getWs(wsName)
            } else if (wsIndex != null) {
                wb?.getWs(wsIndex)
            } else {
                null
            }
            val rt = GetWorksheetResponse(ws?.id)
            responseObserver.onNextAndComplete(rt.toProto())
        } else {
            super.getWorksheet(request, responseObserver)
        }
    }

    override fun getActiveWorksheet(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetWorksheetResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wbk = request.toModel()
            val ws = stateCont.getWbState(wbk)?.activeSheetState?.worksheet
            val rt = GetWorksheetResponse(ws?.id)
            responseObserver.onNextAndComplete(rt.toProto())
        } else {
            super.getActiveWorksheet(request, responseObserver)
        }
    }

    override fun setActiveWorksheet(
        request: WorksheetProtos.WorksheetIdWithIndexProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val req = request.toModel()
            val wbk = req.wbKey
            val wsName = req.wsName
            val wsIndex = req.wsIndex
            val rs: Rse<SetActiveWorksheetResponse2> = if (wsName != null) {
                val setActiveWsRs = globalAction.setActiveWs(
                    request = SetActiveWorksheetRequest(wbKey = wbk, wsName = wsName)
                ).mapError { it.errorReport }
                setActiveWsRs
            } else if (wsIndex != null) {
                // find the name
                val setActiveWsRs = globalAction.setActiveWsUsingIndex(
                    request = SetActiveWorksheetWithIndexRequest(wbKey = wbk, wsIndex = wsIndex)
                ).mapError { it.errorReport }
                setActiveWsRs
            } else {
                val err =
                    WorkbookRpcMsgErrors.IllegalMsg.report("Set active worksheet request must provide a valid worksheet name or a valid worksheet index")
                Err(err)
            }
            val rt = SingleSignalResponse.fromRs(rs)
            responseObserver.onNextAndComplete(rt.toProto())
        } else {
            super.setActiveWorksheet(request, responseObserver)
        }
    }

    override fun getAllWorksheets(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<WorkbookProtos.GetAllWorksheetsResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wbk = request.toModel()
            val wsRs: Rse<List<Worksheet>> = documentCont
                .getWbRs(wbk).map { it.worksheets }
            val rt = GetAllWorksheetsResponse.fromRs(wsRs)
            responseObserver.onNextAndComplete(rt.toProto())
        } else {
            super.getAllWorksheets(request, responseObserver)
        }
    }

    override fun setWbKey(
        request: WorkbookProtos.SetWbKeyRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val req = SetWbKeyRequest.fromProto(request)
            val rs: Rs<SetWbKeyResponse, ErrorReport> = globalAction.setWbKeyRs(req)
            val rt = SingleSignalResponse.fromRs(rs).toProto()
            responseObserver.onNextAndComplete(rt)
        } else {
            super.setWbKey(request, responseObserver)
        }
    }

    override fun addWorksheet(
        request: WorkbookProtos.AddWorksheetRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wbk = request.wbKey.toModel()
            val wsn = request.worksheet.name ?: ""
            val translator = translatorContainer.getTranslatorOrCreate(wbk, wsn)
            val wbkMsRs = documentCont.getWbRs(wbKey = wbk)
            val rs = wbkMsRs.flatMap { wb ->
                val req = request.toModel(wb.keyMs, translator)
                val rs: Rs<AddWorksheetResponse, ErrorReport> = globalAction.addWorksheetRs(req)
                rs
            }
            val rt = SingleSignalResponse.fromRs(rs).toProto()
            responseObserver.onNextAndComplete(rt)
        } else {
            super.addWorksheet(request, responseObserver)
        }
    }

    override fun createNewWorksheet(
        request: WorkbookProtos.CreateNewWorksheetRequestProto?,
        responseObserver: StreamObserver<WorkbookProtos.WorksheetWithErrorReportMsgProto>?
    ) {
        if (request != null && responseObserver != null) {
            val req: CreateNewWorksheetRequest = request.toModel()
            val rs = globalAction.createNewWorksheetRs(req)
            val rt = WorksheetWithErrorReportMsg.fromRs(rs)
            responseObserver.onNextAndComplete(rt.toProto())
        } else {
            super.createNewWorksheet(request, responseObserver)
        }
    }

    override fun deleteWorksheet(
        request: WorksheetProtos.WorksheetIdWithIndexProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val req = request.toModel()
            val rs: Rse<Unit> = globalAction.deleteWorksheetRs(req)
            val rt = SingleSignalResponse.fromRs(rs)
            responseObserver.onNextAndComplete(rt.toProto())
        } else {
            super.deleteWorksheet(request, responseObserver)
        }
    }

    override fun renameWorksheet(
        request: WorkbookProtos.RenameWorksheetRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rs = globalAction.renameWorksheetRs(request.toModel())
            val rt = SingleSignalResponse.fromRs(rs)
            responseObserver.onNextAndComplete(rt.toProto())
        } else {
            super.renameWorksheet(request, responseObserver)
        }
    }

    override fun sheetCount(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<Int64Value>?
    ) {
        if (request != null && responseObserver != null) {
            val wbKey = request.toModel()
            val wb = documentCont.getWb(wbKey)
            responseObserver.onNext(Int64Value.of((wb?.size ?: 0).toLong()))
            responseObserver.onCompleted()
        } else {
            super.sheetCount(request, responseObserver)
        }
    }
}
