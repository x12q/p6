package com.qxdzbc.p6.translator.formula.execution_unit

import com.qxdzbc.common.error.CommonErrors
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KFunction

/**
 * Define how a KFunction should be called. Most of the time, the default is ok.
 */
interface FunctionExecutor {
    /**
     * execute a function, and return what the function returned
     */
    fun execute(func: KFunction<Any>, args: Array<Any?>): Any

    object Default : FunctionExecutor {
        override fun execute(func: KFunction<Any>, args: Array<Any?>): Any {
//            try{
                return func.call(*args)
//            }catch (e:Throwable){
//                if(e is InvocationTargetException){
//                    println(e.stackTrace)
//                }
//                return CommonErrors.ExceptionError.report(e)
//            }

        }
    }
}
