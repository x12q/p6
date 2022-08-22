package com.qxdzbc.p6.app.document.range

import com.qxdzbc.p6.app.common.table.ImmutableTableCR
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.action.range.RangeId.Companion.toModel
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.cell.d.CellImp.Companion.toModel
import com.qxdzbc.p6.proto.RangeProtos.RangeCopyProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

/**
 * contain a [RangeId] and a list of [Cell] in that range
 */
class RangeCopy(
    val rangeId: RangeId,
    val cells: List<Cell>
) {
    companion object {
        fun RangeCopyProto.toModel(translator: P6Translator<ExUnit>): RangeCopy {
            return RangeCopy(
                rangeId = this.id.toModel(),
                cells = this.cellList.map { it.toModel(translator) }
            )
        }

        fun fromProtoBytes(data: ByteArray, translator: P6Translator<ExUnit>): RangeCopy {
            return RangeCopyProto.newBuilder().mergeFrom(data).build().toModel(translator)
        }
    }

    val cellTable = ImmutableTableCR(cells.groupBy { it.address.colIndex }.map { (col, cellList) ->
        col to cellList.associateBy { it.address.rowIndex }
    }.toMap())

    fun toProto(): RangeCopyProto {
        return RangeCopyProto.newBuilder()
            .setId(this.rangeId.toProto())
            .addAllCell(this.cells.map { it.toProto() })
            .build()
    }
}
