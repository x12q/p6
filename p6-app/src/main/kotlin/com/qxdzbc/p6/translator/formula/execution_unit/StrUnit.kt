package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

@JvmInline
value class StrUnit(val v: String) : ExUnit {
    companion object{
        fun String.exUnit(): StrUnit {
            return StrUnit(this)
        }
    }
    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): ExUnit {
        return this
    }

    override fun toFormula(): String {
        return "\"${v}\""
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        return "\"${v}\""
    }

    override fun run(): Result<String, ErrorReport> {
        return Ok(v)
    }
}

