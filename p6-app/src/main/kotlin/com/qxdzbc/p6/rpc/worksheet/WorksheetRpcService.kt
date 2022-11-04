package com.qxdzbc.p6.rpc.worksheet

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.mapError
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateRequestDM.Companion.toModel
import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.action.range.RangeIdDM.Companion.toModel
import com.qxdzbc.p6.app.action.worksheet.delete_multi.RemoveMultiCellRequest
import com.qxdzbc.p6.app.common.utils.Utils.onNextAndComplete
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.ActionDispatcherDefault
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.DocProtos.WorksheetIdProto
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.proto.rpc.WorksheetServiceGrpc
import com.qxdzbc.p6.rpc.cell.msg.CellDM
import com.qxdzbc.p6.rpc.cell.msg.CellDM.CO.toModel
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM.Companion.toModel
import com.qxdzbc.p6.rpc.common_data_structure.BoolMsg.toBoolMsgProto
import com.qxdzbc.p6.rpc.worksheet.msg.*
import com.qxdzbc.p6.rpc.worksheet.msg.CheckContainAddressRequest.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.LoadDataRequest.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM.Companion.toModel
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class,boundType=WorksheetServiceGrpc.WorksheetServiceImplBase::class)
class WorksheetRpcService @Inject constructor(
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    private val rpcActs: WorksheetRpcAction,
    @ActionDispatcherDefault
    val actionDispatcherDefault: CoroutineDispatcher
) : WorksheetServiceGrpc.WorksheetServiceImplBase() {

    private val sc: StateContainer by stateContSt

    override fun updateMultiCellContent(
        request: WorksheetProtos.MultiCellUpdateRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                async(actionDispatcherDefault) {
                    val req = request.toModel()
                    val rs = rpcActs.updateMultiCellDM(req, false)
                    val ssr = SingleSignalResponse.fromRs(rs)
                    ssr
                }.await()
            }
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }


    override fun removeAllCell(
        request: WorksheetIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                async(actionDispatcherDefault) {
                    val o = rpcActs.removeAllCell(request.toModel())
                    val rt = SingleSignalResponse.fromRs(o)
                    rt
                }.await()
            }
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun loadData(
        request: WorksheetProtos.LoadDataRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                async(actionDispatcherDefault) {
                    val req: LoadDataRequest = request.toModel()
                    val rs = rpcActs.loadDataRs(req, false)
                    val o = SingleSignalResponse.fromRs(rs)
                    o
                }.await()
            }
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getCell(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val cellId: CellIdDM = request.toModel()
            val cellRs = sc.getCellRs(cellId.wbKey, cellId.wsName, cellId.address)
            val rt = SingleSignalResponse.fromRs(cellRs)
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getAllCell(
        request: WorksheetIdProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetAllCellResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wsId: WorksheetIdDM = request.toModel()
            val ws: Worksheet? = sc.getWs(wsId)
            val cellAddressList: List<CellAddress> = (ws?.cells ?: emptyList()).map { it.address }
            responseObserver.onNextAndComplete(GetAllCellResponse(cellAddressList).toProto())
        }
    }

    override fun getCellCount(
        request: WorksheetIdProto?,
        responseObserver: StreamObserver<WorksheetProtos.CellCountResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wsId: WorksheetIdDM = request.toModel()
            val count: Int = sc.getWs(wsId)?.size ?: 0
            responseObserver.onNextAndComplete(CellCountResponse(count.toLong()).toProto())
        }
    }


    override fun getUsedRangeAddress(
        request: WorksheetIdProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetUsedRangeResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wsId: WorksheetIdDM = request.toModel()
            val ra: RangeAddress? = sc.getWs(wsId)?.usedRange
            val res = GetUsedRangeResponse(ra)
            responseObserver.onNextAndComplete(res.toProto())
        }
    }


    override fun paste(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                async(actionDispatcherDefault) {
                    val cid: CellIdDM = request.toModel()
                    val o = rpcActs.pasteRange(cid, RangeAddress(cid.address))
                    o
                }.await()
            }
            responseObserver.onNextAndComplete(SingleSignalResponse.fromRs(rt).toProto())
        }
    }

    override fun addCell(
        request: DocProtos.CellProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                async(actionDispatcherDefault) {
                    val i: CellDM = request.toModel()
                    val o = rpcActs.updateCellDM(
                        CellUpdateRequestDM(
                            cellId = i.id,
                            cellContent = i.content
                        )
                    )
                    o
                }.await()
            }
            responseObserver.onNextAndComplete(SingleSignalResponse.fromRs(rt).toProto())
        }
    }

    override fun removeCell(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                async(actionDispatcherDefault) {
                    val i: CellIdDM = request.toModel()
                    val o = rpcActs.deleteMultiCell(
                        RemoveMultiCellRequest(
                            wbKey = i.wbKey,
                            wsName = i.wsName,
                            ranges = emptyList(),
                            cells = listOf(i.address),
                            clearFormat = false,
                            windowId = null
                        )
                    )
                    o
                }.await()
            }
            responseObserver.onNextAndComplete(
                SingleSignalResponse.fromRs(rt.mapError { it.errorReport }).toProto()
            )
        }
    }

    override fun removeRange(
        request: DocProtos.RangeIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val rt = runBlocking {
                async(actionDispatcherDefault) {
                    val i = request.toModel()
                    val o = rpcActs.deleteMultiCell(
                        RemoveMultiCellRequest(
                            wbKey = i.wbKey,
                            wsName = i.wsName,
                            ranges = listOf(i.rangeAddress),
                            cells = listOf(),
                            clearFormat = false,
                            windowId = null
                        )
                    )
                    o
                }.await()
            }
            responseObserver.onNextAndComplete(
                SingleSignalResponse.fromRs(rt.mapError { it.errorReport }).toProto()
            )
        }
    }

    override fun containAddress(
        request: WorksheetProtos.CheckContainAddressRequestProto?,
        responseObserver: StreamObserver<CommonProtos.BoolMsgProto>?
    ) {
        if (request != null && responseObserver != null) {
            val i: CheckContainAddressRequest = request.toModel()
            val o = sc.getWs(i.wsId)?.rangeConstraint?.contains(i.cellAddress) ?: false
            responseObserver.onNextAndComplete(o.toBoolMsgProto())
        }
    }
}
