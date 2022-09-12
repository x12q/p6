package com.qxdzbc.p6.rpc.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest.Companion.toModel
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookRequest.Companion.toModel
import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.action.global.GlobalAction
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.common.utils.Utils.onNextAndComplete
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerErrors
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.proto.AppProtos
import com.qxdzbc.p6.proto.AppProtos.GetAllWorkbookResponseProto
import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.proto.rpc.AppServiceGrpc
import com.qxdzbc.p6.rpc.common_data_structure.BoolMsg.toBoolMsgProto
import com.qxdzbc.p6.rpc.workbook.msg.GetWorksheetResponse
import com.qxdzbc.p6.rpc.workbook.msg.WorkbookKeyWithErrorResponse
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.AppStateErrors
import com.qxdzbc.p6.ui.app.state.StateContainer
import io.grpc.stub.StreamObserver
import java.nio.file.Path
import javax.inject.Inject

class AppRpcService @Inject constructor(
    @AppStateMs
    val appStateMs: Ms<AppState>,
    @StateContainerSt
    val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    val globalActions: GlobalAction,
) : AppServiceGrpc.AppServiceImplBase() {

    private var appState by appStateMs
    private val stateCont by stateContSt
    override fun getWorkbook(
        request: AppProtos.GetWorkbookRequestProto,
        responseObserver: StreamObserver<AppProtos.WorkbookKeyWithErrorResponseProto>
    ) {
        val o = if (request.hasWbKey()) {
            val wbKey = request.wbKey.toModel()
            val rs = appState.getWbRs(wbKey)
            val response = when (rs) {
                is Ok -> AppProtos.WorkbookKeyWithErrorResponseProto.newBuilder()
                    .setWbKey(request.wbKey)
                    .build()
                is Err -> AppProtos.WorkbookKeyWithErrorResponseProto.newBuilder()
                    .setErrorReport(rs.error.toProto())
                    .build()
            }
            response
        } else if (request.hasWbName()) {
            val wbName = request.wbName
            for (wb in appState.wbCont.wbList) {
                if (wb.key.name == wbName) {
                    AppProtos.WorkbookKeyWithErrorResponseProto.newBuilder()
                        .setWbKey(wb.key.toProto())
                        .build()
                }
            }
            AppProtos.WorkbookKeyWithErrorResponseProto.newBuilder()
                .setErrorReport(WorkbookContainerErrors.InvalidWorkbook.reportDefault(wbName).toProto())
                .build()
        } else if (request.hasWbIndex()) {
            val index = request.wbIndex
            val wb = appState.wbCont.wbList.getOrNull(index.toInt())
            if (wb != null) {
                AppProtos.WorkbookKeyWithErrorResponseProto.newBuilder()
                    .setWbKey(wb.key.toProto())
                    .build()
            } else {
                AppProtos.WorkbookKeyWithErrorResponseProto.newBuilder()
                    .setErrorReport(WorkbookContainerErrors.InvalidWorkbook.report(index.toInt()).toProto())
                    .build()
            }
        } else {
            AppProtos.WorkbookKeyWithErrorResponseProto.newBuilder()
                .setErrorReport(CommonErrors.Unknown.report("Unknown error when trying to get workbook").toProto())
                .build()
        }
        responseObserver.onNext(o)
        responseObserver.onCompleted()
    }

    override fun createNewWorkbook(
        request: AppProtos.CreateNewWorkbookRequestProto?,
        responseObserver: StreamObserver<AppProtos.CreateNewWorkbookResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val req: CreateNewWorkbookRequest = request.toModel()
            val o = globalActions.createNewWb(req)
            responseObserver.onNextAndComplete(o.toProto())
        }
    }

    override fun getActiveWorkbook(
        request: CommonProtos.EmptyProto?,
        responseObserver: StreamObserver<AppProtos.WorkbookKeyWithErrorResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wbk: WorkbookKey? = appState.activeWindowState?.activeWorkbookState?.wb?.key
            val rt = WorkbookKeyWithErrorResponse(
                wbKey = wbk,
                errorReport = wbk?.let { null } ?: AppStateErrors.NoActiveWorkbook.report()
            )
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun setActiveWorkbook(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wbk: WorkbookKey = request.toModel()
            val r = globalActions.setActiveWb(wbk)
            responseObserver.onNextAndComplete(SingleSignalResponse.fromRs(r).toProto())
        }
    }

    override fun getActiveWorksheet(
        request: CommonProtos.EmptyProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetWorksheetResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val ws = appState.activeWindowState?.activeWorkbookState?.activeSheetState?.worksheet
            responseObserver.onNextAndComplete(GetWorksheetResponse(wsId = ws?.id).toProto())
        }
    }

    override fun saveWorkbookAtPath(
        request: AppProtos.SaveWorkbookRequestProto?,
        responseObserver: StreamObserver<AppProtos.SaveWorkbookResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val r = globalActions.saveWorkbook(request.wbKey.toModel(), Path.of(request.path))
            responseObserver.onNextAndComplete(r.toProto())
        }
    }

    override fun loadWorkbook(
        request: AppProtos.LoadWorkbookRequestProto?,
        responseObserver: StreamObserver<AppProtos.LoadWorkbookResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val req = request.toModel()
            val o = globalActions.loadWorkbook(req)
            responseObserver.onNextAndComplete(o.toProto())
        }
    }

    override fun closeWorkbook(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val req = CloseWorkbookRequest(wbKey = request.toModel())
            val o = globalActions.closeWb(req)
            val s = SingleSignalResponse.fromWithErr(o)
            responseObserver.onNextAndComplete(s.toProto())
        }
    }

    override fun checkWbExistence(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<CommonProtos.BoolMsgProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wbk = request.toModel()
            val r = stateCont.getWb(wbk)!=null
            responseObserver.onNextAndComplete(r.toBoolMsgProto())
        }
    }

    override fun getAllWorkbooks(
        request: CommonProtos.EmptyProto?,
        responseObserver: StreamObserver<AppProtos.GetAllWorkbookResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wbkList = stateCont.wbCont.wbList.map{it.key}
            val r=GetAllWorkbookResponseProto
                .newBuilder()
                .addAllWbKeys(wbkList.map{it.toProto()})
                .build()
            responseObserver.onNextAndComplete(r)
        }
    }
}
