package com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.document_data_layer.cell.address.CRAddress
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import androidx.compose.runtime.State
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

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

