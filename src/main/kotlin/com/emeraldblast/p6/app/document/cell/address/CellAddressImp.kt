package com.emeraldblast.p6.app.document.cell.address

import com.emeraldblast.p6.proto.DocProtos

/**
 * Standard implementation of CellAddress.
 * col and row index cannot go lower than 1, but has no upper bound
 */
data class CellAddressImp constructor(
    override val colIndex: Int,
    override val rowIndex: Int,
    override val isColFixed: Boolean = false,
    override val isRowFixed: Boolean = false
) : CellAddress {
    override fun equals(other: Any?): Boolean {
        if (other is CellAddress) {
            return this.colIndex == other.colIndex && this.rowIndex == other.rowIndex
        } else {
            return false
        }
    }

    override fun shift(oldAnchorCell: CellAddress, newAnchorCell: CellAddress): CellAddress {
        val colDif = newAnchorCell.colIndex - oldAnchorCell.colIndex
        val rowDif = newAnchorCell.rowIndex - oldAnchorCell.rowIndex
        return this.copy(
            colIndex = this.colIndex + colDif,
            rowIndex = this.rowIndex + rowDif
        )
    }

    override fun increaseRowBy(v: Int): CellAddress {
        if (this.rowIndex == Int.MAX_VALUE && v > 0) {
            return this
        } else if (this.rowIndex == Int.MIN_VALUE && v < 0) {
            return this
        } else {
            val newRow = maxOf(this.rowIndex + v, 1)
            return this.copy(rowIndex = newRow)
        }
    }

    override fun increaseColBy(v: Int): CellAddress {
        if (this.colIndex == Int.MAX_VALUE && v > 0) {
            return this
        } else if (this.colIndex == Int.MIN_VALUE && v < 0) {
            return this
        } else {
            val newCol = maxOf(this.colIndex + v, 1)
            return this.copy(colIndex = newCol)
        }
    }

    override fun toProto(): DocProtos.CellAddressProto {
        return DocProtos.CellAddressProto.newBuilder()
            .setCol(this.colIndex)
            .setRow(this.rowIndex)
            .build()
    }

    override fun toString(): String {
        return this.toRawLabel()
    }

    override fun hashCode(): Int {
        var result = colIndex.hashCode()
        result = 31 * result + rowIndex.hashCode()
        return result
    }
}
