package com.qxdzbc.p6.translator.formula.execution_unit.primitive

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.document_data_layer.cell.address.CRAddress
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

/**
 * An [ExUnit] representing a string
 */
data class StrUnit(val v: String) : ExUnit {
    companion object{
        fun String.toExUnit(): StrUnit {
            return StrUnit(this)
        }
    }
    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): ExUnit {
        return this
    }

    override fun toFormula(): String {
        return "\"${v}\""
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        return "\"${v}\""
    }

    override fun runRs(): Result<String, SingleErrorReport> {
        return Ok(v)
    }
}

