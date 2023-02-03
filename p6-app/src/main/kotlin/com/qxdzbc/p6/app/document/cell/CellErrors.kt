package com.qxdzbc.p6.app.document.cell

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.SingleErrorReport

object CellErrors {
    val prefix = "CellErrors "

    val all:List<Any> get(){
        if(_l==null){
            _l=listOf(NotEditable,InvalidCellValue,InvalidCellAddress,OverflowError)
        }
        return _l!!
    }
    private var _l:List<Any>? = null

    fun coldInit(){
        val q = listOf(this)+all
        for( e in q){
            println("cold init ${e::class}")
        }
    }
    object NotEditable{
        val header = ErrorHeader("${prefix}0","Cell cannot be edited")
        fun report(cellAddress: CellAddress):SingleErrorReport{
            return SingleErrorReport(
                header= header.setDescription("Cell ${cellAddress.label} cannot be edited")
            )
        }
    }
    object InvalidCellValue{
        val header = ErrorHeader("${prefix}1","Invalid cell value")
        fun report(newValue:Any?):SingleErrorReport{
            return SingleErrorReport(
                header= header.setDescription("Invalid cell value. A cell can only store text, number, boolean, range, cell, or error. The new value type is ${newValue?.javaClass?.name}")
            )
        }
    }
    object InvalidCellAddress{
        val header = ErrorHeader("${prefix}2","Invalid cell address")
        fun report(label:String):SingleErrorReport{
            return SingleErrorReport(
                header= header.setDescription("Invalid cell address: ${label}")
            )
        }
    }
    object OverflowError{
        val header = ErrorHeader("${prefix}3","Overflowing")
        fun report(detail:String?=null):SingleErrorReport{
            return SingleErrorReport(
                header= detail?.let { header.setDescription(detail) } ?: header
            )
        }
    }
}
