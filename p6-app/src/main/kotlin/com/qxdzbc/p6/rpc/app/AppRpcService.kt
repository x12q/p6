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
import com.qxdzbc.p6.proto.AppProtos
import com.qxdzbc.p6.proto.rpc.AppServiceGrpc
import io.grpc.stub.StreamObserver
import javax.inject.Inject

class AppRpcService @Inject constructor(
    @AppStateMs
    val appStateMs: Ms<AppState>
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
}
