package com.qxdzbc.p6.rpc.cell

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.common.utils.Utils.onNextAndComplete
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.cell.d.CellContent
import com.qxdzbc.p6.app.document.cell.d.CellContentImp
import com.qxdzbc.p6.app.document.cell.d.CellValue
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.proto.CellProtos
import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.rpc.CellServiceGrpc
import com.qxdzbc.p6.rpc.common_data_structure.StrMsg
import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.CellId
import com.qxdzbc.p6.rpc.worksheet.msg.CellId.Companion.toModel
import com.qxdzbc.p6.ui.app.state.StateContainer
import io.grpc.stub.StreamObserver
import javax.inject.Inject


class CellRpcService @Inject constructor(
    @StateContainerSt
    val stateContSt:St<@JvmSuppressWildcards StateContainer>,
    val copyCell: CopyCellAction,
) : CellServiceGrpc.CellServiceImplBase() {

    private val stateCont by stateContSt

    override fun getDisplayValue(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.StrMsgProto>?
    ) {
        if(request != null && responseObserver!=null){
            val cid: CellId = request.toModel()
            val cell: Cell? = stateCont.getCell(cid)
            val rt = StrMsg(cell?.displayValue ?:"")
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getFormula(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CommonProtos.StrMsgProto>?
    ) {
        if(request != null && responseObserver!=null){
            val cid: CellId = request.toModel()
            val cell: Cell? = stateCont.getCell(cid)
            val rt = StrMsg(cell?.formula ?:"")
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getCellValue(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<DocProtos.CellValueProto>?
    ) {
        if(request != null && responseObserver!=null){
            val cid: CellId = request.toModel()
            val cell: Cell? = stateCont.getCell(cid)
            val rt:CellValue = cell?.currentCellValue ?: CellValue.empty
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun getCellContent(
        request: DocProtos.CellIdProto?,
        responseObserver: StreamObserver<CellProtos.CellContentProto>?
    ) {
        if(request != null && responseObserver!=null){
            val cid: CellId = request.toModel()
            val cell: Cell? = stateCont.getCell(cid)
            val rt:CellContent = cell?.content ?: CellContentImp.empty
            responseObserver.onNextAndComplete(rt.toProto())
        }
    }

    override fun copyFrom(
        request: CellProtos.CopyCellRequestProto?,
        responseObserver: StreamObserver<CommonProtos.SingleSignalResponseProto>?
    ) {
        if(request != null && responseObserver!=null){
            val req = request.toModel()
            val rt = copyCell.copyCell(req)
            responseObserver.onNextAndComplete(SingleSignalResponse.fromRs(rt).toProto())
        }
    }
}


