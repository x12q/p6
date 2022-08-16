package com.emeraldblast.p6.rpc.document.workbook

import com.emeraldblast.p6.proto.DocProtos
import com.emeraldblast.p6.proto.rpc.workbook.service.WorkbookServiceGrpc
import com.google.protobuf.Empty
import com.google.protobuf.Int64Value
import io.grpc.stub.StreamObserver
import javax.inject.Inject

class MockWorkbookService @Inject constructor() : WorkbookServiceGrpc.WorkbookServiceImplBase() {
    override fun sheetCount(request: DocProtos.WorkbookKeyProto?, responseObserver: StreamObserver<Int64Value>?) {
            responseObserver?.onNext(Int64Value.of(123))
        responseObserver?.onCompleted()
    }
}
