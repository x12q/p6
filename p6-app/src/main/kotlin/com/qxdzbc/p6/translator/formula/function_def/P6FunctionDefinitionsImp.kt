package com.qxdzbc.p6.translator.formula.function_def

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.FormulaErrors
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

import com.qxdzbc.p6.translator.formula.execution_unit.function.FunctionExecutor
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import javax.inject.Inject
import kotlin.reflect.KFunction

class P6FunctionDefinitionsImp @Inject constructor(
    private val docCont: DocumentContainer,
) : P6FunctionDefinitions {

    /**
     * A list of internal function for getting wb, ws, range, cell
     */
    private val documentFunctions = listOf(
        object : AbstractFunctionDef() {
            fun getLazyRangeRs(
                wbKeySt: St<WorkbookKey>,
                wsNameSt: St<String>,
                rangeAddress: RangeAddress
            ): Rse<Range> {
                return docCont.getLazyRangeRs(wbKeySt, wsNameSt, rangeAddress)
            }
            override val name: String = P6FunctionDefinitions.getRangeRs
            override val function: KFunction<Rse<Range>> = ::getLazyRangeRs
        },
        object : AbstractFunctionDef() {
            fun getCellRs(
                wbKeySt: St<WorkbookKey>,
                wsNameSt: St<String>,
                cellAddress: CellAddress
            ): Rse<St<Cell>?> {
                val rt= docCont.getWsMsRs(wbKeySt, wsNameSt).map {
                    it.value.getCellMs(cellAddress)
                }
                return rt
            }

            override val name: String = P6FunctionDefinitions.getCellRs
            override val function: KFunction<Rse<St<Cell>?>> = ::getCellRs
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
            override val functionExecutor: FunctionExecutor = FunctionExecutor.ArgsAsList

            /**
             * SUM accepts a mix list of Cell, Range, and Number, remember to check for type before doing anything
             */
            fun SUM(inputList: List<Any?>): Rse<Double> {
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
            override val function: KFunction<Rse<Double>> = ::SUM
        },
    ) + documentFunctions

    override val functionMap: Map<String, FunctionDef> = all.associateBy(
        keySelector = { it.name },
        valueTransform = { it }
    )
}
