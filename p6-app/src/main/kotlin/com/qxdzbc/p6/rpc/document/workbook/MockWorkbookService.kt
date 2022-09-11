package com.qxdzbc.p6.rpc.document.workbook

import com.google.protobuf.Int64Value
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.rpc.WorkbookServiceGrpc
import io.grpc.stub.StreamObserver
import javax.inject.Inject

class MockWorkbookService @Inject constructor() : WorkbookServiceGrpc.WorkbookServiceImplBase() {
    override fun sheetCount(request: DocProtos.WorkbookKeyProto?, responseObserver: StreamObserver<Int64Value>?) {
            responseObserver?.onNext(Int64Value.of(123))
        responseObserver?.onCompleted()
    }
}
