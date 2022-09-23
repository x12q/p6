package com.qxdzbc.p6.rpc.workbook

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
import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetRequest.Companion.toModel
import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetResponse
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetRequest.Companion.toModel
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse2
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetWithIndexRequest
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetRequest.Companion.toModel
import com.qxdzbc.p6.app.common.utils.CoroutineUtils
import com.qxdzbc.p6.app.common.utils.Utils.onNextAndComplete
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.ActionDispatcherMain
import com.qxdzbc.p6.di.AppCoroutineScope
import com.qxdzbc.p6.di.state.app_state.DocumentContainerMs
import com.qxdzbc.p6.di.state.app_state.SubAppStateContainerMs
import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.WorkbookProtos
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.proto.rpc.WorkbookServiceGrpc
import com.qxdzbc.p6.rpc.workbook.msg.GetAllWorksheetsResponse
import com.qxdzbc.p6.rpc.workbook.msg.GetWorksheetResponse
import com.qxdzbc.p6.rpc.workbook.msg.WorksheetWithErrorReportMsg
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt.Companion.toModel
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.*
import javax.inject.Inject

class WorkbookRpcService @Inject constructor(
//    @AppStateMs
//    private val appStateMs: Ms<AppState>,
    private val translatorContainer: TranslatorContainer,
    private val rpcActions: WorkbookRpcActions,
    @DocumentContainerMs
    private val documentContMs: Ms<DocumentContainer>,
    @SubAppStateContainerMs
    private val stateContMs: Ms<SubAppStateContainer>,
    @AppCoroutineScope
    val crtScope: CoroutineScope,
    @ActionDispatcherMain
    val actionDispatcherMain: CoroutineDispatcher
) : WorkbookServiceGrpc.WorkbookServiceImplBase() {

    //    private var appState by appStateMs
    private var dc by documentContMs
    private var sc by stateContMs

    override fun removeAllWorksheet(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                crtScope.async(actionDispatcherMain) {
                    val wbk = request.toModel()
                    val rs = rpcActions.removeAllWsRs(wbk)
                    val ssr = SingleSignalResponse.fromRs(rs)
                    ssr
                }.await()
            }
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getWorksheet(
        request: WorksheetProtos.WorksheetIdWithIndexProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetWorksheetResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val req = request.toModel()
            val wsIndex = req.wsIndex
            val wbk = req.wbKey
            val wsName = req.wsName
            val wb = dc.getWb(wbk)
            val ws = if (wsName != null) {
                wb?.getWs(wsName)
            } else if (wsIndex != null) {
                wb?.getWs(wsIndex)
            } else {
                null
            }
            val rt = GetWorksheetResponse(ws?.id)
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getActiveWorksheet(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetWorksheetResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wbk = request.toModel()
            val ws = sc.getWbState(wbk)?.activeSheetState?.worksheet
            val rt = GetWorksheetResponse(ws?.id)
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun setActiveWorksheet(
        request: WorksheetProtos.WorksheetIdWithIndexProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                crtScope.async(actionDispatcherMain) {
                    val req = request.toModel()
                    val wbk = req.wbKey
                    val wsName = req.wsName
                    val wsIndex = req.wsIndex
                    val rs: Rse<SetActiveWorksheetResponse2> = if (wsName != null) {
                        val setActiveWsRs = rpcActions.setActiveWs(
                            request = SetActiveWorksheetRequest(wbKey = wbk, wsName = wsName)
                        ).mapError { it.errorReport }
                        setActiveWsRs
                    } else if (wsIndex != null) {
                        // find the name
                        val setActiveWsRs = rpcActions.setActiveWsUsingIndex(
                            request = SetActiveWorksheetWithIndexRequest(wbKey = wbk, wsIndex = wsIndex)
                        ).mapError { it.errorReport }
                        setActiveWsRs
                    } else {
                        val err =
                            WorkbookRpcMsgErrors.IllegalMsg.report("Set active worksheet request must provide a valid worksheet name or a valid worksheet index")
                        Err(err)
                    }
                    val rt = SingleSignalResponse.fromRs(rs)
                    rt
                }.await()
            }
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getAllWorksheets(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<WorkbookProtos.GetAllWorksheetsResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wbk = request.toModel()
            val wsRs: Rse<List<Worksheet>> = dc
                .getWbRs(wbk).map { it.worksheets }
            val rt = GetAllWorksheetsResponse.fromRs(wsRs)
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun setWbKey(
        request: WorkbookProtos.SetWbKeyRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                crtScope.async(actionDispatcherMain) {
                    val req = SetWbKeyRequest.fromProto(request)
                    val rs: Rs<Unit, ErrorReport> = rpcActions.replaceWbKey(req)
                    val rt = SingleSignalResponse.fromRs(rs).toProto()
                    rt
                }.await()
            }
            responseObserver.onNextAndComplete(rt)
        }
    }

    override fun addWorksheet(
        request: WorkbookProtos.AddWorksheetRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                crtScope.async(actionDispatcherMain) {
                    val wbk = request.wbKey.toModel()
                    val wsn = request.worksheet.name ?: ""
                    val translator = translatorContainer.getTranslatorOrCreate(wbk, wsn)
                    val wbkMsRs = dc.getWbRs(wbKey = wbk)
                    val rs = wbkMsRs.flatMap { wb ->
                        val req = request.toModel(wb.keyMs, translator)
                        val rs: Rs<AddWorksheetResponse, ErrorReport> = rpcActions.createNewWorksheetRs(req)
                        rs
                    }
                    val rt = SingleSignalResponse.fromRs(rs).toProto()
                    rt
                }.await()
            }
            responseObserver.onNextAndComplete(rt)
        }
    }

    override fun createNewWorksheet(
        request: WorkbookProtos.CreateNewWorksheetRequestProto?,
        responseObserver: StreamObserver<WorkbookProtos.WorksheetWithErrorReportMsgProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                async(actionDispatcherMain) {
                    // this below line is a bug
                    // it will trigger skia on non-awt thread-> exception -> crash grpc
//                async(Dispatchers.Default.limitedParallelism(1)) {
                    val req: CreateNewWorksheetRequest = request.toModel()
                    val rs = rpcActions.createNewWorksheetRs(req)
                    val res = WorksheetWithErrorReportMsg.fromRs(rs)
                    res
                }.await().toProto()
            }
            responseObserver.onNextAndComplete(rt)
        }
    }

    override fun removeWorksheet(
        request: WorksheetProtos.WorksheetIdWithIndexProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                crtScope.async(actionDispatcherMain) {
                    val req = request.toModel()
                    val rs: Rse<Unit> = rpcActions.deleteWorksheetRs(req)
                    val rt = SingleSignalResponse.fromRs(rs)
                    rt
                }.await()
            }
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun renameWorksheet(
        request: WorkbookProtos.RenameWorksheetRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                crtScope.async(actionDispatcherMain) {
                    val rs = rpcActions.renameWorksheetRs(request.toModel())
                    val rt = SingleSignalResponse.fromRs(rs)
                    rt
                }.await()
            }
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun wsCount(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<Int64Value>?
    ) {
        if (request != null && responseObserver != null) {
            val wbKey = request.toModel()
            val wb = dc.getWb(wbKey)
            responseObserver.onNext(Int64Value.of((wb?.size ?: 0).toLong()))
            responseObserver.onCompleted()
        }
    }
}
