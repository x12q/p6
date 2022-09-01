package com.qxdzbc.p6.translator.jvm_translator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.FormulaErrors
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.CellContentImp
import com.qxdzbc.p6.app.document.cell.d.CellImp
import com.qxdzbc.p6.app.document.cell.d.CellValue.Companion.toCellValue
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.FunctionMapImp
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitionsImp
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp
import com.qxdzbc.p6.ui.app.state.AppState
import test.TestSample
import kotlin.test.*

class JvmFormulaTranslator_Integration_Test {
    lateinit var ts: TestSample
    lateinit var functionMap: Ms<FunctionMap>
    lateinit var translator: JvmFormulaTranslator
    val wbKey: WorkbookKey get() = ts.wbKey1
    val wsName get() = ts.wsn1

    val wbKeySt get() = ts.wbKey1Ms
    val wsNameSt get() = ts.appState.docCont.getWsNameSt(wbKeySt, wsName)!!

    lateinit var appStateMs: Ms<AppState>
    lateinit var p6FunctDefs: P6FunctionDefinitionsImp
    val wbCont: WorkbookContainer get() = ts.appState.globalWbContMs.value

    @BeforeTest
    fun b() {
        ts = TestSample()
        val wbl: List<Workbook> = listOf(
            WorkbookImp(
                keyMs = ts.wbKey1Ms,
            ).addMultiSheetOrOverwrite(
                listOf(
                    WorksheetImp("Sheet1".toMs(), ts.wbKey2Ms)
                        .addOrOverwrite(
                            CellImp(
                                address = CellAddress("A1"),
                                content = CellContentImp(1.0.toCellValue().toMs())
                            )
                        )
                        .addOrOverwrite(
                            CellImp(
                                address = CellAddress("B2"),
                                content = CellContentImp(2.0.toCellValue().toMs())
                            )
                        )
                        .addOrOverwrite(
                            CellImp(
                                address = CellAddress("C3"),
                                content = CellContentImp(3.0.toCellValue().toMs())
                            )
                        ).addOrOverwrite(
                            CellImp(
                                address = CellAddress("D4"),
                                content = CellContentImp("abc".toCellValue().toMs())
                            )
                        ),
                    WorksheetImp("Sheet2".toMs(), ts.wbKey2Ms)
                )
            ),
            WorkbookImp(
                keyMs = ts.wbKey2Ms,
            ).addMultiSheetOrOverwrite(
                listOf<Worksheet>(
                    WorksheetImp("Sheet1".toMs(), ts.wbKey2Ms),
                    WorksheetImp("Sheet2".toMs(), ts.wbKey2Ms)
                )
            )
        )

        ts.appState
        var wbc: WorkbookContainer by ts.appState.globalWbContMs
        wbc = wbc.removeAll()
        wbc = wbl.fold(wbc) { acc, wb ->
            acc.addWb(wb)
        }

        appStateMs = ts.sampleAppStateMs(wbCont)
        p6FunctDefs = P6FunctionDefinitionsImp(
            appStateMs, ts.appState.docContMs,
        )
        fun add(n1: Int, n2: Int): Result<Int, ErrorReport> {
            return Ok(n1 + n2)
        }

        fun toUpper(str: String): Result<String, ErrorReport> {
            return Ok(str.uppercase())
        }

        functionMap = FunctionMapImp(
            p6FunctDefs.functionMap
        ).toMs()
        translator = JvmFormulaTranslator(
            treeExtractor = TreeExtractorImp(),
            visitor = JvmFormulaVisitor(
                wbKeySt = wbKeySt,
                wsNameSt = wsNameSt,
                functionMapMs = functionMap,
                docContMs = ts.appState.docContMs
            )
        )
    }

    @Test
    fun `translate call functions on real app`() {
        val inputMap: Map<String, Result<Any, ErrorReport>> = mapOf(
            "=A1" to appStateMs.value.getCellRs(wbKey, wsName, CellAddress("A1")),
            "=A1:B3@Sheet2" to appStateMs.value.getRangeRs(wbKey, "Sheet2", RangeAddress("A1:B3")),
            "=A1:B3@'Sheet not exist'" to appStateMs.value.getRangeRs(wbKey, "Sheet not exist", RangeAddress("A1:B3")),
            "=SUM(A1:C3)" to Ok(1.0 + 2 + 3),
            "=SUM(A1:D4)" to Err(FormulaErrors.InvalidFunctionArgument.report("")),
        )
        for ((i, expect) in inputMap) {
            val rs = translator.translate(i)
            assertTrue { rs is Ok }
            val u = rs.component1()
            assertNotNull(u)
            val oRs = u.run()
            if (expect is Ok) {
                assertEquals(expect, oRs)
            } else {
                val o = oRs.component2()!!
                assertTrue(expect.component2()!!.isType(o))
            }
//            assertEquals(i,"="+u.toFormula())
        }
    }
}
