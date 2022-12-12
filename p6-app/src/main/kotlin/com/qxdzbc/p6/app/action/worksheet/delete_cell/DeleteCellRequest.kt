package com.qxdzbc.p6.app.action.worksheet.delete_cell

import com.qxdzbc.p6.app.communication.res_req_template.request.RequestWithWorkbookKey
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.app.action.common_data_structure.WbWs

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
