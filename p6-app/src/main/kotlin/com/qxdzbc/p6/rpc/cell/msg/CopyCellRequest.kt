package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.p6.proto.CellProtos.CopyCellRequestProto
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM.Companion.toModel

/**
 * A request to copy data and format from [fromCell] to [toCell].
 * @param shiftRange shift/or not the ranges inside original cell's formula when pasting its content to the new cell.
 * @param undoable make the action carrying out this request undoable or not.
 */
class CopyCellRequest(
    val fromCell: CellIdDM,
    val toCell: CellIdDM,
    val shiftRange:Boolean = true,
    val undoable:Boolean = true,
) {
    companion object{
        fun CopyCellRequestProto.toModel(): CopyCellRequest {
            return CopyCellRequest(
                fromCell = this.fromCell.toModel(),
                toCell = this.toCell.toModel(),
                shiftRange = this.shiftRange,
                undoable = this.undoable,
            )
        }
    }
    fun toProto(): CopyCellRequestProto {
        return CopyCellRequestProto.newBuilder()
            .setFromCell(fromCell.toProto())
            .setToCell(toCell.toProto())
            .setShiftRange(shiftRange)
            .setUndoable(undoable)
            .build()
    }
}

