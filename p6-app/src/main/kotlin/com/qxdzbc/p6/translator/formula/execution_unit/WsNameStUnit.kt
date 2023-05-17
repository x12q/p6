package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import androidx.compose.runtime.State
/**
 * An [ExUnit] representing a [State] holding a worksheet name in string
 */
@JvmInline
value class WsNameStUnit(val nameSt: St<String>) : ExUnit {
    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
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

    override fun runRs(): Result<St<String>, SingleErrorReport> {
        return Ok(nameSt)
    }
}

