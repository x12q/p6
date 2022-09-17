package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.p6.proto.CellProtos.CopyCellRequestProto
import com.qxdzbc.p6.rpc.worksheet.msg.IndeCellId
import com.qxdzbc.p6.rpc.worksheet.msg.IndeCellId.Companion.toIndeModel

class CopyCellRequest(
    val fromCell: IndeCellId,
    val toCell: IndeCellId,
) {
    companion object{
        fun CopyCellRequestProto.toModel(): CopyCellRequest {
            return CopyCellRequest(
                fromCell = this.fromCell.toIndeModel(),
                toCell = this.toCell.toIndeModel()
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

