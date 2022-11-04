package com.qxdzbc.p6.di.rpc

import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.proto.rpc.AppServiceGrpc
import com.qxdzbc.p6.proto.rpc.CellServiceGrpc
import com.qxdzbc.p6.proto.rpc.WorkbookServiceGrpc
import com.qxdzbc.p6.proto.rpc.WorksheetServiceGrpc
import com.qxdzbc.p6.rpc.MsP6RpcServer
import com.qxdzbc.p6.rpc.P6RpcServer
import com.qxdzbc.p6.rpc.P6RpcServerImp
import com.qxdzbc.p6.rpc.app.AppRpcService
import com.qxdzbc.p6.rpc.cell.CellRpcService
import com.qxdzbc.p6.rpc.workbook.WorkbookRpcService
import com.qxdzbc.p6.rpc.worksheet.WorksheetRpcService
import dagger.Binds

@dagger.Module
interface RpcModule {
//    @Binds
//    @P6Singleton
//    fun WorksheetServiceImplBase(i: WorksheetRpcService): WorksheetServiceGrpc.WorksheetServiceImplBase
//
//    @Binds
//    @P6Singleton
//    fun AppServiceImplBase(i: AppRpcService): AppServiceGrpc.AppServiceImplBase
//
//    @Binds
//    @P6Singleton
//    fun WorkbookService(i: WorkbookRpcService): WorkbookServiceGrpc.WorkbookServiceImplBase
//
//    @Binds
//    @P6Singleton
//    fun CellRpcService(i: CellRpcService): CellServiceGrpc.CellServiceImplBase
//
//    @Binds
//    @P6Singleton
//    fun P6RpcServer(i: P6RpcServerImp):P6RpcServer
//
//    @Binds
//    @P6Singleton
//    @MsRpcServerQualifier
//    fun ReactiveP6RpcServer(i:MsP6RpcServer):P6RpcServer
}
