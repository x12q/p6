package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

data class WbKeyStUnit(val wbKeySt: St<WorkbookKey>) : ExUnit {
    companion object{
        fun St<WorkbookKey>.toExUnit(): WbKeyStUnit {
            return WbKeyStUnit(this)
        }
    }
    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): ExUnit {
        return this
    }

    override fun toFormula(): String {
        val p = wbKeySt.value.path
        if (p != null) {
            return "@\'${wbKeySt.value.name}\'@\'${p.toAbsolutePath()}\'"
        } else {
            return "@\'${wbKeySt.value.name}\'"
        }
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        if (wbKey == this.wbKeySt.value) {
            return ""
        } else {
            return toFormula()
        }
    }

    override fun runRs(): Result<St<WorkbookKey>, ErrorReport> {
        return Ok(wbKeySt)
    }
}

