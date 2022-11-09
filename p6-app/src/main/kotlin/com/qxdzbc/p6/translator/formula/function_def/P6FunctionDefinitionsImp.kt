package com.qxdzbc.p6.translator.formula.function_def

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rs
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.FormulaErrors
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

import com.qxdzbc.p6.translator.formula.execution_unit.FunctionExecutor
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import javax.inject.Inject
import kotlin.reflect.KFunction

class P6FunctionDefinitionsImp @Inject constructor(
    private val appStateMs: Ms<AppState>,
    private val docContSt: St<@JvmSuppressWildcards DocumentContainer>,
) : P6FunctionDefinitions {

    private var appState by appStateMs
    private val docCont by docContSt

    /**
     * A list of internal function for getting wb, ws, range, cell
     */
    private val documentFunctions = listOf(
        object : AbstractFunctionDef() {
            fun getLazyRangeRs(
                wbKeySt: St<WorkbookKey>,
                wsNameSt: St<String>,
                rangeAddress: RangeAddress
            ): Rs<Range, ErrorReport> {
                return docCont.getLazyRangeRs(wbKeySt, wsNameSt, rangeAddress)
            }
            override val name: String = P6FunctionDefinitions.getRangeRs
            override val function: KFunction<Rs<Range, ErrorReport>> = ::getLazyRangeRs
        },
        object : AbstractFunctionDef() {
            fun getCellRs(
                wbKeySt: St<WorkbookKey>,
                wsNameSt: St<String>,
                cellAddress: CellAddress
            ): Rs<St<Cell>?, ErrorReport> {
                val rt= docCont.getWsMsRs(wbKeySt, wsNameSt).map {
                    it.value.getCellMs(cellAddress)
                }
                return rt
            }

            override val name: String = P6FunctionDefinitions.getCellRs
            override val function: KFunction<Rs<St<Cell>?, ErrorReport>> = ::getCellRs
        }
    )

    /**
     * A list of all available formula function
     */
    internal val all = listOf<FunctionDef>(
        /**
         * SUM function definition
         */
        object : AbstractFunctionDef() {
            override val functionExecutor: FunctionExecutor = object : FunctionExecutor {
                override fun execute(func: KFunction<Any>, args: Array<Any?>): Any {
                    return func.call(args.toList())
                }
            }

            /**
             * SUM accepts a mix list of Cell, Range, and Number, remember to check for type before doing anything
             */
            fun SUM(inputList: List<Any?>): Result<Double, ErrorReport> {
                var rt: Double = 0.0
                if (inputList.isNotEmpty()) {
                    val invalidArgumentReport =
                        FormulaErrors.InvalidFunctionArgument.report("SUM function only accept numbers and numeric cells.")
                            .toErr()
                    for (obj in inputList) {
                        if (obj != null) {
                            when (obj) {
                                is Number -> {
                                    rt += (obj.toDouble())
                                }
                                is Cell -> {
                                    val cv = obj.valueAfterRun
                                    if(cv!=null){
                                        try {
                                            rt += (cv as Double)
                                        } catch (e: Throwable) {
                                            when (e) {
                                                is ClassCastException -> {
                                                    return invalidArgumentReport
                                                }
                                                else -> return FormulaErrors.Unknown.report("Unknown error").toErr()
                                            }
                                        }
                                    }
                                }
                                is Range -> {
                                    for (cell in obj.cells) {
                                        val cv = cell.valueAfterRun
                                        if (cv != null) {
                                            try {
                                                rt += (cv as Double)
                                            } catch (e: Throwable) {
                                                when (e) {
                                                    is ClassCastException -> {
                                                        return invalidArgumentReport
                                                    }
                                                    else -> return FormulaErrors.Unknown.report("Unknown error").toErr()
                                                }
                                            }
                                        }
                                    }
                                }
                                else -> return invalidArgumentReport
                            }
                        }
                    }
                }
                return Ok(rt)
            }

            override val name = "SUM"
            override val function: KFunction<Result<Double, ErrorReport>> = ::SUM
        },
    ) + documentFunctions

    override val functionMap: Map<String, FunctionDef> = all.associateBy(
        keySelector = { it.name },
        valueTransform = { it }
    )
}
