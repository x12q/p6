package com.qxdzbc.p6.translator.formula.formula_back_converter

import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.P6FunctionDefinitions
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

class FunctionFormulaConverter_ForGetRangeAddress : FunctionFormulaConverter {
    override fun toFormula(u: ExUnit.Func): String? {
        val args = u.args
        if (u.funcName == P6FunctionDefinitions.getRangeRs && args.size == 3) {
            val rt = args[0].run().flatMap { wbKey ->
                args[1].run().flatMap { wsName ->
                    args[2].run().map { rangeAddress ->
                        if (
                            wbKey is WorkbookKey &&
                            wsName is String &&
                            rangeAddress is RangeAddress
                        ) {

                            ""
                        }else{
                            null
                        }
                    }
                }
            }
            return null
        } else {
            return null
        }
    }
}
