package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

/**
 * An [ExUnit] representing a range address
 */
data class RangeAddressUnit(val rangeAddress: RangeAddress) : ExUnit {
    companion object{
        fun RangeAddress.toExUnit(): RangeAddressUnit {
            return RangeAddressUnit(this)
        }
    }

    override fun getCellRangeExUnit(): List<ExUnit> {
        return listOf()
    }

    override fun getRanges(): List<RangeAddress> {
        return listOf(rangeAddress)
    }

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): RangeAddressUnit {
        return this.copy(rangeAddress = rangeAddress.shift(oldAnchorCell, newAnchorCell))
    }

    override fun toFormula(): String {
        return rangeAddress.label
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        return rangeAddress.label
    }

    override fun runRs(): Result<RangeAddress, SingleErrorReport> {
        return Ok(rangeAddress)
    }
}

