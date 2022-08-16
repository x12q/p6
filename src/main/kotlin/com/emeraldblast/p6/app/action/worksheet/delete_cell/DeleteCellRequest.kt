package com.emeraldblast.p6.app.action.worksheet.delete_cell

import com.emeraldblast.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.proto.WorksheetProtos
import com.google.protobuf.ByteString

class DeleteCellRequest(
    override val wbKey: WorkbookKey,
    val wsName:String,
    val cellAddress: CellAddress,
) : RequestToP6WithWorkbookKey {
    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
    fun toProto(): WorksheetProtos.DeleteCellRequestProto {
        return WorksheetProtos.DeleteCellRequestProto.newBuilder()
            .setWorkbookKey(this.wbKey.toProto())
            .setWorksheetName(this.wsName)
            .setCellAddress(this.cellAddress.toProto())
            .build()
    }
}
