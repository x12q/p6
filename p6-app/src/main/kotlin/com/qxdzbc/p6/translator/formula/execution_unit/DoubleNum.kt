package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

data class DoubleNum(val _v: Double) : NumberUnit(_v) {
    companion object{
        fun Double.exUnit(): DoubleNum {
            return DoubleNum(this)
        }
        fun Float.exUnit(): DoubleNum {
            return DoubleNum(this.toDouble())
        }
    }
    override fun run(): Result<Double, ErrorReport> {
        return Ok(v as Double)
    }

    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): ExUnit {
        return this
    }

    override fun toFormula(): String {
        return _v.toString()
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        return _v.toString()
    }
}

