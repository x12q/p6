package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KFunction

/**
 * Define how a KFunction should be called. Most of the time, the default is ok.
 */
interface FunctionExecutor {
    /**
     * execute a function, and return what the function returned
     */
    fun execute(func: KFunction<Any>, args: Array<Any?>): Result<Any, ErrorReport>

    object Default : FunctionExecutor {
        @Suppress("UNCHECKED_CAST")
        override fun execute(func: KFunction<Any>, args: Array<Any?>): Result<Any, ErrorReport> {
            return runFunction {
                func.call(*args) as Result<Any, ErrorReport>
            }
        }
    }
    @Suppress("UNCHECKED_CAST")
    object ArgsAsList : FunctionExecutor {
        override fun execute(func: KFunction<Any>, args: Array<Any?>): Result<Any, ErrorReport> {
            return runFunction{
                func.call(args.asList()) as Result<Any, ErrorReport>
            }
        }
    }

    companion object{
        private fun runFunction(f:()->Result<Any, ErrorReport>):Result<Any, ErrorReport>{
            try {
                return f()
            } catch (e: Throwable) {
                when (e) {
                    is ClassCastException -> {
                        return ExUnitErrors.IllegalReturnType.report(null).toErr()
                    }

                    else -> {
                        return CommonErrors.ExceptionError.report(e).toErr()
                    }
                }
            }
        }
    }
}
