package com.qxdzbc.p6.app.document.range.address

import com.qxdzbc.p6.app.document.cell.address.CellAddress

abstract class BaseRangeAddress : RangeAddress {

    private fun findIntersectionPoints(rangeAddress: RangeAddress):List<CellAddress>{
        val ra = rangeAddress
        val op1 = ra.topLeft.upOneRow().leftOneCol()
        val op2 = ra.topRight.upOneRow().rightOneCol()
        val op3 = ra.botLeft.downOneRow().leftOneCol()
        val op4 = ra.botRight.downOneRow().rightOneCol()
        if(rangeAddress in this){
            return listOf(op1,op2,op3,op4)
        }else{
            var p1 = op1
            var p2 = op2
            var p3 = op3
            var p4 = op4
            //==== 1 2
            if(op1 !in this && op2 in this){
                p1= CellAddress(this.colRange.first,op1.rowIndex)
            }

            if(op1 in this && op2 !in this){
                p2= CellAddress(this.colRange.last,op2.rowIndex)
            }

            if(op1 !in this && op2 !in this && op1.rowIndex in this.rowRange){
                p1= CellAddress(this.colRange.first,op1.rowIndex)
                p2= CellAddress(this.colRange.last,op2.rowIndex)
            }
            // ==== 1 3
            if(op1 in this && op3 !in this){
                p3 = CellAddress(op3.colIndex,this.rowRange.last)
            }
            if(op1 !in this && op3 in this){
                p1 = CellAddress(p1.colIndex,this.rowRange.first)
            }

            if(op1 !in this && op3 !in this && op1.colIndex in this.colRange){
                p1 = CellAddress(p1.colIndex,this.rowRange.first)
                p3 = CellAddress(op3.colIndex,this.rowRange.last)
            }

            //==== 2 4
            if(op2 in this && op4!in this){
                p4 = CellAddress(p4.colIndex,this.rowRange.last)
            }
            if(op2 !in this && op4 in this){
                p2 = CellAddress(p2.colIndex,this.rowRange.first)
            }
            if(op2 !in this && op4 !in this && op2.colIndex in this.colRange){
                p4 = CellAddress(p4.colIndex,this.rowRange.last)
                p2 = CellAddress(p2.colIndex,this.rowRange.first)
            }

            // ==== 3 4
            if(op3 in this && op4 !in this){
                p4 = CellAddress(this.colRange.last,p4.rowIndex)
            }
            if(op3 !in this && op4 in this){
                p3 = CellAddress(this.colRange.first,p3.rowIndex)
            }
            if(op3 !in this && op4 !in this && op3.rowIndex in this.rowRange){
                p3 = CellAddress(this.colRange.first,p3.rowIndex)
                p4 = CellAddress(this.colRange.last,p4.rowIndex)
            }
            return listOf(p1,p2,p3,p4)
        }
    }

    override fun getNotIn(rangeAddress: RangeAddress): List<RangeAddress> {
        if (this.isValid() && rangeAddress.isValid()) {
            if (isInterSectionExistWith(rangeAddress)) {
                if(this in rangeAddress){
                    return emptyList()
                }else{
                    val ra = rangeAddress
                    val rt = mutableListOf<RangeAddress>()
                    val interSectionPoints = findIntersectionPoints(ra)
                    val p1 = interSectionPoints[0]
                    val p2 = interSectionPoints[1]
                    val p3 = interSectionPoints[2]
                    val p4 = interSectionPoints[3]

                    val s1 = RangeAddress(this.topLeft,p1)
                    if(s1 in this){
                        rt.add(s1)
                    }
                    val s2 = RangeAddress(p1.rightOneCol(), CellAddress(
                        col=p2.colIndex-1,row = this.rowRange.first
                    ))
                    if(s2 in this){
                        rt.add(s2)
                    }

                    val s3 = RangeAddress(this.topRight,p2)
                    if(s3 in this){
                        rt.add(s3)
                    }

                    val s4 = RangeAddress(p1.downOneRow(),CellAddress(col=this.colRange.first,row = p3.rowIndex-1))
                    if(s4 in this){
                        rt.add(s4)
                    }

                    val s5 = RangeAddress(p2.downOneRow(),CellAddress(col=this.colRange.last,row=p4.rowIndex-1))
                    if(s5 in this){
                        rt.add(s5)
                    }

                    val s6 = RangeAddress(this.botLeft,p3)
                    if(s6 in this){
                        rt.add(s6)
                    }

                    val s7 = RangeAddress(p3.rightOneCol(), CellAddress(col=p4.colIndex-1,row=this.rowRange.last))
                    if(s7 in this){
                        rt.add(s7)
                    }

                    val s8 = RangeAddress(this.botRight,p4)
                    if(s8 in this){
                        rt.add(s8)
                    }
                    return RangeAddresses.exhaustiveMergeRanges(rt)
                }
            } else {
                return listOf(this)
            }
        } else {
            return listOf(this)
        }
    }

    private fun isInterSectionExistWith(rangeAddress: RangeAddress): Boolean {
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
