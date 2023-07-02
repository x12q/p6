package com.qxdzbc.p6.app.document.range.address

import com.qxdzbc.common.IntRangeUtils.dif
import com.qxdzbc.p6.app.common.utils.CellLabelNumberSystem
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraint


data class RangeAddressImp(override val topLeft: CellAddress, override val botRight: CellAddress) :
    BaseRangeAddress() {

    init {
        if (topLeft.rowIndex > botRight.rowIndex) {
            throw IllegalArgumentException("first address row must be <= last address row")
        }
        if (topLeft.colIndex > botRight.colIndex) {
            throw IllegalArgumentException("first address col must be <= last address col")
        }
    }
    companion object{
        fun getCycleInt(i: IntRange, num: Int): Int {
            if (num in i) {
                return num
            } else {
                val gap = i.dif() + 1
                if (num > i.last) {
                    val mod = (num-i.last) % gap
                    if(mod == 0){
                        return i.last
                    }else{
                        return i.first+mod-1
                    }
                }
                if (num < i.first) {
                    val mod = (num-i.first) % gap
                    if(mod == 0){
                        return i.first
                    }else{
                        return i.last+mod+1
                    }
                }
                // this is impossible
                return num
            }
        }
    }
    override fun getCellAddressCycle(cellAddress: CellAddress): CellAddress {
        val r = this
        if (r.contains(cellAddress)) {
            return cellAddress
        } else {
            val newCol = getCycleInt(r.colRange, cellAddress.colIndex)
            val newRow = getCycleInt(r.rowRange, cellAddress.rowIndex)
            return CellAddress(newCol,newRow)
        }
    }

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): RangeAddress {
        val tl=this.topLeft.shift(oldAnchorCell,newAnchorCell)
        val br = this.botRight.shift(oldAnchorCell, newAnchorCell)
        return this.copy(
            topLeft = tl, botRight = br
        )
    }

    override val colRange: IntRange = topLeft.colIndex..botRight.colIndex
    override val rowRange: IntRange = topLeft.rowIndex..botRight.rowIndex

    override fun nextLockState(): RangeAddress {
        return this.copy(
            topLeft = topLeft.nextLockState(),
            botRight = botRight.nextLockState()
        )
    }

    override val cellIterator: Iterator<CellAddress>
        get() {
            return object : Iterator<CellAddress> {
                var col = topLeft.colIndex
                var row = topLeft.rowIndex
                val maxCol = botRight.colIndex
                val maxRow = botRight.rowIndex

                override fun hasNext(): Boolean {
                    return col <= maxCol && row <= maxRow
                }

                override fun next(): CellAddress {
                    val rt = if (this.hasNext()) {
                        CellAddress(col, row)
                    } else {
                        throw IllegalStateException("iterator done")
                    }

                    val nr = if (row == maxRow && col + 1 <= maxCol) {
                        topLeft.rowIndex
                    } else {
                        row + 1
                    }

                    val nc = if (nr == topLeft.rowIndex) {
                        col + 1
                    } else {
                        col
                    }

                    col = nc
                    row = nr
                    return rt
                }
            }
        }

    override val label: String get() = rawLabel

    override val rawLabel: String
        get() {
            val firstAddressOnFirstRow = topLeft.rowIndex == 1
            val lastAddressOnLastRow = botRight.rowIndex == P6R.worksheetValue.rowLimit
            if (firstAddressOnFirstRow && lastAddressOnLastRow) {
                val firstColLabel = CellLabelNumberSystem.numberToLabel(topLeft.colIndex)
                val lastColLabel = CellLabelNumberSystem.numberToLabel(botRight.colIndex)
                return "${firstColLabel}:${lastColLabel}"
            }
            val firstAddressOnFirstCol = topLeft.colIndex == 1
            val lastAddressOnLastCol = botRight.colIndex == P6R.worksheetValue.colLimit
            if (firstAddressOnFirstCol && lastAddressOnLastCol) {
                return "${topLeft.rowIndex}:${botRight.rowIndex}"
            }
            return "${topLeft.label}:${botRight.label}"
        }

    override operator fun contains(cellAddress: CellAddress): Boolean {
        val rowOk = cellAddress.rowIndex >= topLeft.rowIndex && cellAddress.rowIndex <= botRight.rowIndex
        val colOk = cellAddress.colIndex >= topLeft.colIndex && cellAddress.colIndex <= botRight.colIndex
        return rowOk && colOk
    }

    override fun contains(rangeConstraint: RangeConstraint): Boolean {
        val c = rangeConstraint.colRange.first in this.colRange && rangeConstraint.colRange.last in this.colRange
        val r = rangeConstraint.rowRange.first in this.rowRange && rangeConstraint.rowRange.last in this.rowRange
        return c && r
    }

    override val topRight: CellAddress =
        CellAddresses.fromIndices(colIndex = botRight.colIndex, rowIndex = topLeft.rowIndex)
    override val botLeft: CellAddress =
        CellAddresses.fromIndices(colIndex = topLeft.colIndex, rowIndex = botRight.rowIndex)

    override fun setTopLeft(i: CellAddress): RangeAddress {
        return this.copy(topLeft = i)
    }

    override fun setBotRight(i: CellAddress): RangeAddress {
        return this.copy(botRight = i)
    }

    override fun lock(): RangeAddress {
        return this.copy(
            topLeft = topLeft.lock(),
            botRight = botRight.lock()
        )
    }

    override fun unLock(): RangeAddress {
        return this.copy(
            topLeft = topLeft.unlock(),
            botRight = botRight.unlock()
        )
    }

    override fun removeCell(cellAddress: CellAddress): List<RangeAddress> {
        if (cellAddress in this) {
            val rt = mutableListOf<RangeAddress>()
            // 1st segment
            val _1stTopleft = this.topLeft
            val _1stBotRight = CellAddress(this.botRight.colIndex, cellAddress.rowIndex - 1)

            if (_1stBotRight.isValid()
                && _1stBotRight in this
                && _1stTopleft != cellAddress && _1stBotRight != cellAddress
            ) {
                rt.add(RangeAddress(_1stTopleft, _1stBotRight))
            }

            // 2nd segment
            val _2ndTopleft = CellAddress(this.topLeft.colIndex, cellAddress.rowIndex + 1)
            val _2ndBotRight = this.botRight
            if (_2ndTopleft.isValid()
                && _2ndTopleft in this
                && _2ndBotRight != cellAddress && _2ndTopleft != cellAddress
            ) {
                rt.add(RangeAddress(_2ndTopleft, _2ndBotRight))
            }

            //3rd segment
            val _3rdTopLeft = CellAddress(this.topLeft.colIndex, cellAddress.rowIndex)
            val _3rdBotRight = cellAddress.leftOneCol()
            if (_3rdTopLeft != cellAddress
                && _3rdTopLeft in this && _3rdBotRight in this
            ) {
                rt.add(RangeAddress(_3rdTopLeft, _3rdBotRight))
            }

            //4th segment
            val _4thTopLeft = cellAddress.rightOneCol()
            val _4thBotRight = CellAddress(this.botRight.colIndex, cellAddress.rowIndex)
            if (
                _4thBotRight != cellAddress &&
                _4thTopLeft in this && _4thBotRight in this
            ) {
                rt.add(RangeAddress(_4thBotRight, _4thTopLeft))
            }
            return rt
        } else {
            return listOf(this)
        }
    }

    override fun mergeWith(anotherRangeAddress: RangeAddress): RangeAddress {
        return RangeAddressUtils.rangeForMultiCells(
            listOf(
                this.topLeft,
                this.botRight,
                anotherRangeAddress.topLeft,
                anotherRangeAddress.botRight
            )
        )
    }

    override fun expand(cellAddress: CellAddress): RangeAddress {
        return RangeAddressUtils.rangeForMultiCells(listOf(this.topLeft, this.botRight, cellAddress))
    }

    override fun strictMerge(cellAddress: CellAddress): RangeAddress? {
        if (this.contains(cellAddress)) {
            return this
        }
        if (!this.isRow() && !this.isCol()) {
            return null
        } else {
            if (this.isCol()) {
                val top = topLeft.upOneRow()
                if (cellAddress == top) {
                    return this.copy(
                        topLeft = cellAddress
                    )
                }
                val bot = botLeft.downOneRow()
                if (cellAddress == bot) {
                    return this.copy(
                        botRight = cellAddress
                    )
                }
            }
            if (this.isRow()) {
                if (cellAddress == topLeft.leftOneCol()) {
                    return this.copy(topLeft = cellAddress)
                }
                if (cellAddress == botRight.rightOneCol()) {
                    return this.copy(botRight = cellAddress)
                }
            }
            return null
        }
    }

    override fun strictMerge(anotherRange: RangeAddress): RangeAddress? {
        if (this.contains(anotherRange)) {
            return this
        }
        if (this.colRange == anotherRange.colRange) {
            if (anotherRange.contains(this.botLeft.downOneRow())
                || anotherRange.contains(this.topLeft.upOneRow())
            ) {
                return this.mergeWith(anotherRange)
            }
            return null
        }
        if (this.rowRange == anotherRange.rowRange) {
            if (anotherRange.contains(this.topLeft.leftOneCol())
                || anotherRange.contains(this.topRight.rightOneCol())
            ) {
                return this.mergeWith(anotherRange)
            }
            return null
        }
        return null
    }

    override fun toProto(): DocProtos.RangeAddressProto {
        return DocProtos.RangeAddressProto.newBuilder()
            .setTopLeft(this.topLeft.toProto())
            .setBotRight(this.botRight.toProto())
            .build()
    }

    override fun toString(): String {
        return this.label
    }
}
