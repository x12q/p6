package com.emeraldblast.p6.app.document.range.address

import com.emeraldblast.p6.app.common.utils.dif
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.range.RangeCopy

abstract class BaseRangeAddress : RangeAddress {
    override fun intersect(otherRangeAddress: RangeAddress): RangeAddress? {
        if (this.isValid() && otherRangeAddress.isValid()) {
            val intersectionExist =
                this.contains(otherRangeAddress.topLeft)
                        || this.contains(otherRangeAddress.topRight)
                        || this.contains(otherRangeAddress.botLeft)
                        || this.contains(otherRangeAddress.botRight)
                        || otherRangeAddress.contains(this.topLeft)
                        || otherRangeAddress.contains(this.topRight)
                        || otherRangeAddress.contains(this.botLeft)
                        || otherRangeAddress.contains(this.botRight)
            if (intersectionExist) {
                val firstCol = maxOf(this.colRange.first, otherRangeAddress.colRange.first)
                val lastCol = minOf(this.colRange.last, otherRangeAddress.colRange.last)
                val firstRow = maxOf(this.rowRange.first, otherRangeAddress.rowRange.first)
                val lastRow = minOf(this.rowRange.last, otherRangeAddress.rowRange.last)
                return RangeAddress(
                    colRange = firstCol..lastCol,
                    rowRange = firstRow..lastRow
                )
            } else {
                return null
            }
        } else {
            return null
        }
    }

    override fun isValid(): Boolean {
        return this.topLeft.isValid() && this.botRight.isValid()
    }

    override fun isRow(): Boolean {
        return topLeft == botLeft
    }

    override fun isCol(): Boolean {
        return topLeft == topRight
    }

    override fun isCell(): Boolean {
        return topLeft == botRight
    }

    override fun takeCrossCell(cellAddress: CellAddress): CellAddress {
        val selectedRange = this
        val rt = when (cellAddress) {
            selectedRange.topLeft -> selectedRange.botRight
            selectedRange.botLeft -> selectedRange.topRight
            selectedRange.topRight -> selectedRange.botLeft
            selectedRange.botRight -> selectedRange.topLeft
            else -> throw IllegalArgumentException()
        }
        return rt
    }
}
