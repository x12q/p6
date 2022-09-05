package com.qxdzbc.p6.di.rpc

import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.proto.rpc.app.service.AppServiceGrpc
import com.qxdzbc.p6.proto.rpc.cell.service.CellServiceGrpc
import com.qxdzbc.p6.proto.rpc.workbook.service.WorkbookServiceGrpc
import com.qxdzbc.p6.proto.rpc.worksheet.service.WorksheetServiceGrpc
import com.qxdzbc.p6.rpc.P6RpcServer
import com.qxdzbc.p6.rpc.P6RpcServerImp
import com.qxdzbc.p6.rpc.MsP6RpcServer
import com.qxdzbc.p6.rpc.document.app.AppRpcService
import com.qxdzbc.p6.rpc.document.cell.CellRpcService
import com.qxdzbc.p6.rpc.document.workbook.WorkbookRpcService
import com.qxdzbc.p6.rpc.document.worksheet.WorksheetRpcService
import dagger.Binds

@dagger.Module
interface RpcModule {
    @Binds
    @P6Singleton
    fun WorksheetServiceImplBase(i:WorksheetRpcService): WorksheetServiceGrpc.WorksheetServiceImplBase

    @Binds
    @P6Singleton
    fun AppServiceImplBase(i:AppRpcService): AppServiceGrpc.AppServiceImplBase

    @Binds
    @P6Singleton
    fun WorkbookService(i: WorkbookRpcService): WorkbookServiceGrpc.WorkbookServiceImplBase

    @Binds
    @P6Singleton
    fun CellRpcService(i: CellRpcService): CellServiceGrpc.CellServiceImplBase

    @Binds
    @P6Singleton
    fun P6RpcServer(i: P6RpcServerImp):P6RpcServer

    @Binds
    @P6Singleton
    @MsRpcServerQualifier
    fun ReactiveP6RpcServer(i:MsP6RpcServer):P6RpcServer
}
