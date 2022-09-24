package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

data class IntNum(val _v: Int) : NumberUnit(_v) {
    companion object{
        fun Int.exUnit(): IntNum {
            return IntNum(this)
        }
    }
    override fun run(): Result<Int, ErrorReport> {
        return Ok(this.v as Int)
    }

    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): ExUnit {
        return this
    }

    override fun toFormula(): String {
        return this._v.toString()
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        return _v.toString()
    }
}

