package com.qxdzbc.p6.app.document.range.address

import com.qxdzbc.p6.app.document.cell.address.CellAddress

abstract class BaseRangeAddress : RangeAddress {

    override fun getNotIn(rangeAddress: RangeAddress): List<RangeAddress> {
        if (this.isValid() && rangeAddress.isValid()) {
            if(isInterSectionExistWith(rangeAddress)){
                // ====
                val rt = mutableListOf<RangeAddress>()
                // 1st segment
                val _1stTopleft = this.topLeft
                val _1stBotRight = CellAddress(
                    this.botRight.colIndex,
                    rangeAddress.rowRange.first -1
                )


                if (_1stBotRight.isValid()
                    && _1stBotRight in this
//                    && _1stTopleft != cellAddress
//                    && _1stBotRight != cellAddress
                ) {
                    rt.add(RangeAddress(_1stTopleft, _1stBotRight))
                }

                // 2nd segment
                val _2ndTopleft = CellAddress(
                    rangeAddress.colRange.last + 1,
                    rangeAddress.rowRange.first)
                val _2ndBotRight = this.botRight
                if (_2ndTopleft.isValid()
                    && _2ndTopleft in this
                ) {
                    rt.add(RangeAddress(_2ndTopleft, _2ndBotRight))
                }
//
                //3rd segment
                val _3rdTopLeft = CellAddress(
                    this.colRange.first,
                    rangeAddress.rowRange.last+1
                )
                val _3rdBotRight = CellAddress(
                    rangeAddress.colRange.last,
                    this.rowRange.last
                )
                if (_3rdTopLeft.isValid() && _3rdBotRight.isValid()
                    && _3rdTopLeft in this && _3rdBotRight in this
                ) {
                    rt.add(RangeAddress(_3rdTopLeft, _3rdBotRight))
                }
//
                //4th segment
                val _4thTopLeft = CellAddress(this.colRange.first,rangeAddress.rowRange.first)
                val _4thBotRight = CellAddress(rangeAddress.colRange.first-1,minOf(rangeAddress.rowRange.last,this.rowRange.last))
                if (
                    _4thTopLeft.isValid() && _4thBotRight.isValid() &&
                    _4thTopLeft in this && _4thBotRight in this
                ) {
                    rt.add(RangeAddress(_4thBotRight, _4thTopLeft))
                }
                return rt

                // ====

            }else{
                return listOf(this)
            }
        }else{
            return listOf(this)
        }
    }

    private fun isInterSectionExistWith(rangeAddress: RangeAddress):Boolean{
        val intersectionExist = this.contains(rangeAddress.topLeft)
                    || this.contains(rangeAddress.topRight)
                    || this.contains(rangeAddress.botLeft)
                    || this.contains(rangeAddress.botRight)
                    || rangeAddress.contains(this.topLeft)
                    || rangeAddress.contains(this.topRight)
                    || rangeAddress.contains(this.botLeft)
                    || rangeAddress.contains(this.botRight)
        return intersectionExist
    }

    override fun intersect(otherRangeAddress: RangeAddress): RangeAddress? {
        if (this.isValid() && otherRangeAddress.isValid()) {
            if (isInterSectionExistWith(otherRangeAddress)) {
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

    override fun takeCrossCell(vertex: CellAddress): CellAddress {
        val selectedRange = this
        val rt = when (vertex) {
            selectedRange.topLeft -> selectedRange.botRight
            selectedRange.botLeft -> selectedRange.topRight
            selectedRange.topRight -> selectedRange.botLeft
            selectedRange.botRight -> selectedRange.topLeft
            else -> throw IllegalArgumentException()
        }
        return rt
    }
}
