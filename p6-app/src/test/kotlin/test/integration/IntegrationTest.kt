package test.integration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.CellContentImp
import com.qxdzbc.p6.app.document.cell.d.CellImp
import com.qxdzbc.p6.app.document.cell.d.CellValue
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.p6.translator.formula.FunctionMapImp
import com.qxdzbc.p6.translator.formula.P6FunctionDefinitionsImp
import com.qxdzbc.p6.translator.jvm_translator.JvmFormulaTranslator
import com.qxdzbc.p6.translator.jvm_translator.JvmFormulaVisitor
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.StateUtils.toSt
import com.qxdzbc.common.compose.StateUtils.ms
import test.TestSample
import kotlin.test.*

class IntegrationTest {

    lateinit var ts: TestSample
    val appState get() = ts.appState

    @BeforeTest
    fun b() {
        ts = TestSample()
    }

    /**
     * Scenario: Give a worksheet with 4 cells:
     * A1 = 1
     * A2 = 2
     * A3 = 3
     * C1 = SUM(A1:A3)
     * Remove A2, rerun the worksheet, expect C1 value is correctly computed (which is 1+3 = 3)
     */
    @Test
    fun `test cell auto cell computation update after removing cell`() {
        val appMs = ts.sampleAppStateMs()
        var wbCont by appMs.value.globalWbContMs
        val wbKeySt = WorkbookKey("Wb1").toMs()
        val wsNameSt = "S1".toMs()
        val translator = JvmFormulaTranslator(
            visitor = JvmFormulaVisitor(
                wbKeySt = wbKeySt,
                wsNameSt = wsNameSt,
                functionMap = FunctionMapImp(P6FunctionDefinitionsImp(appMs,appMs.value.docContMs).functionMap),
                appMs.value.docContMs
            ),
            treeExtractor = TreeExtractorImp()
        )
        val wb = WorkbookImp(
            keyMs = wbKeySt,
            worksheetMsList = listOf(
                WorksheetImp(wsNameSt, wbKeySt = wbKeySt).let { ws ->
                    ws.addOrOverwrite(
                        CellImp(
                            address = CellAddress("A1"),
                            content = CellContentImp(
                                cellValueMs = CellValue.from(1).toMs()
                            )
                        )
                    ).addOrOverwrite(
                        CellImp(
                            address = CellAddress("A2"),
                            content = CellContentImp(
                                cellValueMs = CellValue.from(2).toMs()
                            )
                        )
                    ).addOrOverwrite(
                        CellImp(
                            address = CellAddress("A3"),
                            content = CellContentImp(
                                cellValueMs = CellValue.from(3).toMs()
                            )
                        )
                    ).addOrOverwrite(
                        CellImp(
                            address = CellAddress("C1"),
                            content = CellContentImp(
                                formula = "=SUM(A1:A3)",
                                exUnit = translator.translate("=SUM(A1:A3)").component1()!!
                            )
                        )
                    )
                }
            ).map { ms(it) }
        )
        wbCont = wbCont.addWb(wb)

        val wb2 = wb.reRun()
        assertEquals(6.0, wb2.getWs(wsNameSt)!!.getCellOrNull(CellAddress("C1"))!!.cellValueAfterRun.valueAfterRun)


        val newWs = wb2.getWs(wsNameSt)!!.removeCell(CellAddress("A2"))
        val wb3 = wb2.addSheetOrOverwrite(newWs).reRun()
        wbCont = wbCont.overwriteWB(wb3)
        val wb4 = wb3.reRun()

        assertEquals(4.0, wb4.getWs(wsNameSt)!!.getCellOrNull(CellAddress("C1"))!!.cellValueAfterRun.valueAfterRun)
    }
}
