package com.qxdzbc.p6.rpc.document.cell

import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.rpc.cell.CellServiceProtos.*
import com.qxdzbc.p6.proto.rpc.cell.service.CellServiceGrpc
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.common.compose.Ms
import io.grpc.stub.StreamObserver
import javax.inject.Inject


class CellRpcService @Inject constructor(
    @AppStateMs
    val appStateMs:Ms<AppState>
) : CellServiceGrpc.CellServiceImplBase() {
    override fun getCellValue(
        request: GetCellRequest,
        responseObserver: StreamObserver<GetCellResponse>
    ) {
        val id = request.cellId
        val cell = appStateMs.value.getCell(
            wbKey = id.wbKey.toModel(),
            wsName = id.wsName,
            cellAddress = id.cellAddress.toModel()
        )
        val response =
            "${request.cellId.cellAddress.toModel().toRawLabel()} is ${cell?.currentValue?.toString() ?: "null"}"
        responseObserver.onNext(GetCellResponse.newBuilder().setCellValue(response).build())
        responseObserver.onCompleted()
    }
}


