package com.emeraldblast.p6.app.document.range.address

import com.emeraldblast.p6.app.common.Rs
import com.emeraldblast.p6.app.common.utils.CellLabelNumberSystem
import com.emeraldblast.p6.app.common.utils.ResultUtils.toOk
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.address.CellAddresses
import com.emeraldblast.p6.app.document.cell.address.toModel
import com.emeraldblast.p6.app.document.range.RangeErrors
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.DocProtos.RangeAddressProto
import com.emeraldblast.p6.ui.common.R
import com.github.michaelbull.result.Ok


object RangeAddresses {

    val InvalidRange = RangeAddressImp(CellAddresses.InvalidCell,CellAddresses.InvalidCell)
    // A1:B2
    val rangeAddressPattern = Regex("[a-zA-Z]+[1-9][0-9]*:[a-zA-Z]+[1-9][0-9]*")
    // whole col / whole row : A:A , 123: 233
    val wholeRangeAddressPattern = Regex("([a-zA-Z]+|[1-9][0-9]*):([a-zA-Z]+|[1-9][0-9]*)")
    val singleWholeColAddressPattern = Regex("[a-zA-Z]+")
    val singleWholeRowAddressPattern = Regex("[1-9][0-9]*")

    fun fromLabelRs(label:String):Rs<RangeAddress,ErrorReport>{
        val isNormalRange = rangeAddressPattern.matchEntire(label) != null
        if(isNormalRange){
            val splits = label.split(":")
            val cell1 = CellAddress(splits[0])
            val cell2 = CellAddress(splits[1])
            return RangeAddress(cell1,cell2).toOk()
        }
        val isWholeRange = wholeRangeAddressPattern.matchEntire(label)!=null
        if(isWholeRange){
            val splits = label.split(":")
            val firstPart = splits[0]
            val lastPart = splits[1]

            val firstPartIsCol = singleWholeColAddressPattern.matchEntire(firstPart)!=null
            val firstPartIsRow = singleWholeRowAddressPattern.matchEntire(firstPart)!=null

            val lastPartIsCol = singleWholeColAddressPattern.matchEntire(lastPart)!=null
            val lastPartIsRow = singleWholeRowAddressPattern.matchEntire(lastPart)!=null

            if(firstPartIsCol && lastPartIsCol){
                val firstColIndex = CellLabelNumberSystem.labelToNumber(firstPart)
                val lastColIndex = CellLabelNumberSystem.labelToNumber(lastPart)

                val first = minOf(firstColIndex,lastColIndex)
                val last = maxOf(firstColIndex,lastColIndex)
                return RangeAddress(
                    CellAddress(first,1),
                    CellAddress(last,R.worksheetValue.rowLimit)
                ).toOk()
            }else if(firstPartIsRow && lastPartIsRow){
                val fp = firstPart.toInt()
                val lp = lastPart.toInt()
                val first = minOf(fp,lp)
                val last = maxOf(fp,lp)
                return RangeAddress(
                    CellAddress(1, first),
                    CellAddress(R.worksheetValue.colLimit, last)
                ).toOk()
            }else{
                return RangeErrors.InvalidRangeAddress.report(label).toErr()
            }
        }
        return RangeErrors.InvalidRangeAddress.report(label).toErr()
    }

    /**
     * Create a [RangeAddress] from a label, such as "A1:B3", "22:33", "F:Z"
     */
    fun fromLabel(label:String): RangeAddress {
        val z = this.fromLabelRs(label)
        when(z){
            is Ok -> return z.value
            else -> throw IllegalArgumentException("Illegal range address format: ${label}")
        }
    }

    fun fromManyCells(cells:List<CellAddress>): RangeAddress {
        if(cells.isEmpty()){
            throw IllegalArgumentException("Can't construct range address from 0 cell address")
        }else{
            var maxRow = -1
            var maxCol = -1
            var minRow = Int.MAX_VALUE
            var minCol = Int.MAX_VALUE
            for(cell in cells){
                val r = cell.rowIndex
                val c = cell.colIndex
                if (r >= maxRow) maxRow = r
                if(r <= minRow) minRow = r
                if(c >= maxCol) maxCol = c
                if(c <= minCol) minCol = c
            }
            return RangeAddressImp(
                topLeft = CellAddresses.fromIndices(minCol,minRow),
                botRight = CellAddresses.fromIndices(maxCol,maxRow)
            )
        }
    }

    fun from2Cells(address1: CellAddress, address2: CellAddress): RangeAddress {
        val firstAddress = CellAddresses.fromIndices(
            colIndex = minOf(address1.colIndex, address2.colIndex),
            rowIndex = minOf(address1.rowIndex, address2.rowIndex)
        )
        val lastAddress = CellAddresses.fromIndices(
            colIndex = maxOf(address1.colIndex, address2.colIndex),
            rowIndex = maxOf(address1.rowIndex, address2.rowIndex)
        )
        return RangeAddressImp(firstAddress, lastAddress)
    }

    fun singleCell(cellAddress: CellAddress): RangeAddress {
        return RangeAddressImp(cellAddress, cellAddress)
    }

    fun wholeCol(colIndex: Int): RangeAddress {
        return from2Cells(
            address1 = CellAddresses.fromIndices(colIndex, 1),
            address2 = CellAddresses.fromIndices(colIndex, R.worksheetValue.rowLimit),
        )
    }

    fun wholeRow(rowIndex: Int): RangeAddress {
        return from2Cells(
            address1 = CellAddresses.fromIndices(1, rowIndex),
            address2 = CellAddresses.fromIndices(R.worksheetValue.colLimit, rowIndex),
        )
    }

    fun wholeMultiRow(row1:Int,row2:Int): RangeAddress {
        val fromRow = minOf(row1,row2)
        val toRow = maxOf(row1,row2)
        var rt = wholeRow(fromRow)
        for(x in fromRow+1 .. toRow){
           rt = rt.mergeWith(wholeRow(x))
        }
        return rt
    }

    fun wholeMultiCol(col1:Int, col2:Int): RangeAddress {
        val fromCol = minOf(col1,col2)
        val toCol = maxOf(col1,col2)
        var rt = wholeCol(fromCol)
        for(x in fromCol+1 .. toCol){
            rt = rt.mergeWith(wholeCol(x))
        }
        return rt
    }
    fun RangeAddressProto.toModel(): RangeAddress {
        return from2Cells(
            address1 = this.topLeft.toModel(),
            address2 =  this.botRight.toModel()
        )
    }
}
