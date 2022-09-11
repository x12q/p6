package com.qxdzbc.p6.rpc.document.cell.msg

import com.qxdzbc.p6.proto.CellProtos.CopyCellRequestProto
import com.qxdzbc.p6.rpc.document.worksheet.msg.CellId
import com.qxdzbc.p6.rpc.document.worksheet.msg.CellId.Companion.toModel

class CopyCellRequest(
    val fromCell:CellId,
    val toCell: CellId,
) {
    companion object{
        fun CopyCellRequestProto.toModel():CopyCellRequest{
            return CopyCellRequest(
                fromCell = this.fromCell.toModel(),
                toCell = this.toCell.toModel()
            )
        }
    }
    fun toProto(): CopyCellRequestProto {
        return CopyCellRequestProto.newBuilder()
            .setFromCell(fromCell.toProto())
            .setToCell(toCell.toProto())
            .build()
    }
}

