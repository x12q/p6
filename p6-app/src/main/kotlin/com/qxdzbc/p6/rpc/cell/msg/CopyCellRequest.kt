package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.p6.proto.CellProtos.CopyCellRequestProto
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM.Companion.toModel

/**
 * @param shiftRange shift the ranges inside original cell's formula when pasting its content to the new cell or not
 */
class CopyCellRequest(
    val fromCell: CellIdDM,
    val toCell: CellIdDM,
    val shiftRange:Boolean = true,
) {
    companion object{
        fun CopyCellRequestProto.toModel(): CopyCellRequest {
            return CopyCellRequest(
                fromCell = this.fromCell.toModel(),
                toCell = this.toCell.toModel(),
                shiftRange = this.shiftRange,
            )
        }
    }
    fun toProto(): CopyCellRequestProto {
        return CopyCellRequestProto.newBuilder()
            .setFromCell(fromCell.toProto())
            .setToCell(toCell.toProto())
            .setShiftRange(shiftRange)
            .build()
    }
}

