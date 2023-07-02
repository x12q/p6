package com.qxdzbc.p6.app.document.range.address

import com.qxdzbc.p6.app.document.cell.address.CellAddress

abstract class BaseRangeAddress : RangeAddress {

    private fun findIntersectionPoints(rangeAddress: RangeAddress): List<CellAddress> {
        val ra = rangeAddress
        val op1 = ra.topLeft.upOneRow().leftOneCol()
        val op2 = ra.topRight.upOneRow().rightOneCol()
        val op3 = ra.botLeft.downOneRow().leftOneCol()
        val op4 = ra.botRight.downOneRow().rightOneCol()
        if (rangeAddress in this) {
            return listOf(op1, op2, op3, op4)
        } else {
            var p1 = op1
            var p2 = op2
            var p3 = op3
            var p4 = op4
            //==== 1 2
            if (op1 !in this && op2 in this) {
                p1 = CellAddress(this.colRange.first-1, op1.rowIndex)
            }

            if (op1 in this && op2 !in this) {
                p2 = CellAddress(this.colRange.last+1, op2.rowIndex)
            }

            if (op1 !in this && op2 !in this && op1.rowIndex in this.rowRange) {
                p1 = CellAddress(this.colRange.first-1, op1.rowIndex)
                p2 = CellAddress(this.colRange.last+1, op2.rowIndex)
            }
            // ==== 1 3
            if (op1 in this && op3 !in this) {
                p3 = CellAddress(op3.colIndex, this.rowRange.last+1)
            }
            if (op1 !in this && op3 in this) {
                p1 = CellAddress(p1.colIndex, this.rowRange.first-1)
            }

            if (op1 !in this && op3 !in this && op1.colIndex in this.colRange) {
                p3 = CellAddress(op3.colIndex, this.rowRange.last+1)
                p1 = CellAddress(p1.colIndex, this.rowRange.first-1)
            }

            //==== 2 4
            if (op2 in this && op4 !in this) {
                p4 = CellAddress(p4.colIndex, this.rowRange.last+1)
            }
            if (op2 !in this && op4 in this) {
                p2 = CellAddress(p2.colIndex, this.rowRange.first-1)
            }
            if (op2 !in this && op4 !in this && op2.colIndex in this.colRange) {
                p4 = CellAddress(p4.colIndex, this.rowRange.last+1)
                p2 = CellAddress(p2.colIndex, this.rowRange.first-1)
            }

            // ==== 3 4
            if (op3 in this && op4 !in this) {
                p4 = CellAddress(this.colRange.last+1, p4.rowIndex)
            }
            if (op3 !in this && op4 in this) {
                p3 = CellAddress(this.colRange.first-1, p3.rowIndex)
            }
            if (op3 !in this && op4 !in this && op3.rowIndex in this.rowRange) {
                p3 = CellAddress(this.colRange.first, p3.rowIndex)
                p4 = CellAddress(this.colRange.last, p4.rowIndex)
            }
            return listOf(p1, p2, p3, p4)
        }
    }

    override fun getNotIn(rangeAddress: RangeAddress): List<RangeAddress> {
        if (this.isValid() && rangeAddress.isValid()) {
            if (hasIntersectionWith(rangeAddress)) {
                if (this in rangeAddress) {
                    return emptyList()
                } else {
                    val ra = rangeAddress
                    val rt = mutableListOf<RangeAddress>()
                    val interSectionPoints = findIntersectionPoints(ra)
                    val p1 = interSectionPoints[0]
                    val p2 = interSectionPoints[1]
                    val p3 = interSectionPoints[2]
                    val p4 = interSectionPoints[3]
                    fun commonCheck(r: RangeAddress): Boolean {
                        return r in this && r.intersect(ra) == null
                    }

                    val s1 = RangeAddress(this.topLeft, p1)
                    if (commonCheck(s1)) {
                        rt.add(s1)
                    }
                    val s2 = RangeAddress(
                        p1.rightOneCol(),
                        CellAddress(
                            col = p2.colIndex - 1, row = this.rowRange.first
                        )
                    )
                    if (commonCheck(s2)) {
                        rt.add(s2)
                    }

                    val s3 = RangeAddress(this.topRight, p2)
                    if (commonCheck(s3)) {
                        rt.add(s3)
                    }

                    val s4 = RangeAddress(
                        p1.downOneRow(),
                        CellAddress(col = this.colRange.first, row = p3.rowIndex - 1)
                    )
                    if (commonCheck(s4)) {
                        rt.add(s4)
                    }

                    val s5 = RangeAddress(
                        p2.downOneRow(),
                        CellAddress(col = this.colRange.last, row = p4.rowIndex - 1)
                    )
                    if (commonCheck(s5)) {
                        rt.add(s5)
                    }

                    val s6 = RangeAddress(this.botLeft, p3)
                    if (commonCheck(s6)) {
                        rt.add(s6)
                    }

                    val s7 = RangeAddress(
                        p3.rightOneCol(),
                        CellAddress(col = p4.colIndex - 1, row = this.rowRange.last)
                    )
                    if (commonCheck(s7)) {
                        rt.add(s7)
                    }

                    val s8 = RangeAddress(this.botRight, p4)
                    if (commonCheck(s8)) {
                        rt.add(s8)
                    }
                    return RangeAddressUtils.exhaustiveMergeRanges(rt)
                }
            } else {
                return listOf(this)
            }
        } else {
            return listOf(this)
        }
    }

    override fun hasIntersectionWith(rangeAddress: RangeAddress): Boolean {
        val ra = rangeAddress
        val intersectionExist = this.contains(ra.topLeft)
                || this.contains(ra.topRight)
                || this.contains(ra.botLeft)
                || this.contains(ra.botRight)
                || ra.contains(this.topLeft)
                || ra.contains(this.topRight)
                || ra.contains(this.botLeft)
                || ra.contains(this.botRight)
        val c1 =
            (ra.topLeft.colIndex in this.colRange && ra.topLeft.rowIndex <= this.rowRange.last && ra.botLeft.rowIndex >= this.rowRange.first)
                    || (ra.topLeft.rowIndex in this.rowRange && ra.topLeft.colIndex <= this.colRange.last && ra.topRight.colIndex >= this.colRange.first)

        val c2 =
            (ra.topRight.colIndex in this.colRange && ra.topRight.rowIndex <= this.rowRange.last && ra.botRight.rowIndex >= this.rowRange.first)
                    || (ra.topRight.rowIndex in this.rowRange && ra.topRight.colIndex >= this.colRange.first && ra.topLeft.colIndex <= this.colRange.last)

        val c3 =
            (ra.botLeft.colIndex in this.colRange && ra.botLeft.rowIndex >= this.rowRange.first && ra.topLeft.rowIndex <= this.rowRange.last)
                    || (ra.botLeft.rowIndex in this.rowRange && ra.botLeft.colIndex <= this.colRange.last && ra.botRight.colIndex >= this.colRange.first)

        val c4 =
            (ra.botRight.colIndex in this.colRange && ra.botRight.rowIndex >= this.rowRange.first && ra.topRight.rowIndex <= this.rowRange.last)
                    || (ra.botRight.rowIndex in this.rowRange && ra.botRight.colIndex >= this.colRange.first && ra.botLeft.colIndex <= this.colRange.last)
        val c = c1 || c2 || c3 || c4
        return intersectionExist || c
    }

    override fun intersect(otherRangeAddress: RangeAddress): RangeAddress? {
        if (this.isValid() && otherRangeAddress.isValid()) {
            if (hasIntersectionWith(otherRangeAddress)) {
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
