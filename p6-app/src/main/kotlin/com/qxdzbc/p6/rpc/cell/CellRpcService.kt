package com.qxdzbc.p6.rpc.cell

import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM.Companion.toModel
import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.common.utils.Utils.onNextAndComplete
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.CellContent
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.di.qualifiers.ActionDispatcherDefault
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.proto.CellProtos
import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.rpc.CellServiceGrpc
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM.Companion.toModel
import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest.Companion.toModel
import com.qxdzbc.p6.rpc.common_data_structure.StrMsg
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class,boundType= CellServiceGrpc.CellServiceImplBase::class)
class CellRpcService @Inject constructor(
    val stateCont:StateContainer,
    val acts: CellRpcActions,
    @ActionDispatcherDefault
    val actionDispatcherDefault: CoroutineDispatcher
) : CellServiceGrpc.CellServiceImplBase() {

    private val sc  = stateCont

    override fun updateCellContent(
        request: CellProtos.CellUpdateRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                async(actionDispatcherDefault) {
                    val req = request.toModel()
                    val o = acts.updateCellDM(req, false)
                    o
                }.await()
            }
            responseObserver.onNextAndComplete(SingleSignalResponse.fromRs(rt).toProto())

        }
    }

    override fun getDisplayText(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.StrMsgProto>?
    ) {
        if (request != null && responseObserver != null) {
            val cid: CellIdDM = request.toModel()
            val cell: Cell? = sc.getCellOrDefault(cid)
            val rt = StrMsg(cell?.cachedDisplayText ?: "")
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getFormula(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.StrMsgProto>?
    ) {
        if (request != null && responseObserver != null) {
            val cid: CellIdDM = request.toModel()
            val cell: Cell? = sc.getCellOrDefault(cid)
            val rt = StrMsg(cell?.fullFormulaFromExUnit ?: "")
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getCellValue(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<DocProtos.CellValueProto>?
    ) {
        if (request != null && responseObserver != null) {
            val cid: CellIdDM = request.toModel()
            val cell: Cell? = sc.getCellOrDefault(cid)
            val rt: CellValue = cell?.currentCellValue ?: CellValue.empty
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getCellContent(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<DocProtos.CellContentProto>?
    ) {
        if (request != null && responseObserver != null) {
            val cid: CellIdDM = request.toModel()
            val cell: Cell? = sc.getCellOrDefault(cid)
            val rt: CellContent = cell?.content ?: CellContentImp.empty
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun copyFrom(
        request: CellProtos.CopyCellRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                async(actionDispatcherDefault) {
                    val req = request.toModel()
                    val rt = acts.copyCellWithoutClipboard(req,false)
                    rt
                }.await()
            }
            responseObserver.onNextAndComplete(SingleSignalResponse.fromRs(rt).toProto())
        }
    }
}
