package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Result
import com.qxdzbc.common.Rse
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.SingleErrorReport
import kotlin.reflect.KFunction

/**
 * Define how a KFunction should be called. Most of the time, the default is ok.
 */
interface FunctionExecutor {
    /**
     * execute a function, and return what the function returned
     */
    fun execute(func: KFunction<Any>, args: Array<Any?>): Rse<Any>

    object ArgsAsArray : FunctionExecutor {
        @Suppress("UNCHECKED_CAST")
        override fun execute(func: KFunction<Any>, args: Array<Any?>): Rse<Any> {
            return runFunction {
                func.call(*args) as Result<Any, SingleErrorReport>
            }
        }
    }
    @Suppress("UNCHECKED_CAST")
    object ArgsAsList : FunctionExecutor {
        override fun execute(func: KFunction<Any>, args: Array<Any?>): Rse<Any> {
            return runFunction{
                func.call(args.asList()) as Result<Any, SingleErrorReport>
            }
        }
    }

    companion object{
        private fun runFunction(f:()->Rse<Any>):Rse<Any>{
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
