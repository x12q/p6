package com.qxdzbc.p6.translator.formula

import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnitErrors
import com.qxdzbc.p6.translator.formula.function_def.FunctionDef

data class FunctionMapImp(
    private val m: Map<String, FunctionDef>
) : FunctionMap, Map<String, FunctionDef> by m {

    override fun getFunc(name: String): FunctionDef? {
        return m[name]
    }

    override fun getFuncRs(name: String): Rse<FunctionDef> {
        val f = m[name]
        if (f != null) {
            return Ok(f)
        } else {
            return ExUnitErrors.InvalidFunction.report(name).toErr()
        }
    }

    override fun addFunc(name: String, func: FunctionDef): FunctionMap {
        return FunctionMapImp(m = m + (name to func))
    }

    override fun removeFunc(name: String): FunctionMap {
        return FunctionMapImp(m = m - (name))
    }
}
