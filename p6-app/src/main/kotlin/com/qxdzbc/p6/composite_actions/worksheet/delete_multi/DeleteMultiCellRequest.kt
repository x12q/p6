package com.qxdzbc.p6.composite_actions.worksheet.delete_multi

import com.qxdzbc.p6.rpc.communication.res_req_template.request.RequestWithWorkbookKey
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.proto.WorksheetProtos.DeleteMultiRequestProto

/**
 * Request for deleting multiple cells and ranges at the same time
 */
data class DeleteMultiCellRequest(
    val ranges: List<RangeAddress> = emptyList(),
    val cells: List<CellAddress> = emptyList(),
    override val wbKey: WorkbookKey,
    val wsName: String,
    val clearFormat:Boolean = false,
    override val windowId:String? = null,
) : RequestWithWorkbookKey, com.qxdzbc.p6.common.err.WithReportNavInfo {
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
