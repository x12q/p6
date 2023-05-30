package com.qxdzbc.p6.translator.formula.execution_unit.primitive

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

/**
 * An [ExUnit] representing an int
 */
data class IntUnit(val v: Int) : ExUnit {
    companion object{
        fun Int.toExUnit(): IntUnit {
            return IntUnit(this)
        }
    }
    override fun runRs(): Result<Int, SingleErrorReport> {
        return Ok(this.v)
    }

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): ExUnit {
        return this
    }

    override fun toFormula(): String {
        return this.v.toString()
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        return v.toString()
    }
}

