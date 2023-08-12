package com.qxdzbc.p6.rpc.worksheet

import com.github.michaelbull.result.mapError
import com.qxdzbc.p6.composite_actions.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.composite_actions.cell.multi_cell_update.UpdateMultiCellRequestDM.Companion.toModel
import com.qxdzbc.p6.composite_actions.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.composite_actions.range.RangeIdDM.Companion.toModel
import com.qxdzbc.p6.composite_actions.worksheet.delete_multi.DeleteMultiCellRequest
import com.qxdzbc.p6.common.utils.Utils.onNextAndComplete
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.di.qualifiers.ActionDispatcherDefault
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.DocProtos.WorksheetIdProto
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.proto.rpc.WorksheetServiceGrpc
import com.qxdzbc.p6.rpc.cell.msg.CellDM
import com.qxdzbc.p6.rpc.cell.msg.CellDM.Companion.toModel
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM.Companion.toModel
import com.qxdzbc.p6.rpc.common_data_structure.BoolMsg.toBoolMsgProto
import com.qxdzbc.p6.rpc.worksheet.msg.*
import com.qxdzbc.p6.rpc.worksheet.msg.CheckContainAddressRequest.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.LoadDataRequest.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM.Companion.toModelDM
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class,boundType=WorksheetServiceGrpc.WorksheetServiceImplBase::class)
class WorksheetRpcService @Inject constructor(
    private val stateCont:StateContainer,
    private val rpcActs: WorksheetRpcAction,
    @ActionDispatcherDefault
    val actionDispatcherDefault: CoroutineDispatcher
) : WorksheetServiceGrpc.WorksheetServiceImplBase() {

    private val sc: StateContainer  = stateCont

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
                    val o = rpcActs.removeAllCell(request.toModelDM())
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
            val cellRs = sc.getCellRsOrDefault(cellId.wbKey, cellId.wsName, cellId.address)
            val rt = SingleSignalResponse.fromRs(cellRs)
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getAllCell(
        request: WorksheetIdProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetAllCellResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wsId: WorksheetIdDM = request.toModelDM()
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
            val wsId: WorksheetIdDM = request.toModelDM()
            val count: Int = sc.getWs(wsId)?.size ?: 0
            responseObserver.onNextAndComplete(CellCountResponse(count.toLong()).toProto())
        }
    }


    override fun getUsedRangeAddress(
        request: WorksheetIdProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetUsedRangeResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wsId: WorksheetIdDM = request.toModelDM()
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
                    val o = rpcActs.pasteRange(cid, RangeAddress(cid.address),true)
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
                    val o = rpcActs.deleteDataOfMultiCell(
                        DeleteMultiCellRequest(
                            wbKey = i.wbKey,
                            wsName = i.wsName,
                            ranges = emptyList(),
                            cells = listOf(i.address),
                            clearFormat = false,
                            windowId = null
                        ),
                        undoable =true
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
                    val o = rpcActs.deleteDataOfMultiCell(
                        DeleteMultiCellRequest(
                            wbKey = i.wbKey,
                            wsName = i.wsName,
                            ranges = listOf(i.rangeAddress),
                            cells = listOf(),
                            clearFormat = false,
                            windowId = null
                        ),
                        undoable=true
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
