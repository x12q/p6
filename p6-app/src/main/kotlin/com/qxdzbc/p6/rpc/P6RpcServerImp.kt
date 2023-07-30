package com.qxdzbc.p6.rpc

import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.proto.rpc.AppServiceGrpc
import com.qxdzbc.p6.proto.rpc.CellServiceGrpc
import com.qxdzbc.p6.proto.rpc.WorkbookServiceGrpc
import com.qxdzbc.p6.proto.rpc.WorksheetServiceGrpc
import com.squareup.anvil.annotations.ContributesBinding
import io.grpc.Server
import io.grpc.ServerBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class P6RpcServerImp @Inject constructor(
    private val cellRpcService: CellServiceGrpc.CellServiceImplBase,
    private val wbRpcService: WorkbookServiceGrpc.WorkbookServiceImplBase,
    private val appRpcService: AppServiceGrpc.AppServiceImplBase,
    private val wsRpcService: WorksheetServiceGrpc.WorksheetServiceImplBase,
) : P6RpcServer {
    private var _server: Server? = null
    private var _port: Int = -1
    override val port: Int
        get() = _port

    override val server: Server?
        get() = _server

    override fun start(): Rse<Unit> {
        try {
//            _port = Utils.findSocketPort()
            _port = 52533
            _server = ServerBuilder
                .forPort(_port)
                .addService(this.cellRpcService)
                .addService(this.wbRpcService)
                .addService(this.appRpcService)
                .addService(this.wsRpcService)
                .build()
            _server?.start()
            return Ok(Unit)
        } catch (e: Throwable) {
            return CommonErrors.ExceptionError
                .report("Encounter exception when trying to start rpc server", e)
                .toErr()
        }
    }

    override fun stop(): Rse<Unit> {
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
