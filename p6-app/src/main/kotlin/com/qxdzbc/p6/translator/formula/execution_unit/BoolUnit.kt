package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.common.utils.Utils.toTF
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

@JvmInline
value class BoolUnit(val v: Boolean) : ExUnit {
    companion object{
        val TRUE = BoolUnit(true)
        val FALSE = BoolUnit(false)
        fun Boolean.exUnit(): BoolUnit {
            return BoolUnit(this)
        }
    }
    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): ExUnit {
        return this
    }

    override fun toFormula(): String {
        return v.toTF()
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        return v.toTF()
    }

    override fun runRs(): Result<Boolean, ErrorReport> {
        return Ok(v)
    }
}

