package com.qxdzbc.p6.translator.jvm_translator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.document.cell.FormulaErrors
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.CellContentImp
import com.qxdzbc.p6.app.document.cell.d.CellImp
import com.qxdzbc.p6.app.document.cell.d.CellValue.Companion.toCellValue
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.FunctionMapImp
import com.qxdzbc.p6.translator.formula.P6FunctionDefinitionsImp
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.StateUtils.toSt
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import kotlin.test.*
import com.github.michaelbull.result.Result
import test.TestSample

class JvmFormulaTranslator_Integration_Test {
    lateinit var testSample: TestSample
    lateinit var functionMap: FunctionMap
    lateinit var translator: JvmFormulaTranslator
    val wbKey: WorkbookKey = TestSample.wbk1
    val wsName = "Sheet1"
    lateinit var appStateMs: Ms<AppState>
    lateinit var p6FunctDefs: P6FunctionDefinitionsImp
    val wbCont: WorkbookContainer get() = testSample.appState.globalWbContMs.value

    @BeforeTest
    fun b() {
        testSample = TestSample()
        val wbl = listOf(
            WorkbookImp(
                keyMs = TestSample.wbk1.toMs(),
            ).addMultiSheetOrOverwrite(
                listOf(
                    WorksheetImp("Sheet1".toMs(), testSample.wbKey2Ms)
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
                    WorksheetImp("Sheet2".toMs(), testSample.wbKey2Ms)
                )
            ),
            WorkbookImp(
                keyMs = testSample.wbKey2Ms,
            ).addMultiSheetOrOverwrite(
                listOf<Worksheet>(
                    WorksheetImp("Sheet1".toMs(), testSample.wbKey2Ms),
                    WorksheetImp("Sheet2".toMs(), testSample.wbKey2Ms)
                )
            )
        )

        var wbc by testSample.appState.globalWbContMs
        wbc = wbc.removeAll()
        wbc = wbl.fold(wbc) { acc, wb ->
            acc.addWb(wb)
        }

        appStateMs = testSample.sampleAppStateMs(wbCont)
        p6FunctDefs = P6FunctionDefinitionsImp(appStateMs)

        fun add(n1: Int, n2: Int): Result<Int, ErrorReport> {
            return Ok(n1 + n2)
        }

        fun toUpper(str: String): Result<String, ErrorReport> {
            return Ok(str.uppercase())
        }

        functionMap = FunctionMapImp(
            p6FunctDefs.functionMap
        )
        translator = JvmFormulaTranslator(
            treeExtractor = TreeExtractorImp(),
            visitor = JvmFormulaVisitor2(
                wbKeySt = wbKey.toSt(),
                wsNameSt = wsName.toSt(),
                functionMap = functionMap
            )
        )
    }

    @Test
    fun `translate call functions on real app`() {
        val inputMap: Map<String, Result<Any, ErrorReport>> = mapOf(
            "=A1" to appStateMs.value.getCellRs(wbKey, wsName, CellAddress("A1")),
            "=A1:B3@Sheet2" to appStateMs.value.getRangeRs(wbKey, "Sheet2", RangeAddress("A1:B3")),
            "=A1:B3@'Sheet 13'" to appStateMs.value.getRangeRs(wbKey, "Sheet 13", RangeAddress("A1:B3")),
            "=SUM(A1:C3)" to Ok(1.0 + 2 + 3),
            "=SUM(A1:D4)" to Err(FormulaErrors.InvalidFunctionArgument.report("")),
        )
        for ((i, expect) in inputMap) {
            val rs = translator.translate(i)
            assertTrue { rs is Ok }
            val oRs = rs.component1()!!.run()
            if (expect is Ok) {
                assertEquals(expect, oRs)
            } else {
                val o = oRs.component2()!!
                assertTrue(expect.component2()!!.isType(o))
            }
        }
    }
}
