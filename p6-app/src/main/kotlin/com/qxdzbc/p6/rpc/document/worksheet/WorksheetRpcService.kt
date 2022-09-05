package com.qxdzbc.p6.rpc.document.worksheet

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.mapError
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.action.range.RangeIdImp.Companion.toModel
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiRequest
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.app.common.utils.Utils.onNextAndComplete
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.CommonProtos.BoolMsgProto
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.proto.rpc.worksheet.WorksheetServiceProtos
import com.qxdzbc.p6.proto.rpc.worksheet.service.WorksheetServiceGrpc
import com.qxdzbc.p6.rpc.document.cell.msg.Cell2Prt
import com.qxdzbc.p6.rpc.document.cell.msg.Cell2Prt.CO.toModel
import com.qxdzbc.p6.rpc.document.worksheet.msg.*
import com.qxdzbc.p6.rpc.document.worksheet.msg.CellId.Companion.toModel
import com.qxdzbc.p6.rpc.document.worksheet.msg.CheckContainAddressRequest.Companion.toModel
import com.qxdzbc.p6.rpc.document.worksheet.msg.WorksheetIdPrt.Companion.toModel
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.cell.action.UpdateCellAction
import io.grpc.stub.StreamObserver
import javax.inject.Inject

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
            val cellId: CellId = request.toModel()
            val cellRs = stateCont.getCellRs(cellId.wbKey, cellId.wsName, cellId.address)
            val rt = SingleSignalResponse.fromRs(cellRs)
            responseObserver.onNextAndComplete(rt.toProto())
        } else {
            super.getCell(request, responseObserver)
        }
    }

    override fun getAllCell(
        request: WorksheetProtos.WorksheetIdProto?,
        responseObserver: StreamObserver<WorksheetServiceProtos.GetAllCellResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wsId: WorksheetIdPrt = request.toModel()
            val ws: Worksheet? = stateCont.getWs(wsId)
            val cellAddressList: List<CellAddress> = (ws?.cells ?: emptyList()).map { it.address }
            responseObserver.onNextAndComplete(GetAllCellResponse(cellAddressList).toProto())
        } else {
            super.getAllCell(request, responseObserver)
        }
    }

    override fun getCellCount(
        request: WorksheetProtos.WorksheetIdProto?,
        responseObserver: StreamObserver<WorksheetServiceProtos.CellCountResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wsId: WorksheetIdPrt = request.toModel()
            val count: Int = stateCont.getWs(wsId)?.size ?: 0
            responseObserver.onNextAndComplete(CellCountResponse(count.toLong()).toProto())
        } else {
            super.getCellCount(request, responseObserver)
        }
    }


    override fun getUsedRangeAddress(
        request: WorksheetProtos.WorksheetIdProto?,
        responseObserver: StreamObserver<WorksheetServiceProtos.GetUsedRangeResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val wsId: WorksheetIdPrt = request.toModel()
            val ra: RangeAddress? = stateCont.getWs(wsId)?.usedRange
            val res = GetUsedRangeResponse(ra)
            responseObserver.onNextAndComplete(res.toProto())
        } else {
            super.getUsedRangeAddress(request, responseObserver)
        }
    }


    override fun paste(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val cid: CellId = request.toModel()
            val o = pasteAction.pasteRange(cid, RangeAddress(cid.address))
            responseObserver.onNextAndComplete(SingleSignalResponse.fromRs(o).toProto())
        } else {
            super.paste(request, responseObserver)
        }
    }

    override fun addCell(
        request: DocProtos.Cell2Proto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val i: Cell2Prt = request.toModel()
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
        } else {
            super.addCell(request, responseObserver)
        }
    }

    override fun deleteCell(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if (request != null && responseObserver != null) {
            val i:CellId = request.toModel()
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
        else{
            super.deleteCell(request, responseObserver)
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
        }else{
            super.deleteRange(request, responseObserver)
        }
    }

    override fun containAddress(
        request: WorksheetServiceProtos.CheckContainAddressRequestProto?,
        responseObserver: StreamObserver<CommonProtos.BoolMsgProto>?
    ) {
        if(request!=null && responseObserver!=null){
            val i:CheckContainAddressRequest = request.toModel()
            val o = stateCont.getWs(i.wsId)?.rangeConstraint?.contains(i.cellAddress) ?: false
            responseObserver.onNextAndComplete(BoolMsgProto.newBuilder().setV(o).build())
        }else{
            super.containAddress(request, responseObserver)
        }
    }
}
