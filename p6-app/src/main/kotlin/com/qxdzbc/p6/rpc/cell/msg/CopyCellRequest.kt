package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.p6.proto.CellProtos.CopyCellRequestProto
import com.qxdzbc.p6.rpc.worksheet.msg.CellIdProtoDM
import com.qxdzbc.p6.rpc.worksheet.msg.CellIdProtoDM.Companion.toModel

class CopyCellRequest(
    val fromCell: CellIdProtoDM,
    val toCell: CellIdProtoDM,
) {
    companion object{
        fun CopyCellRequestProto.toModel(): CopyCellRequest {
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

