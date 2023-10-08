package com.qxdzbc.p6.rpc

import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.p6.common.utils.Utils
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.proto.rpc.AppServiceGrpc
import com.qxdzbc.p6.proto.rpc.CellServiceGrpc
import com.qxdzbc.p6.proto.rpc.WorkbookServiceGrpc
import com.qxdzbc.p6.proto.rpc.WorksheetServiceGrpc
import com.squareup.anvil.annotations.ContributesBinding
import io.grpc.Server
import io.grpc.ServerBuilder
import java.net.ServerSocket
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


    private fun findPort():Int{
        val defaultPort:Int = P6RPCServerConsts.defaultPort
        try{
            val socket = ServerSocket(defaultPort)
            socket.close()
            return defaultPort
        }catch (e:Throwable){
            val socket = ServerSocket(0)
            val port = socket.localPort
            socket.close()
            return port
        }
    }

    /**
     * This function needs to be able to test a default port and find open port if the default port is already occupied.
     */
    override fun start(): Rse<Unit> {
        try {
            _port = findPort()
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
