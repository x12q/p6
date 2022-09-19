package com.qxdzbc.p6.app.action.worksheet.delete_multi

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.WorksheetProtos.DeleteMultiRequestProto
import com.qxdzbc.p6.app.common.err.WithReportNavInfo

/**
 * Request for deleting multiple cells and ranges at the same time
 */
class RemoveMultiCellRequest(
    val ranges: List<RangeAddress>,
    val cells: List<CellAddress>,
    override val wbKey: WorkbookKey,
    val wsName: String,
    val clearFormat:Boolean = false,
    override val windowId:String? = null,
) : RequestToP6WithWorkbookKey, WithReportNavInfo {
//    override fun toProtoBytes(): ByteString {
//        return this.toProto().toByteString()
//    }
    fun toProto(): DeleteMultiRequestProto {
        return DeleteMultiRequestProto.newBuilder()
            .addAllRange(ranges.map { it.toProto() })
            .addAllCell(cells.map { it.toProto() })
            .setWorkbookKey(this.wbKey.toProto())
            .setWorksheetName(this.wsName)
            .build()
    }
}
