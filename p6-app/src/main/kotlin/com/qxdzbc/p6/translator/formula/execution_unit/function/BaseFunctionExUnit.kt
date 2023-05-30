package com.qxdzbc.p6.translator.formula.execution_unit.function

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.formula.function_def.FunctionDef
import kotlin.reflect.KFunction

/**
 * An [ExUnit] representing a function. All function [ExUnit] must derive from this class.
 */
abstract class BaseFunctionExUnit : ExUnit {
    abstract val functionMap: FunctionMap
    abstract val args: List<ExUnit>
    abstract val funcName: String

    @Suppress("UNCHECKED_CAST")
    override fun runRs(): Result<Any?, ErrorReport> {
        try {
            // x: evaluate the args
            val argValueRs: Array<Result<Any?, ErrorReport>> = args.map { it.runRs() }.toTypedArray()
            val funcRs: Rs<FunctionDef, ErrorReport> = functionMap.getFuncRs(funcName)
            when (funcRs) {
                is Ok -> {
                    val funcDef: FunctionDef = funcRs.value
                    val func: KFunction<Any> = funcDef.function
                    val argsContainerErrorsReport:Err<ErrorReport>? = run {
                        val errs: List<ErrorReport> =
                            argValueRs.filterIsInstance<Err<ErrorReport>>().map { it.component2() }
                        val argsContainErrors = errs.isNotEmpty()
                        if (argsContainErrors) {
                            CommonErrors.MultipleErrors.report(errs).toErr()
                        } else {
                            null
                        }
                    }
                    if (argsContainerErrorsReport != null) {
                        return argsContainerErrorsReport
                    } else {
                        val argValues: Array<Any?> = argValueRs
                            .map { it.component1() }
                            .toTypedArray()
                        try {
                            val functionExecutor: FunctionExecutor = funcDef.functionExecutor
                            val funcOutput:Any = functionExecutor.execute(func, argValues)
                            return funcOutput as Result<Any, SingleErrorReport>
                        } catch (e: Exception) {
                            return CommonErrors.ExceptionError.report(e).toErr()
                        }
                    }
                }

                is Err -> {
                    return funcRs
                }
            }
        } catch (e: Throwable) {
            return CommonErrors.ExceptionError.report(e).toErr()
        }
    }
}
