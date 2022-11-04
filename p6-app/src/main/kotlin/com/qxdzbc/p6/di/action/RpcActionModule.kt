package com.qxdzbc.p6.di.action

import com.qxdzbc.p6.app.action.rpc.AppRpcAction
import com.qxdzbc.p6.app.action.rpc.AppRpcActionImp
import com.qxdzbc.p6.app.action.rpc.AppRpcActions
import com.qxdzbc.p6.app.action.rpc.AppRpcActionsImp
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.rpc.cell.CellRpcActions
import com.qxdzbc.p6.rpc.cell.CellRpcActionsImp
import com.qxdzbc.p6.rpc.workbook.WorkbookRpcActions
import com.qxdzbc.p6.rpc.workbook.WorkbookRpcActionsImp
import com.qxdzbc.p6.rpc.worksheet.WorksheetRpcAction
import com.qxdzbc.p6.rpc.worksheet.WorksheetRpcActionImp
import dagger.Binds

@dagger.Module
interface RpcActionModule {
//    @Binds
//    @P6Singleton
//    fun CellRpcActions(i: CellRpcActionsImp):CellRpcActions
//
//    @Binds
//    @P6Singleton
//    fun WorkbookRpcActions(i:WorkbookRpcActionsImp): WorkbookRpcActions
//
//    @Binds
//    @P6Singleton
//    fun WorksheetRpcAction(i:WorksheetRpcActionImp): WorksheetRpcAction
//
//    @Binds
//    @P6Singleton
//    fun rpcAction(i: AppRpcActionsImp):AppRpcActions
//
//    @Binds
//    @P6Singleton
//    fun AppRpcAction(i:AppRpcActionImp): AppRpcAction
}
