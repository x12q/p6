package com.qxdzbc.p6.composite_actions.worksheet.delete_cell

import com.qxdzbc.p6.rpc.communication.res_req_template.request.RequestWithWorkbookKey
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs

class DeleteCellRequest(
    override val wbKey: WorkbookKey,
    override val wsName:String,
    val cellAddress: CellAddress,
) : RequestWithWorkbookKey,WbWs {
    fun toProto(): WorksheetProtos.DeleteCellRequestProto {
        return WorksheetProtos.DeleteCellRequestProto.newBuilder()
            .setWorkbookKey(this.wbKey.toProto())
            .setWorksheetName(this.wsName)
            .setCellAddress(this.cellAddress.toProto())
            .build()
    }
}
