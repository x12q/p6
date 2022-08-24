package com.qxdzbc.p6.translator.formula

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.app.document.cell.FormulaErrors
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.translator.formula.execution_unit.ExecutionWay
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.*
import javax.inject.Inject
import kotlin.reflect.KFunction

class P6FunctionDefinitionsImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>
) : P6FunctionDefinitions {

    private var appState by appStateMs

    internal val documentFunctions = listOf(
        object : AbstractFunctionDef() {
            override val name: String = P6FunctionDefinitions.getSheetRs
            override val function: KFunction<Any> = appState::getWsRs
        },
        object : AbstractFunctionDef() {
            override val name: String = P6FunctionDefinitions.getRangeRs
//            override val function: KFunction<Any> = appState::getRangeRs
            override val function: KFunction<Any> = appState::getLazyRangeRs
        },
        object : AbstractFunctionDef() {
            override val name: String = P6FunctionDefinitions.getCellRs
            override val function: KFunction<Any> = appState::getCellRs
        }
    )

    internal val all = listOf<FunctionDef>(
        object : AbstractFunctionDef() {
            override val executionWay: ExecutionWay = object : ExecutionWay {
                override fun execute(func: KFunction<Any>, args: Array<Any?>): Any {
                    return func.call(args.toList())
                }
            }

            /**
             * SUM accept a mix list of Cell, Range, and Number
             */
            fun SUM(inputList: List<Any?>): Result<Double, ErrorReport> {
                var d: Double = 0.0
                if(inputList.isNotEmpty()){
                    val invalidArgumentReport = FormulaErrors.InvalidFunctionArgument.report("SUM function only accept numbers and numeric cells.").toErr()
                    for (obj in inputList) {
                        if(obj!=null){
                            when(obj){
                                is Number ->{
                                    d+=(obj.toDouble())
                                }
                                is Cell ->{
                                    val cv = obj.valueAfterRun
                                    try {
                                        d += (cv as Double)
                                    } catch (e: Throwable) {
                                        when (e) {
                                            is ClassCastException -> {
                                                return invalidArgumentReport
                                            }
                                            else -> return FormulaErrors.Unknown.report("Unknown error").toErr()
                                        }
                                    }
                                }
                                is Range->{
                                    for (cell in obj.cells) {
                                        val cv = cell.valueAfterRun
                                        if (cv != null) {
                                            try {
                                                d += (cv as Double)
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
                return Ok(d)
            }
            override val name = "SUM"
            override val function: KFunction<Any> = ::SUM
        },
    ) + documentFunctions

    override val functionMap: Map<String, FunctionDef> = all.associateBy(
        keySelector = { it.name },
        valueTransform = { it }
    )
}
