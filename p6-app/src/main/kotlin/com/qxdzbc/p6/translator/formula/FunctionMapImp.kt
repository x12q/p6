package com.qxdzbc.p6.translator.formula

import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnitErrors
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlin.reflect.KFunction

data class FunctionMapImp(
    private val m:Map<String,FunctionDef>
) : FunctionMap, Map<String, FunctionDef> by m {

    override fun getFunc(name: String): FunctionDef? {
        return m[name]
    }

    override fun getFuncRs(name: String): Result<FunctionDef, ErrorReport> {
        val f = m[name]
        if(f!=null){
            return Ok(f)
        }else{
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
