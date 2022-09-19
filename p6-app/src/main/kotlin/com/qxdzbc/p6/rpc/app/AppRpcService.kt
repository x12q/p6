package com.qxdzbc.p6.rpc.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest.Companion.toModel
import com.qxdzbc.p6.app.action.app.get_wb.GetWorkbookRequest.Companion.toModel
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookRequest.Companion.toModel
import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.action.rpc.AppRpcAction
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.common.utils.CoroutineUtils
import com.qxdzbc.p6.app.common.utils.Utils.onNextAndComplete
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.di.AppCoroutineScope
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.proto.AppProtos
import com.qxdzbc.p6.proto.AppProtos.GetAllWorkbookResponseProto
import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.proto.rpc.AppServiceGrpc
import com.qxdzbc.p6.rpc.common_data_structure.BoolMsg.toBoolMsgProto
import com.qxdzbc.p6.rpc.workbook.msg.GetWorksheetResponse
import com.qxdzbc.p6.rpc.workbook.msg.WorkbookKeyWithErrorResponse
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.CoroutineScope
import java.nio.file.Path
import javax.inject.Inject

class AppRpcService @Inject constructor(
    @AppStateMs
    val appStateMs: Ms<AppState>,
    @StateContainerSt
    val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    val rpcActions: AppRpcAction,
    @AppCoroutineScope
    val crtScope: CoroutineScope
) : AppServiceGrpc.AppServiceImplBase() {

    val launchOnMain = CoroutineUtils.makeLaunchOnMain(crtScope)
    private var aps by appStateMs
    private val sc by stateContSt

    override fun getWorkbook(
        request: AppProtos.GetWorkbookRequestProto,
        responseObserver: StreamObserver<AppProtos.WorkbookKeyWithErrorResponseProto>
    ) {
        val rs = rpcActions.getWbRs(request.toModel())
        val o = AppProtos.WorkbookKeyWithErrorResponseProto.newBuilder()
            .apply {
                when(rs){
                    is Ok ->{
                        setWbKey(rs.value.key.toProto())
                    }
                    is Err->{
                        setErrorReport(rs.error.toProto())
                    }
                }
            }.build()
        responseObserver.onNextAndComplete(o)
    }

    override fun createNewWorkbook(
        request: AppProtos.CreateNewWorkbookRequestProto?,
        responseObserver: StreamObserver<AppProtos.CreateNewWorkbookResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            launchOnMain{
                val req: CreateNewWorkbookRequest = request.toModel()
                val o = rpcActions.createNewWb(req)
                responseObserver.onNextAndComplete(o.toProto())
            }
        }
    }

    override fun getActiveWorkbook(
        request: CommonProtos.EmptyProto?,
        responseObserver: StreamObserver<AppProtos.WorkbookKeyWithErrorResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wbk: Rse<Workbook> = sc.getActiveWorkbookRs()
            val rt = WorkbookKeyWithErrorResponse(
                wbKey = wbk.component1()?.key,
                errorReport = wbk.component2()
            )
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun setActiveWorkbook(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            launchOnMain{
                val wbk: WorkbookKey = request.toModel()
                val r = rpcActions.setActiveWb(wbk)
                responseObserver.onNextAndComplete(SingleSignalResponse.fromRs(r).toProto())
            }        }
    }

    override fun getActiveWorksheet(
        request: CommonProtos.EmptyProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetWorksheetResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val ws = aps.activeWindowState?.activeWbState?.activeSheetState?.worksheet
            responseObserver.onNextAndComplete(GetWorksheetResponse(wsId = ws?.id).toProto())
        }
    }

    override fun saveWorkbookAtPath(
        request: AppProtos.SaveWorkbookRequestProto?,
        responseObserver: StreamObserver<AppProtos.SaveWorkbookResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            launchOnMain{
                val r = rpcActions.saveWorkbook(request.wbKey.toModel(), Path.of(request.path))
                responseObserver.onNextAndComplete(r.toProto())
            }
        }
    }

    override fun loadWorkbook(
        request: AppProtos.LoadWorkbookRequestProto?,
        responseObserver: StreamObserver<AppProtos.LoadWorkbookResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            launchOnMain{
                val req = request.toModel()
                val o = rpcActions.loadWorkbook(req)
                responseObserver.onNextAndComplete(o.toProto())
            }        }
    }

    override fun closeWorkbook(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            launchOnMain{
                val req = CloseWorkbookRequest(wbKey = request.toModel())
                val o = rpcActions.closeWb(req)
                val s = SingleSignalResponse.fromWithErr(o)
                responseObserver.onNextAndComplete(s.toProto())
            }        }
    }

    override fun checkWbExistence(
        request: DocProtos.WorkbookKeyProto?,
        responseObserver: StreamObserver<CommonProtos.BoolMsgProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wbk = request.toModel()
            val r = sc.getWb(wbk)!=null
            responseObserver.onNextAndComplete(r.toBoolMsgProto())
        }
    }

    override fun getAllWorkbooks(
        request: CommonProtos.EmptyProto?,
        responseObserver: StreamObserver<AppProtos.GetAllWorkbookResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wbkList = sc.wbCont.wbList.map{it.key}
            val r=GetAllWorkbookResponseProto
                .newBuilder()
                .addAllWbKeys(wbkList.map{it.toProto()})
                .build()
            responseObserver.onNextAndComplete(r)
        }
    }
}
