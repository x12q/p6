package com.emeraldblast.p6.app.action.cell.cell_multi_update

import com.emeraldblast.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.proto.CellProtos.*
import com.google.protobuf.ByteString

data class CellUpdateContent(
    val formula: String,
    val displayValue: String,
    val cellValue:Any? = null
) {
    fun toProto(): CellUpdateContentProto {
        return CellUpdateContentProto.newBuilder()
            .setFormula(formula)
            .setLiteral(displayValue)
            .build()
    }
}

data class CellUpdateEntry(
    val cellAddress: CellAddress,
    val cellUpdateContent: CellUpdateContent
) {
    fun toProto(): CellUpdateEntryProto {
        return CellUpdateEntryProto.newBuilder()
            .setCellAddress(this.cellAddress.toProto())
            .setContent(this.cellUpdateContent.toProto())
            .build()
    }
}

data class CellMultiUpdateRequest(
    override val wbKey: WorkbookKey,
    val wsName: String,
    val cellUpdateList: List<CellUpdateEntry>
) : RequestToP6WithWorkbookKey {
    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
    fun toProto(): CellMultiUpdateRequestProto {
        return CellMultiUpdateRequestProto.newBuilder()
            .setWorkbookKey(wbKey.toProto())
            .setWorksheetName(wsName)
            .addAllCellUpdate(cellUpdateList.map { it.toProto() })
            .build()
    }
}
