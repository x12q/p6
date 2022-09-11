package com.qxdzbc.p6.rpc.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerErrors
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest.Companion.toModel
import com.qxdzbc.p6.app.action.global.GlobalAction
import com.qxdzbc.p6.app.common.utils.Utils.onNextAndComplete
import com.qxdzbc.p6.proto.AppProtos
import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.proto.rpc.AppServiceGrpc
import io.grpc.stub.StreamObserver
import javax.inject.Inject

class AppRpcService @Inject constructor(
    @AppStateMs
    val appStateMs: Ms<AppState>,
    val globalActions:GlobalAction,
) : AppServiceGrpc.AppServiceImplBase() {

    private var appState by appStateMs
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
            for (wb in appState.globalWbCont.wbList) {
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
            val wb = appState.globalWbCont.wbList.getOrNull(index.toInt())
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
        if(request!=null && responseObserver!=null){
            val req: CreateNewWorkbookRequest = request.toModel()
            val o = globalActions.createNewWb(req)
            responseObserver.onNextAndComplete(o.toProto())
        }else{
            super.createNewWorkbook(request, responseObserver)
        }
    }

    override fun getActiveWorkbook(
        request: CommonProtos.EmptyProto?,
        responseObserver: StreamObserver<AppProtos.WorkbookKeyWithErrorResponseProto>?
    ) {
        super.getActiveWorkbook(request, responseObserver)
    }

    override fun setActiveWorkbook(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        super.setActiveWorkbook(request, responseObserver)
    }

    override fun getActiveWorksheet(
        request: CommonProtos.EmptyProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetWorksheetResponseProto>?
    ) {
        super.getActiveWorksheet(request, responseObserver)
    }

    override fun saveWorkbookAtPath(
        request: AppProtos.SaveWorkbookRequestProto?,
        responseObserver: StreamObserver<AppProtos.SaveWorkbookResponseProto>?
    ) {
        super.saveWorkbookAtPath(request, responseObserver)
    }

    override fun loadWorkbook(
        request: AppProtos.LoadWorkbookRequestProto?,
        responseObserver: StreamObserver<AppProtos.LoadWorkbookResponseProto>?
    ) {
        super.loadWorkbook(request, responseObserver)
    }

    override fun closeWorkbook(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        super.closeWorkbook(request, responseObserver)
    }

    override fun checkWbExistence(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<CommonProtos.BoolMsgProto>?
    ) {
        super.checkWbExistence(request, responseObserver)
    }

    override fun getAllWorkbooks(
        request: CommonProtos.EmptyProto?,
        responseObserver: StreamObserver<AppProtos.GetAllWorkbookResponseProto>?
    ) {
        super.getAllWorkbooks(request, responseObserver)
    }
}
