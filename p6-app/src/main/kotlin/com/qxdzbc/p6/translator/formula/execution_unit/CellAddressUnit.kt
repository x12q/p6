package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

/**
 * An [ExUnit] representing a cell address
 */
data class CellAddressUnit(val cellAddress: CellAddress) : ExUnit {
    companion object{
        fun CellAddress.toExUnit(): CellAddressUnit {
            return CellAddressUnit(this)
        }
    }
    override fun getCellRangeExUnit(): List<ExUnit> {
        return listOf()
    }

    override fun getRanges(): List<RangeAddress> {
        return listOf(RangeAddress(cellAddress))
    }

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): CellAddressUnit {
        return this.copy(cellAddress = cellAddress.shift(oldAnchorCell, newAnchorCell))
    }

    override fun toFormula(): String {
        return cellAddress.label
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        return cellAddress.label
    }

    override fun runRs(): Result<CellAddress, SingleErrorReport> {
        return Ok(cellAddress)
    }
}

