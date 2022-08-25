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
    override val isColLocked: Boolean = colCR.isLocked
    override fun setLockCol(i: Boolean): CellAddress {
        val ccr = colCR.copy(isLocked = i)
        return this.copy(colCR = ccr)
    }

    override fun unlockCol(): CellAddress {
        return this.setLockCol(false)
    }

    override fun lockCol(): CellAddress {
        return this.setLockCol(true)
    }

    override val isRowLocked: Boolean = rowCR.isLocked
    override fun setLockRow(i: Boolean): CellAddress {
        val rcr = rowCR.copy(isLocked = i)
        return this.copy(rowCR = rcr)
    }

    override fun unlockRow(): CellAddress {
        return this.setLockRow(false)
    }

    override fun lockRow(): CellAddress {
        return this.setLockRow(true)
    }

    override fun lock(): CellAddress {
        return this.copy(
            colCR = colCR.lock(),
            rowCR = rowCR.lock()
        )
    }

    override fun unlock(): CellAddress {
        return this.copy(
            colCR = colCR.unlock(),
            rowCR = rowCR.unlock()
        )
    }

    override fun isSame(other: CellAddress): Boolean {
        return this.colIndex == other.colIndex && this.rowIndex == other.rowIndex
    }

    override fun equals(other: Any?): Boolean {
        if(other is CellAddress){
            return this.colIndex == other.colIndex && this.rowIndex == other.rowIndex && this.isColLocked == other.isColLocked && this.isRowLocked == other.isRowLocked
        }else{
            return false
        }
    }

    override fun shift(oldAnchorCell: GenericCellAddress<Int, Int>, newAnchorCell: GenericCellAddress<Int, Int>): CellAddress {
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
        result = 31 * result + isColLocked.hashCode()
        result = 31 * result + isRowLocked.hashCode()
        return result
    }
}
