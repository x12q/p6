package com.qxdzbc.p6.app.document.cell.address

import com.qxdzbc.p6.proto.DocProtos

/**
 * Standard implementation of CellAddress.
 * col and row index cannot go lower than 1, but has no upper bound
 */
data class CellAddressImp constructor(
    override val colCR: CR,
    override val rowCR: CR,
) : CellAddress {

    constructor(
        colIndex: Int,
        rowIndex: Int,
        isColFixed: Boolean = false,
        isRowFixed: Boolean = false
    ) : this(colCR = CR(colIndex, isColFixed), rowCR = CR(rowIndex, isRowFixed))

    override val colIndex: Int = colCR.n
    override val rowIndex: Int = rowCR.n
    override val isColFixed: Boolean = colCR.isFixed
    override val isRowFixed: Boolean = rowCR.isFixed

    override fun isSame(other: CellAddress): Boolean {
        return this.colIndex == other.colIndex && this.rowIndex == other.rowIndex
    }

    override fun equals(other: Any?): Boolean {
        if(other is CellAddress){
            return this.colIndex == other.colIndex && this.rowIndex == other.rowIndex && this.isColFixed == other.isColFixed && this.isRowFixed == other.isRowFixed
        }else{
            return false
        }
    }

    override fun shift(oldAnchorCell: CellAddress, newAnchorCell: CellAddress): CellAddress {
        val colDif = newAnchorCell.colIndex - oldAnchorCell.colIndex
        val rowDif = newAnchorCell.rowIndex - oldAnchorCell.rowIndex
        val colIndex = this.colIndex + colDif
        val rowIndex = this.rowIndex + rowDif
        return this.copy(
           colCR = colCR.copy(n=colIndex),
            rowCR = rowCR.copy(n=rowIndex)
        )
    }

    override fun increaseRowBy(v: Int): CellAddress {
        if (this.rowIndex == Int.MAX_VALUE && v > 0) {
            return this
        } else if (this.rowIndex == Int.MIN_VALUE && v < 0) {
            return this
        } else {
            val newRow = maxOf(this.rowIndex + v, 1)
            return this.copy(rowCR=rowCR.copy(n=newRow))
        }
    }

    override fun increaseColBy(v: Int): CellAddress {
        if (this.colIndex == Int.MAX_VALUE && v > 0) {
            return this
        } else if (this.colIndex == Int.MIN_VALUE && v < 0) {
            return this
        } else {
            val newCol = maxOf(this.colIndex + v, 1)
            return this.copy(colCR = colCR.copy(n=newCol))
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
        var result = colIndex
        result = 31 * result + rowIndex
        result = 31 * result + isColFixed.hashCode()
        result = 31 * result + isRowFixed.hashCode()
        return result
    }
}
