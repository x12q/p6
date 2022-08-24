package com.qxdzbc.p6.translator.formula.execution_unit

import kotlin.reflect.KFunction

/**
 * Define how a KFunction should be called. Most of the time, the default is ok.
 */
interface ExecutionWay {
    /**
     * execute a function, and return what the function returned
     */
    fun execute(func: KFunction<Any>, args: Array<Any?>): Any

    object Default : ExecutionWay {
        override fun execute(func: KFunction<Any>, args: Array<Any?>): Any {
            return func.call(*args)
        }
    }
}
