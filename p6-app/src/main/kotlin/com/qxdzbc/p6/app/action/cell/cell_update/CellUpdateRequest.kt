package com.qxdzbc.p6.app.action.cell.cell_update

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.CellProtos
import com.google.protobuf.ByteString

/**
 *
 */
@Deprecated("dont use", ReplaceWith("com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest2"))
data class CellUpdateRequest(
    override val wbKey: WorkbookKey,
    val wsName: String,
    val cellAddress: CellAddress,
    val formula: String?=null,
    val cellValue:Any?=null,
) : RequestToP6WithWorkbookKey {
//    fun toProto():CellProtos.CellUpdateRequestProto{
//        val rt = CellProtos.CellUpdateRequestProto.newBuilder()
//            .setCellId()
//            .setCellAddress(cellAddress.toProto())
//            .apply{
//                if(this@CellUpdateRequest.formula!=null){
//                    setFormula(this@CellUpdateRequest.formula)
//                }
//            }
//            .build()
//        return rt
//    }
}
