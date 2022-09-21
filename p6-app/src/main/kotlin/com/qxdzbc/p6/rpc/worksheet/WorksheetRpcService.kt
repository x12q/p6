package com.qxdzbc.p6.rpc.worksheet

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.mapError
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest2
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateRequestDM.Companion.toModel
import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.action.range.IndRangeIdImp.Companion.toModel
import com.qxdzbc.p6.app.action.worksheet.delete_multi.RemoveMultiCellRequest
import com.qxdzbc.p6.app.common.utils.CoroutineUtils
import com.qxdzbc.p6.app.common.utils.Utils.onNextAndComplete
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.AppCoroutineScope
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.DocProtos.WorksheetIdProto
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.proto.rpc.WorksheetServiceGrpc
import com.qxdzbc.p6.rpc.cell.msg.CellProtoDM
import com.qxdzbc.p6.rpc.cell.msg.CellProtoDM.CO.toModel
import com.qxdzbc.p6.rpc.common_data_structure.BoolMsg.toBoolMsgProto
import com.qxdzbc.p6.rpc.worksheet.msg.*
import com.qxdzbc.p6.rpc.worksheet.msg.CheckContainAddressRequest.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.CellIdProtoDM.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.LoadDataRequest.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM.Companion.toModel
import com.qxdzbc.p6.ui.app.state.StateContainer
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class WorksheetRpcService @Inject constructor(
    @StateContainerSt
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    private val rpcActs:WorksheetRpcAction,
    @AppCoroutineScope
    val crtScope: CoroutineScope
) : WorksheetServiceGrpc.WorksheetServiceImplBase() {

    private val sc: StateContainer by stateContSt
    val launchOnMain = CoroutineUtils.makeLaunchOnMain(crtScope)

    override fun updateMultiCellContent(
        request: WorksheetProtos.MultiCellUpdateRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if(request!=null && responseObserver!=null){
            val req = request.toModel()
            val rs = rpcActs.updateMultiCell(req,false)
            val ssr = SingleSignalResponse.fromRs(rs)
            responseObserver.onNextAndComplete(ssr.toProto())
        }
    }

    override fun removeAllCell(
        request: WorksheetIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if(request!=null && responseObserver!=null){
            launchOnMain{
                val o = rpcActs.removeAllCell(request.toModel())
                val rt = SingleSignalResponse.fromRs(o)
                responseObserver.onNextAndComplete(rt.toProto())
            }
        }
    }

    override fun loadData(
        request: WorksheetProtos.LoadDataRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if(request!=null && responseObserver!=null){
            launchOnMain{
                val req: LoadDataRequest = request.toModel()
                val rs = rpcActs.loadDataRs(req, false)
                val o = SingleSignalResponse.fromRs(rs)
                responseObserver.onNextAndComplete(o.toProto())
            }
        }
    }

    override fun getCell(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val cellId: CellIdProtoDM = request.toModel()
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
            launchOnMain{
                val cid: CellIdProtoDM = request.toModel()
                val o = rpcActs.pasteRange(cid, RangeAddress(cid.address))
                responseObserver.onNextAndComplete(SingleSignalResponse.fromRs(o).toProto())
            }
        }
    }

    override fun addCell(
        request: DocProtos.CellProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            launchOnMain{
                val i: CellProtoDM = request.toModel()
                val o = rpcActs.updateCell2(
                    CellUpdateRequest2(
                        wbKey = i.id.wbKey,
                        wsName = i.id.wsName,
                        cellAddress = i.id.address,
                        cellContent = i.content                    )
                )
                responseObserver.onNextAndComplete(SingleSignalResponse.fromRs(o).toProto())
            }
        }
    }

    override fun removeCell(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            launchOnMain{
                val i: CellIdProtoDM = request.toModel()
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
                responseObserver.onNextAndComplete(
                    SingleSignalResponse.fromRs(o.mapError { it.errorReport }).toProto()
                )
            }
        }
    }

    override fun deleteRange(
        request: DocProtos.RangeIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            launchOnMain{
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
                responseObserver.onNextAndComplete(
                    SingleSignalResponse.fromRs(o.mapError { it.errorReport }).toProto()
                )
            }
        }
    }

    override fun containAddress(
        request: WorksheetProtos.CheckContainAddressRequestProto?,
        responseObserver: StreamObserver<CommonProtos.BoolMsgProto>?
    ) {
        if(request!=null && responseObserver!=null){
            val i: CheckContainAddressRequest = request.toModel()
            val o = sc.getWs(i.wsId)?.rangeConstraint?.contains(i.cellAddress) ?: false
            responseObserver.onNextAndComplete(o.toBoolMsgProto())
        }
    }
}
