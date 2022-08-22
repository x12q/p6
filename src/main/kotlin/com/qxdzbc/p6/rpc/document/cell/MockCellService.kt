package com.qxdzbc.p6.rpc.document.cell

import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.proto.rpc.cell.CellServiceProtos
import com.qxdzbc.p6.proto.rpc.cell.service.CellServiceGrpc
import io.grpc.stub.StreamObserver

class MockCellService constructor(
) : CellServiceGrpc.CellServiceImplBase() {
    override fun getCellValue(
        request: CellServiceProtos.GetCellRequest,
        responseObserver: StreamObserver<CellServiceProtos.GetCellResponse>
    ) {
        val id = request.cellId
        val response =
            "${request.cellId.cellAddress.toModel().toRawLabel()} is asd"
        responseObserver.onNext(CellServiceProtos.GetCellResponse.newBuilder().setCellValue(response).build())
        responseObserver.onCompleted()
    }
}
