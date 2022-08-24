package com.qxdzbc.p6.rpc

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.proto.rpc.app.service.AppServiceGrpc
import com.qxdzbc.p6.proto.rpc.cell.service.CellServiceGrpc
import com.qxdzbc.p6.proto.rpc.workbook.service.WorkbookServiceGrpc
import com.github.michaelbull.result.Ok
import io.grpc.Server
import io.grpc.ServerBuilder
import javax.inject.Inject


class P6RpcServerImp @Inject constructor(
    private val cellRpcService: CellServiceGrpc.CellServiceImplBase,
    private val wbRpcService: WorkbookServiceGrpc.WorkbookServiceImplBase,
    private val appRpcService: AppServiceGrpc.AppServiceImplBase,
) : P6RpcServer {
    private var _server: Server? = null
    private var _port: Int = -1
    override val port: Int
        get() = _port

    override val server: Server?
        get() = _server

    override fun start(): Rs<Unit, ErrorReport> {
        try {
//            _port = Utils.findSocketPort()
            _port = 50052
            _server = ServerBuilder
                .forPort(_port)
                .addService(this.cellRpcService)
                .addService(this.wbRpcService)
                .addService(this.appRpcService)
                .build()
            _server?.start()
            return Ok(Unit)
        } catch (e: Throwable) {
            return CommonErrors.ExceptionError
                .report("Encounter exception when trying to start rpc server", e)
                .toErr()
        }
    }

    override fun stop(): Rs<Unit, ErrorReport> {
        try {
            _server?.shutdownNow()
            _server = null
            _port = -1
            return Ok(Unit)
        } catch (e: Throwable) {
            return CommonErrors.ExceptionError
                .report("Encounter exception when trying to stop rpc server", e)
                .toErr()
        }
    }
}
