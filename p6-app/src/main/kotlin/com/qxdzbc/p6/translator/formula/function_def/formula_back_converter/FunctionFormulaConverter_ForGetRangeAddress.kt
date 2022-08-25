package com.qxdzbc.p6.translator.formula.function_def.formula_back_converter

import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitions
import kotlin.io.path.absolute
import kotlin.io.path.absolutePathString

class FunctionFormulaConverter_ForGetRangeAddress : FunctionFormulaConverter {
    override fun toFormula(u: ExUnit.Func): String? {
        val args = u.args
        if (args.size == 3) {
            val a1 = args[0]
            val a2 = args[1]
            val a3 = args[2]
            if(a1 is ExUnit.WbKeyStUnit && a2 is ExUnit.WsNameStUnit && a3 is ExUnit.RangeAddressUnit){
                val t1:String = a1.toFormula()
                val t2:String = a2.toFormula()
                val t3:String = a3.toFormula()
                return t3+t2+t1
            }else{
                return null
            }
        } else {
            return null
        }
    }
}
