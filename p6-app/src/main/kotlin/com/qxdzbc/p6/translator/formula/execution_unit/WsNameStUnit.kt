package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

@JvmInline
value class WsNameStUnit(val nameSt: St<String>) : ExUnit {
    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): ExUnit {
        return this
    }

    override fun toFormula(): String {
        return "@\'${nameSt.value}\'"
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        if (wsName == nameSt.value) {
            return ""
        } else {
            return toFormula()
        }
    }

    override fun runRs(): Result<St<String>, ErrorReport> {
        return Ok(nameSt)
    }
}

