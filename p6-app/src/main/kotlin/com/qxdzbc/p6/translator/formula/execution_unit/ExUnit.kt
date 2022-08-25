package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.qxdzbc.common.Rs
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.common.utils.Utils.toTF
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.function_def.FunctionDef
import com.qxdzbc.p6.translator.formula.FunctionMap
import kotlin.math.pow
import kotlin.reflect.KFunction

/**
 * An ExUnit (execution unit) is a provider obj that when run will return something that can be stored by a Cell
 *
 */
interface ExUnit : Shiftable {
    /**
     * Perform a shift on all shift-able element (range, cell)
     */
    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): ExUnit

    /**
     * convert exUnit to a formula
     */
    fun toFormula(): String?

    /**
     * when this run, it returns something
     */
    fun run(): Result<Any, ErrorReport>

    data class RangeAddressUnit(val rangeAddress: RangeAddress) : ExUnit {
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this.copy(rangeAddress = rangeAddress.shift(oldAnchorCell, newAnchorCell))
        }

        override fun toFormula(): String {
            return rangeAddress.label
        }

        override fun run(): Result<RangeAddress, ErrorReport> {
            return Ok(rangeAddress)
        }
    }

    data class CellAddressUnit(val cellAddress: CellAddress) : ExUnit {
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this.copy(cellAddress = cellAddress.shift(oldAnchorCell, newAnchorCell))
        }

        override fun toFormula(): String? {
            return cellAddress.toLabel()
        }

        override fun run(): Result<CellAddress, ErrorReport> {
            return Ok(cellAddress)
        }
    }
    data class WbKeyStUnit(val wbKeySt: St<WorkbookKey>) : ExUnit {
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this
        }

        override fun toFormula(): String {
            val p = wbKeySt.value.path
            if (p != null) {
                return "@\'${wbKeySt.value.name}\'@\'${p.toAbsolutePath()}\'"
            } else {
                return "@\'${wbKeySt.value.name}\'"
            }
        }

        override fun run(): Result<St<WorkbookKey>, ErrorReport> {
            return Ok(wbKeySt)
        }
    }

    data class WbKeyUnit(val wbKey: WorkbookKey) : ExUnit {
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this
        }

        override fun toFormula(): String? {
            val p = wbKey.path
            if (p != null) {
                return "@${wbKey.name}@${p.toAbsolutePath()}"
            } else {
                return "@${wbKey.name}"
            }
        }

        override fun run(): Result<WorkbookKey, ErrorReport> {
            return Ok(wbKey)
        }
    }

    object Nothing : ExUnit {
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this
        }

        override fun toFormula(): String? {
            return null
        }

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
        fun St<WorkbookKey>.exUnit():WbKeyStUnit{
            return WbKeyStUnit(this)
        }

        /**
         * extract value from a variable [r1], if it is a cell, return the value inside, otherwise, return itself.
         * @param defaultValue: default value for when [r1] is a cell and empty
         */
        private fun extractR(r1: Any, defaultValue: Any = 0): Any {
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
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this.copy(
                u1 = u1.shift(oldAnchorCell, newAnchorCell),
                u2 = u2.shift(oldAnchorCell, newAnchorCell)
            )
        }

        override fun toFormula(): String? {
            val f1 = u1.toFormula()
            val f2 = u1.toFormula()
            if (f1 != null && f2 != null) {
                return "${f1} + ${f2}"
            } else {
                return null
            }
        }

        override fun run(): Result<Any, ErrorReport> {
            val r1Rs = u1.run()
            val rt: Result<Any, ErrorReport> = r1Rs.andThen { r1 ->
                val r2Rs = u2.run()
                r2Rs.andThen { r2 ->
                    val trueR1 = extractR(r1)
                    val trueR2 = extractR(r2, "")
                    when {
                        trueR1 is Number && trueR2 is Number -> return Ok(trueR1.toDouble() + (trueR2.toDouble()))
                        trueR1 is String -> {
                            if (trueR2 is Number) {
                                val db = trueR2.toDouble()
                                val i = db.toInt()
                                val isInt = db.toInt().toDouble() == db
                                if (isInt) {
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
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this.copy(
                u1 = u1.shift(oldAnchorCell, newAnchorCell),
                u2 = u2.shift(oldAnchorCell, newAnchorCell)
            )
        }

        override fun toFormula(): String? {
            val f1 = u1.toFormula()
            val f2 = u1.toFormula()
            if (f1 != null && f2 != null) {
                return "${f1} - ${f2}"
            } else {
                return null
            }
        }

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
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this.copy(
                u1 = u1.shift(oldAnchorCell, newAnchorCell),
                u2 = u2.shift(oldAnchorCell, newAnchorCell)
            )
        }

        override fun toFormula(): String? {
            val f1 = u1.toFormula()
            val f2 = u1.toFormula()
            if (f1 != null && f2 != null) {
                return "${f1} * ${f2}"
            } else {
                return null
            }
        }

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
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this.copy(
                u1 = u1.shift(oldAnchorCell, newAnchorCell),
                u2 = u2.shift(oldAnchorCell, newAnchorCell)
            )
        }

        override fun toFormula(): String? {
            val f1 = u1.toFormula()
            val f2 = u1.toFormula()
            if (f1 != null && f2 != null) {
                return "${f1} / ${f2}"
            } else {
                return null
            }
        }

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
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this.copy(
                u1 = u1.shift(oldAnchorCell, newAnchorCell),
                u2 = u2.shift(oldAnchorCell, newAnchorCell)
            )
        }

        override fun toFormula(): String? {
            val f1 = u1.toFormula()
            val f2 = u1.toFormula()
            if (f1 != null && f2 != null) {
                return "${f1} ^ ${f2}"
            } else {
                return null
            }
        }

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
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this.copy(u = u.shift(oldAnchorCell, newAnchorCell))
        }

        override fun toFormula(): String? {
            val f1 = u.toFormula()
            if (f1 != null) {
                return "-${f1}"
            } else {
                return null
            }
        }

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

        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this
        }

        override fun toFormula(): String? {
            return _v.toString()
        }
    }

    data class IntNum(val _v: Int) : NumberUnit(_v) {
        override fun run(): Result<Int, ErrorReport> {
            return Ok(this.v as Int)
        }

        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this
        }

        override fun toFormula(): String? {
            return this._v.toString()
        }
    }

    @JvmInline
    value class StrUnit(val v: String) : ExUnit {
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this
        }

        override fun toFormula(): String? {
            return "\"${v}\""
        }

        override fun run(): Result<String, ErrorReport> {
            return Ok(v)
        }
    }

    @JvmInline
    value class WsNameStUnit(val nameSt: St<String>) : ExUnit {
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this
        }

        override fun toFormula(): String {
            return "@\'${nameSt.value}\'"
        }

        override fun run(): Result<St<String>, ErrorReport> {
            return Ok(nameSt)
        }
    }

    @JvmInline
    value class BoolUnit(val v: Boolean) : ExUnit {
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            return this
        }

        override fun toFormula(): String? {
            return v.toTF()
        }

        override fun run(): Result<Boolean, ErrorReport> {
            return Ok(v)
        }
    }

    /**
     * [isImplicit]: an implicit function is a function that is disguised under non-function form, such as get range syntax.
     */
    data class Func(
        val funcName: String,
        val args: List<ExUnit>,
        val functionMap: FunctionMap,
        val isImplicit:Boolean = false,
    ) : ExUnit {
        override fun shift(
            oldAnchorCell: GenericCellAddress<Int, Int>,
            newAnchorCell: GenericCellAddress<Int, Int>
        ): ExUnit {
            val newArgs = args.map { it.shift(oldAnchorCell, newAnchorCell) }
            return this.copy(args = newArgs)
        }

        override fun toFormula(): String? {
//            functionMap.getFunc(funcName)?.let {
//                funcDef->
//                funcDef
//            }

            val argsStr = args.map { it.toFormula() }.filterNotNull().joinToString(", ")
            if(isImplicit){
                return argsStr
            }else{
                return "${funcName}(${argsStr})"
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun run(): Result<Any, ErrorReport> {
            val argValueRs: Array<Result<Any, ErrorReport>> = (args.map { it.run() }.toTypedArray())
            val funcRs:Rs<FunctionDef,ErrorReport> = functionMap.getFuncRs(funcName)
            when (funcRs) {
                is Ok -> {
                    val funcDef: FunctionDef = funcRs.value
                    val func: KFunction<Any> = funcDef.function
                    val functionExecutor: FunctionExecutor = funcDef.functionExecutor ?: FunctionExecutor.Default
                    val errs: List<ErrorReport> =
                        argValueRs.filterIsInstance<Err<ErrorReport>>().map { it.component2() }
                    if (errs.isNotEmpty()) {
                        return CommonErrors.MultipleErrors.report(errs).toErr()
                    } else {
                        val argValues: Array<Any?> = argValueRs
                            .map { it.component1() }
                            .toTypedArray()
                        try {
                            val funcOutput = functionExecutor.execute(func, argValues)
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

