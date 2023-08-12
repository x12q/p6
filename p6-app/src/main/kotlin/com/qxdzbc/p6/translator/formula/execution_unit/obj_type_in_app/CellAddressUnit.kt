package com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.address.CRAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

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

