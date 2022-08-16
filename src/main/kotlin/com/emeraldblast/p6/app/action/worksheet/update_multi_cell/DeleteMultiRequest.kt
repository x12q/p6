package com.emeraldblast.p6.app.action.worksheet.update_multi_cell

import com.emeraldblast.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.proto.WorksheetProtos.DeleteMultiRequestProto
import com.google.protobuf.ByteString

/**
 * Request for deleting multiple cells and ranges at the same time
 */
class DeleteMultiRequest(
    val ranges: List<RangeAddress>,
    val cells: List<CellAddress>,
    override val wbKey: WorkbookKey,
    val wsName: String
) : RequestToP6WithWorkbookKey {
    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
    fun toProto(): DeleteMultiRequestProto {
        return DeleteMultiRequestProto.newBuilder()
            .addAllRange(ranges.map { it.toProto() })
            .addAllCell(cells.map { it.toProto() })
            .setWorkbookKey(this.wbKey.toProto())
            .setWorksheetName(this.wsName)
            .build()
    }
}
