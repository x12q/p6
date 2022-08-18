package com.emeraldblast.p6.app.document.cell.address

import com.emeraldblast.p6.app.common.utils.Rs
import com.emeraldblast.p6.app.common.utils.CellLabelNumberSystem
import com.emeraldblast.p6.app.document.cell.CellErrors
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.DocProtos
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import java.util.regex.Pattern

object CellAddresses {
    val InvalidCell = fromIndices(-1,-1)
    val A1 = fromIndices(1,1)
    fun fromIndices(colIndex:Int, rowIndex:Int): CellAddress {
        return CellAddressImp(colIndex,rowIndex)
    }

    fun firstOfCol(colIndex: Int):CellAddress{
        return fromIndices(colIndex,1)
    }

    fun firstOfRow(rowIndex: Int):CellAddress{
        return fromIndices(1,rowIndex)
    }
    // A1, A2
    val labelPattern: Pattern = Pattern.compile("[A-Za-z]+[1-9][0-9]*")

    fun fromLabelRs(label:String): Rs<CellAddress, ErrorReport> {
        if(labelPattern.matcher(label).matches()){
            // extract col and row index from the label
            var colLabel = ""
            var rowLabel = ""
            for (c in label){
                if(c.isDigit()){
                    rowLabel += c
                }else{
                    colLabel += c
                }
            }
            val colIndex = CellLabelNumberSystem.labelToNumber(colLabel)
            val rowIndex = rowLabel.toInt()
            val address = fromIndices(colIndex,rowIndex)
            return Ok(address)
        }else{
            return CellErrors.InvalidCellAddress.report(label).toErr()
        }
    }


    fun fromLabel(label:String): CellAddress {
        val rs = fromLabelRs(label)
        when(rs){
            is Ok -> return rs.value
            is Err -> throw InvalidLabelException(label)
        }
    }
}


fun DocProtos.CellAddressProto.toModel(): CellAddress {
    return CellAddresses.fromIndices(this.col, this.row)
}
