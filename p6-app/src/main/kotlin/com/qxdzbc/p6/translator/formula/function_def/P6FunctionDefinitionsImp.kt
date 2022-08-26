package com.qxdzbc.p6.translator.formula.function_def

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.Rs
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.FormulaErrors
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.BackConverterForGetCell
import com.qxdzbc.p6.di.BackConverterForGetRange
import com.qxdzbc.p6.di.NormalBackConverter
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.di.state.app_state.DocumentContainerSt
import com.qxdzbc.p6.translator.formula.execution_unit.FunctionExecutor
import com.qxdzbc.p6.translator.formula.function_def.formula_back_converter.FunctionFormulaBackConverter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import javax.inject.Inject
import kotlin.reflect.KFunction

class P6FunctionDefinitionsImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    @DocumentContainerSt private val docContSt: St<@JvmSuppressWildcards DocumentContainer>,
    @BackConverterForGetRange
    private val f1:FunctionFormulaBackConverter,
    @NormalBackConverter
    private val f2: FunctionFormulaBackConverter,
    @BackConverterForGetCell
    private val f3:FunctionFormulaBackConverter
) : P6FunctionDefinitions {

    private var appState by appStateMs
    private val docCont by docContSt

    /**
     * A list of internal function for getting wb, ws, range, cell
     */
    private val documentFunctions = listOf(
        // this function is currently not need => comment it out
//        object : AbstractFunctionDef() {
//            fun getWsRs(
//                wbkSt: St<WorkbookKey>,
//                wsNameSt: St<String>,
//            ): Rs<Worksheet, ErrorReport> {
//                return docCont.getWsRs(wbkSt, wsNameSt)
//            }
//
//            override val name: String = P6FunctionDefinitions.getSheetRs
//            override val function: KFunction<Any> = ::getWsRs
//        },
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
            override val functionFormulaConverter: FunctionFormulaBackConverter = f1
        },
        object : AbstractFunctionDef() {
            fun getCellRs(
                wbKeySt: St<WorkbookKey>,
                wsNameSt: St<String>,
                cellAddress: CellAddress
            ): Rs<Cell, ErrorReport> {
                return docCont.getCellRs(wbKeySt, wsNameSt, cellAddress)
            }

            override val name: String = P6FunctionDefinitions.getCellRs
            override val function: KFunction<Rs<Cell, ErrorReport>> = ::getCellRs
            override val functionFormulaConverter: FunctionFormulaBackConverter = f3
        }
    )

    /**
     * A list of all available formula function
     */
    internal val all = listOf<FunctionDef>(
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
            override val functionFormulaConverter: FunctionFormulaBackConverter = f2
        },
    ) + documentFunctions

    override val functionMap: Map<String, FunctionDef> = all.associateBy(
        keySelector = { it.name },
        valueTransform = { it }
    )
}
