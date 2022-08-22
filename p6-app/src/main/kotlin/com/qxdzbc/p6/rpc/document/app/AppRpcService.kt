package com.qxdzbc.p6.rpc.document.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerErrors
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.common.exception.error.CommonErrors
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.proto.rpc.app.AppServiceProtos
import com.qxdzbc.p6.proto.rpc.app.service.AppServiceGrpc.AppServiceImplBase
import com.qxdzbc.p6.proto.rpc.app.AppServiceProtos.*
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.grpc.stub.StreamObserver
import javax.inject.Inject

class AppRpcService @Inject constructor(
    @AppStateMs
    val appStateMs: Ms<AppState>
) : AppServiceImplBase() {

    private var appState by appStateMs
    override fun getWorkbook(
        request: AppServiceProtos.GetWorkbookRequestProto,
        responseObserver: StreamObserver<WorkbookKeyWithErrorResponseProto>
    ) {
        val o = if (request.hasWbKey()) {
            val wbKey = request.wbKey.toModel()
            val rs = appState.getWbRs(wbKey)
            val response = when (rs) {
                is Ok -> WorkbookKeyWithErrorResponseProto.newBuilder()
                    .setWbKey(request.wbKey)
                    .build()
                is Err -> WorkbookKeyWithErrorResponseProto.newBuilder()
                    .setErrorReport(rs.error.toProto())
                    .build()
            }
            response
        } else if (request.hasWbName()) {
            val wbName = request.wbName
            for (wb in appState.globalWbCont.wbList) {
                if (wb.key.name == wbName) {
                    WorkbookKeyWithErrorResponseProto.newBuilder()
                        .setWbKey(wb.key.toProto())
                        .build()
                }
            }
            WorkbookKeyWithErrorResponseProto.newBuilder()
                .setErrorReport(WorkbookContainerErrors.InvalidWorkbook.reportDefault(wbName).toProto())
                .build()
        } else if (request.hasWbIndex()) {
            val index = request.wbIndex
            val wb = appState.globalWbCont.wbList.getOrNull(index.toInt())
            if (wb != null) {
                WorkbookKeyWithErrorResponseProto.newBuilder()
                    .setWbKey(wb.key.toProto())
                    .build()
            } else {
                WorkbookKeyWithErrorResponseProto.newBuilder()
                    .setErrorReport(WorkbookContainerErrors.InvalidWorkbook.report(index.toInt()).toProto())
                    .build()
            }
        } else {
            WorkbookKeyWithErrorResponseProto.newBuilder()
                .setErrorReport(CommonErrors.Unknown.report("Unknown error when trying to get workbook").toProto())
                .build()
        }

        responseObserver.onNext(o)
        responseObserver.onCompleted()
    }
}
