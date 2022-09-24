package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

data class CellAddressUnit(val cellAddress: CellAddress) : ExUnit {
    companion object{
        fun CellAddress.exUnit(): CellAddressUnit {
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
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): CellAddressUnit {
        return this.copy(cellAddress = cellAddress.shift(oldAnchorCell, newAnchorCell))
    }

    override fun toFormula(): String {
        return cellAddress.toLabel()
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        return cellAddress.toLabel()
    }

    override fun run(): Result<CellAddress, ErrorReport> {
        return Ok(cellAddress)
    }
}

