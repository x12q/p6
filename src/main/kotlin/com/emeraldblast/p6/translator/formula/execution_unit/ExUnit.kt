package com.emeraldblast.p6.translator.formula.execution_unit

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.d.Cell
import com.emeraldblast.p6.app.document.range.Range
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.translator.formula.FunctionMap
import com.github.michaelbull.result.*
import kotlin.math.pow
import kotlin.reflect.KFunction

/**
 * An ExUnit (execution unit) is a provider obj that when run will return something that can be stored by a Cell
 *
 */
interface ExUnit {

    /**
     * when this run, it returns something
     */
    fun run(): Result<Any, ErrorReport>

    data class RangeAddressUnit(val rangeAddress: RangeAddress) : ExUnit {
        override fun run(): Result<RangeAddress, ErrorReport> {
            return Ok(rangeAddress)
        }
    }

    data class CellAddressUnit(val cellAddress: CellAddress) : ExUnit {
        override fun run(): Result<CellAddress, ErrorReport> {
            return Ok(cellAddress)
        }
    }

    data class WbKeyUnit(val wbKey: WorkbookKey) : ExUnit {
        override fun run(): Result<WorkbookKey, ErrorReport> {
            return Ok(wbKey)
        }
    }

    object Nothing : ExUnit {
        override fun run(): Result<Unit, ErrorReport> {
            return Ok(Unit)
        }
    }

    companion object {
        val TRUE = BoolUnit(true)
        val FALSE = BoolUnit(false)

        fun CellAddress.exUnit(): CellAddressUnit {
            return CellAddressUnit(this)
        }

        fun RangeAddress.exUnit(): RangeAddressUnit {
            return RangeAddressUnit(this)
        }

        fun String.exUnit(): StrUnit {
            return StrUnit(this)
        }

        fun Int.exUnit(): IntNum {
            return IntNum(this)
        }

        fun Double.exUnit(): DoubleNum {
            return DoubleNum(this)
        }

        fun Float.exUnit(): DoubleNum {
            return DoubleNum(this.toDouble())
        }

        fun Boolean.exUnit(): BoolUnit {
            return BoolUnit(this)
        }

        fun WorkbookKey.exUnit(): WbKeyUnit {
            return WbKeyUnit(this)
        }

        /**
         * extract value from a variable [r1], if it is a cell, return the value inside, otherwise, return itself.
         * @param defaultValue: default value for when [r1] is a cell and empty
         */
        private fun extractR(r1: Any,defaultValue:Any = 0): Any {
            val trueR1 = when (r1) {
                is Cell -> r1.valueAfterRun ?: defaultValue
                is Range -> {
                    if (r1.isCell) {
                        r1.cells[0].valueAfterRun ?: defaultValue
                    } else {
                        r1
                    }
                }
                else -> r1
            }
            return trueR1
        }
    }

    /**
     * ExUnit for "+" operator
     */
    data class AddOperator(val u1: ExUnit, val u2: ExUnit) : ExUnit {
        override fun run(): Result<Any, ErrorReport> {
            val r1Rs = u1.run()
            val rt: Result<Any, ErrorReport> = r1Rs.andThen { r1 ->
                val r2Rs = u2.run()
                r2Rs.andThen { r2 ->
                    val trueR1 = extractR(r1)
                    val trueR2 = extractR(r2,"")
                    when {
                        trueR1 is Number && trueR2 is Number -> return Ok(trueR1.toDouble() + (trueR2.toDouble()))
                        trueR1 is String -> {
                            if(trueR2 is Number){
                                val db = trueR2.toDouble()
                                val i = db.toInt()
                                val isInt = db.toInt().toDouble() == db
                                if(isInt){
                                    return Ok(trueR1 + i.toString())
                                }
                            }
                            return Ok(trueR1 + trueR2.toString())
                        }
                        else -> return ExUnitErrors.IncompatibleType.report(
                            "Expect two numbers or first argument as text, but got ${trueR1::class.simpleName} and ${trueR2::class.simpleName}"
                        ).toErr()
                    }
                }
            }
            return rt
        }
    }

    /**
     * ExUnit for "-" operator
     */
    data class MinusOperator(val u1: ExUnit, val u2: ExUnit) : ExUnit {
        override fun run(): Result<Double, ErrorReport> {
            val r1Rs = u1.run()
            val rt = r1Rs.andThen { r1 ->
                val r2Rs = u2.run()
                r2Rs.andThen { r2 ->
                    val trueR1 = extractR(r1)
                    val trueR2 = extractR(r2)
                    if (trueR1 is Number && trueR2 is Number) {
                        Ok(trueR1.toDouble() - (trueR2.toDouble()))
                    } else {
                        ExUnitErrors.IncompatibleType.report(
                            "Expect two numbers, but got ${trueR1::class.simpleName} and ${trueR2::class.simpleName}"
                        ).toErr()
                    }
                }
            }
            return rt
        }
    }

    /**
     * ExUnit for "*" operator
     */
    data class MultiplyOperator(val u1: ExUnit, val u2: ExUnit) : ExUnit {
        override fun run(): Result<Double, ErrorReport> {
            val r1Rs = u1.run()
            val rt = r1Rs.andThen { r1 ->
                val r2Rs = u2.run()
                r2Rs.andThen { r2 ->
                    val trueR1 = extractR(r1)
                    val trueR2 = extractR(r2)
                    if (trueR1 is Number && trueR2 is Number) {
                        Ok(trueR1.toDouble() * (trueR2.toDouble()))
                    } else {
                        ExUnitErrors.IncompatibleType.report(
                            "Expect two numbers, but got ${trueR1::class.simpleName} and ${trueR2::class.simpleName}"
                        ).toErr()
                    }
                }
            }
            return rt
        }
    }

