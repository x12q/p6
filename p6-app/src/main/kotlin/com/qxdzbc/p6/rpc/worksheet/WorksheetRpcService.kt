package com.qxdzbc.p6.rpc.worksheet

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.mapError
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.action.range.IndRangeIdImp.Companion.toModel
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiRequest
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.app.common.utils.Utils.onNextAndComplete
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.proto.rpc.WorksheetServiceGrpc
import com.qxdzbc.p6.rpc.common_data_structure.BoolMsg.toBoolMsgProto
import com.qxdzbc.p6.rpc.cell.msg.CellPrt
import com.qxdzbc.p6.rpc.cell.msg.CellPrt.CO.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.IndeCellId.Companion.toIndeModel
import com.qxdzbc.p6.rpc.worksheet.msg.CheckContainAddressRequest.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdPrt.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.*
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.cell.action.UpdateCellAction
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import com.qxdzbc.p6.proto.DocProtos.WorksheetIdProto

class WorksheetRpcService @Inject constructor(
    @StateContainerSt
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    private val pasteAction: PasteRangeAction,
    private val updateCell: UpdateCellAction,
    private val deleteMultiCell: DeleteMultiCellAction,
) : WorksheetServiceGrpc.WorksheetServiceImplBase() {

    private val stateCont: StateContainer by stateContSt

    override fun getCell(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val cellId: IndeCellId = request.toIndeModel()
            val cellRs = stateCont.getCellRs(cellId.wbKey, cellId.wsName, cellId.address)
            val rt = SingleSignalResponse.fromRs(cellRs)
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getAllCell(
        request: WorksheetIdProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetAllCellResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wsId: WorksheetIdPrt = request.toModel()
            val ws: Worksheet? = stateCont.getWs(wsId)
            val cellAddressList: List<CellAddress> = (ws?.cells ?: emptyList()).map { it.address }
            responseObserver.onNextAndComplete(GetAllCellResponse(cellAddressList).toProto())
        }
    }

    override fun getCellCount(
        request: WorksheetIdProto?,
        responseObserver: StreamObserver<WorksheetProtos.CellCountResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wsId: WorksheetIdPrt = request.toModel()
            val count: Int = stateCont.getWs(wsId)?.size ?: 0
            responseObserver.onNextAndComplete(CellCountResponse(count.toLong()).toProto())
        }
    }


    override fun getUsedRangeAddress(
        request: WorksheetIdProto?,
        responseObserver: StreamObserver<WorksheetProtos.GetUsedRangeResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wsId: WorksheetIdPrt = request.toModel()
            val ra: RangeAddress? = stateCont.getWs(wsId)?.usedRange
            val res = GetUsedRangeResponse(ra)
            responseObserver.onNextAndComplete(res.toProto())
        }
    }


    override fun paste(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val cid: IndeCellId = request.toIndeModel()
            val o = pasteAction.pasteRange(cid, RangeAddress(cid.address))
            responseObserver.onNextAndComplete(SingleSignalResponse.fromRs(o).toProto())
        }
    }

    override fun addCell(
        request: DocProtos.CellProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val i: CellPrt = request.toModel()
            val o = updateCell.updateCell(
                CellUpdateRequest(
                    wbKey = i.id.wbKey,
                    wsName = i.id.wsName,
                    cellAddress = i.id.address,
                    formula = i.formula,
                    cellValue = if (i.cellValue.isBool) {
                        i.cellValue.bool
                    } else if (i.cellValue.isNumber) {
                        i.cellValue.number
                    } else if (i.cellValue.isStr) {
                        i.cellValue.str
                    } else {
                        null
                    }
                )
            )
            responseObserver.onNextAndComplete(SingleSignalResponse.fromRs(o).toProto())
        }
    }

    override fun deleteCell(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val i: IndeCellId = request.toIndeModel()
            val o =deleteMultiCell.deleteMultiCell(
                DeleteMultiRequest(
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

    override fun deleteRange(
        request: DocProtos.RangeIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val i = request.toModel()
            val o =deleteMultiCell.deleteMultiCell(
                DeleteMultiRequest(
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

    override fun containAddress(
        request: WorksheetProtos.CheckContainAddressRequestProto?,
        responseObserver: StreamObserver<CommonProtos.BoolMsgProto>?
    ) {
        if(request!=null && responseObserver!=null){
            val i: CheckContainAddressRequest = request.toModel()
            val o = stateCont.getWs(i.wsId)?.rangeConstraint?.contains(i.cellAddress) ?: false
            responseObserver.onNextAndComplete(o.toBoolMsgProto())
        }
    }
}
