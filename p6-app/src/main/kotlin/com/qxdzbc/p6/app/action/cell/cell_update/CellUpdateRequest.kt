package com.qxdzbc.p6.app.action.cell.cell_update

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.CellProtos
import com.google.protobuf.ByteString

data class CellUpdateRequest(
    override val wbKey: WorkbookKey,
    val wsName: String,
    val cellAddress: CellAddress,
    val formula: String?=null,
    val cellValue:Any?=null,
) : RequestToP6WithWorkbookKey {
    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
    fun toProto():CellProtos.CellUpdateRequestProto{
        val rt = CellProtos.CellUpdateRequestProto.newBuilder()
            .setWorkbookKey(wbKey.toProto())
            .setWorksheetName(wsName)
            .setCellAddress(cellAddress.toProto())
            .apply{
                if(this@CellUpdateRequest.formula!=null){
                    setFormula(this@CellUpdateRequest.formula)
                }
            }
            .build()
        return rt
    }
}
