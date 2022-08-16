package test.integration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.d.CellContentImp
import com.emeraldblast.p6.app.document.cell.d.CellImp
import com.emeraldblast.p6.app.document.cell.d.CellValue
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookImp
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.worksheet.WorksheetImp
import com.emeraldblast.p6.translator.formula.FunctionMapImp
import com.emeraldblast.p6.translator.formula.P6FunctionDefinitionsImp
import com.emeraldblast.p6.translator.jvm_translator.JvmFormulaTranslator
import com.emeraldblast.p6.translator.jvm_translator.JvmFormulaVisitor
import com.emeraldblast.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp
import com.emeraldblast.p6.ui.common.compose.MsUtils.toMs
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction
import test.TestSample
import kotlin.test.*

class IntegrationTest {

    lateinit var testSample: TestSample
    val appState get() = testSample.appState

    @BeforeTest
    fun b() {
        testSample = TestSample()
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
        val appMs = testSample.sampleAppStateMs()
        var wbCont by appMs.value.globalWbContMs
        val wbKey = WorkbookKey("b1").toMs()
        val wsName = "S1"
        val translator = JvmFormulaTranslator(
            visitor = JvmFormulaVisitor(
                wbKey = wbKey.value,
                wsName = wsName,
                functionMap = FunctionMapImp(P6FunctionDefinitionsImp(appMs).functionMap)
            ),
            treeExtractor = TreeExtractorImp()
        )
        val wb = WorkbookImp(
            keyMs = wbKey,
            worksheetMsList = listOf(
                WorksheetImp("S1".toMs(), wbKeySt = wbKey).let { ws ->
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
        wbCont = wbCont.addWb(wb)!!

        val wb2 = wb.reRun()
        assertEquals(6.0, wb2.getWs(wsName)!!.getCellOrNull(CellAddress("C1"))!!.cellValueAfterRun.valueAfterRun)


        val newWs = wb2.getWs(wsName)!!.removeCell(CellAddress("A2"))
        val wb3 = wb2.addSheetOrOverwrite(newWs).reRun()
        wbCont = wbCont.overwriteWB(wb3)
        val wb4 = wb3.reRun()

        assertEquals(4.0, wb4.getWs(wsName)!!.getCellOrNull(CellAddress("C1"))!!.cellValueAfterRun.valueAfterRun)
    }
}
