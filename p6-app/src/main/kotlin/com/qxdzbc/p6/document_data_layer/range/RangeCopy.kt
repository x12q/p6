package com.qxdzbc.p6.document_data_layer.range

import com.qxdzbc.p6.composite_actions.range.RangeId
import com.qxdzbc.p6.common.table.ImmutableTableCR
import com.qxdzbc.p6.composite_actions.range.RangeIdDM.Companion.toModel
import com.qxdzbc.p6.document_data_layer.cell.Cell
import com.qxdzbc.p6.document_data_layer.cell.CellImp.Companion.toShallowModel
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.proto.RangeProtos.RangeCopyProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

/**
 * contain a [RangeId] and a list of [Cell] in that range
 */
data class RangeCopy(
    val rangeId: RangeId,
    val cells: List<com.qxdzbc.p6.document_data_layer.cell.Cell>
) {
    companion object {
        fun RangeCopyProto.toModel(translator: P6Translator<ExUnit>): RangeCopy {
            return RangeCopy(
                rangeId = this.id.toModel(),
                cells = this.cellList.map { it.toShallowModel(translator) }
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

    /**
     * shift all the cell in this object using vector: topLeft of this range -> [newAnchorCell].
     * [rangeId] is preserved.
     */
    fun shiftCells(newAnchorCell:CellAddress): RangeCopy {
        val sourceTopLeft: CellAddress = this.rangeId.rangeAddress.topLeft
        val newCells=this.cells.map{
            it.shift(sourceTopLeft,newAnchorCell)
        }
        return this.copy(cells = newCells)
    }

}