    /**
     * ExUnit for "/" operator
     */
    data class Div(val u1: ExUnit, val u2: ExUnit) : ExUnit {
        override fun run(): Result<Double, ErrorReport> {
            val r1Rs = u1.run()
            val rt = r1Rs.andThen { r1 ->
                val r2Rs = u2.run()
                r2Rs.andThen { r2 ->
                    val trueR1 = extractR(r1)
                    val trueR2 = extractR(r2)
                    if (trueR1 is Number && trueR2 is Number) {
                        try {
                            val result = trueR1.toDouble() / (trueR2.toDouble())
                            Ok(result)
                        } catch (e: Throwable) {
                            CommonErrors.ExceptionError.report(e).toErr()
                        }
                    } else {
                        ExUnitErrors.IncompatibleType.report(
                            "Expect two numbers, but got ${trueR1::class.simpleName} and ${trueR2::class.simpleName}"
                        ).toErr()
                    }
                }
            }
            return rt
        }
    }

    /**
     * ExUnit for "^" operator
     */
    data class PowerBy(val u1: ExUnit, val u2: ExUnit) : ExUnit {
        override fun run(): Result<Double, ErrorReport> {
            val r1Rs = u1.run()
            val rt = r1Rs.andThen { r1 ->
                val r2Rs = u2.run()
                r2Rs.andThen { r2 ->
                    val trueR1 = extractR(r1)
                    val trueR2 = extractR(r2)
                    if (trueR1 is Number && trueR2 is Number) {
                        Ok(trueR1.toDouble().pow(trueR2.toDouble()))
                    } else {
                        ExUnitErrors.IncompatibleType.report("Expect two numbers, but got ${trueR1::class.simpleName} and ${trueR2::class.simpleName}")
                            .toErr()
                    }
                }
            }
            return rt
        }
    }

    /**
     * ExUnit for unary "-"
     */
    data class UnarySubtract(val u: ExUnit) : ExUnit {
        override fun run(): Result<Double, ErrorReport> {
            val runRs = u.run()
            val rt = runRs.andThen { rs ->
                val trueR = extractR(rs)
                val negated = when (trueR) {
                    is Int -> Ok(-trueR.toDouble())
                    is Double -> Ok(-trueR)
                    is Float -> Ok(-trueR.toDouble())
                    else -> ExUnitErrors.IncompatibleType.report("Expect a number, but got ${trueR::class.simpleName}")
                        .toErr()
                }
                negated
            }
            return rt
        }
    }

    sealed class NumberUnit(val v: Number) : ExUnit {
        override fun run(): Result<Number, ErrorReport> {
            return Ok(v)
        }
    }

    data class DoubleNum(val _v: Double) : NumberUnit(_v) {
        override fun run(): Result<Double, ErrorReport> {
            return Ok(v as Double)
        }
    }
    data class IntNum(val _v: Int) : NumberUnit(_v) {
        override fun run(): Result<Int, ErrorReport> {
            return Ok(this.v as Int)
        }
    }
    @JvmInline
    value class StrUnit(val v: String) : ExUnit {
        override fun run(): Result<String, ErrorReport> {
            return Ok(v)
        }
    }

    @JvmInline
    value class BoolUnit(val v: Boolean) : ExUnit {
        override fun run(): Result<Boolean, ErrorReport> {
            return Ok(v)
        }
    }

    data class Func(
        val funcName: String,
        val args: List<ExUnit>,
        val functionMap: FunctionMap,
    ) : ExUnit {
        @Suppress("UNCHECKED_CAST")
        override fun run(): Result<Any, ErrorReport> {
            val argValueRs = (args.map { it.run() }.toTypedArray())
            val funcRs = functionMap.getFuncRs(funcName)
            when (funcRs) {
                is Ok -> {
                    val funcDef = funcRs.value
                    val func = funcDef.function
                    val executionWay: ExecutionWay = funcDef.executionWay ?: ExecutionWay.Default
                    val errs: List<ErrorReport> =
                        argValueRs.filterIsInstance<Err<ErrorReport>>().map { it.component2() }
                    if (errs.isNotEmpty()) {
                        return CommonErrors.MultipleErrors.report(errs).toErr()
                    } else {
                        val argValues = argValueRs.map { it.component1() }.toTypedArray()
                        try {
                            val funcOutput = executionWay.execute(func, argValues)
                            return funcOutput as Result<Any, ErrorReport>
                        } catch (e: Exception) {
                            return CommonErrors.ExceptionError.report(e).toErr()
                        }
                    }
                }
                is Err -> {
                    return funcRs
                }
            }
        }
    }
}

/**
 * Define how a KFunction should be called. Most of the time, the default is ok
 */
interface ExecutionWay {
    fun execute(func: KFunction<Any>, args: Array<Any?>): Any

    object Default : ExecutionWay {
        override fun execute(func: KFunction<Any>, args: Array<Any?>): Any {
            return func.call(*args)
        }
    }
}
