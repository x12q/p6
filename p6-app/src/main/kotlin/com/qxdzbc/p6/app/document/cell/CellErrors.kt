package com.qxdzbc.p6.app.document.cell

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport

object CellErrors {
    val CellUIErr = "UI_CellErrors_"
    object NotEditable{
        val header = ErrorHeader("${CellUIErr}0","Cell cannot be edited")
        fun report(cellAddress: CellAddress):ErrorReport{
            return ErrorReport(
                header= header.setDescription("Cell ${cellAddress.toLabel()} cannot be edited")
            )
        }
    }
    object InvalidCellValue{
        val header = ErrorHeader("${CellUIErr}1","Invalid cell value")
        fun report(newValue:Any?):ErrorReport{
            return ErrorReport(
                header= header.setDescription("Invalid cell value. A cell can only store text, number, boolean, range, cell, or error. The new value type is ${newValue?.javaClass?.name}")
            )
        }
    }
    object InvalidCellAddress{
        val header = ErrorHeader("${CellUIErr}2","Invalid cell address")
        fun report(label:String):ErrorReport{
            return ErrorReport(
                header= header.setDescription("Invalid cell address: ${label}")
            )
        }
    }
    object OverflowError{
        val header = ErrorHeader("${CellUIErr}3","Overflowing")
        fun report(detail:String?=null):ErrorReport{
            return ErrorReport(
                header= detail?.let { header.setDescription(detail) } ?: header
            )
        }
    }
}
