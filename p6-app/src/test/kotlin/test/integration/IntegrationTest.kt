package test.integration

import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellRequest
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.IndCellImp
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.translator.formula.FunctionMapImp
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitionsImp
import com.qxdzbc.p6.translator.jvm_translator.ExUnitFormulaTranslator
import com.qxdzbc.p6.translator.jvm_translator.ExUnitFormulaVisitor
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp
import test.TestSample
import kotlin.test.*

class IntegrationTest {

    lateinit var ts: TestSample
    val appState get() = ts.appState
    val sc get() = ts.sc

    @BeforeTest
    fun b() {
        ts = TestSample()
    }

    /**
     * input "=A1" into A1 => receive an error message inside the cell
     * input "=A1+1" into B1 => error msg in A1 should remain the same
     * delete B1 => error msg in A1 should remain the same
     */
    @Test
    fun `bug case- cell error should not change during worksheet operation`() {
        val updateCellAct = ts.comp.updateCellAction()
        val deleteMultiCellAction: DeleteMultiCellAction = ts.comp.deleteMultiCellAction()
        val wbws = WbWs(ts.wbKey1, ts.wsn1)
//        ColdInit()
        updateCellAct.updateCellDM(
            request = CellUpdateRequestDM(
                cellId = CellIdDM(CellAddress("A1"), wbws),
                cellContent = CellContentDM.fromFormula("=A1")
            ),
            publishError = false
        )
        val cell1 = sc.getCellOrDefault(wbws, CellAddress("A1"))!!

        val originalErrMsg = cell1.cachedDisplayText
        cell1.valueAfterRun
        val ms2 = cell1.cachedDisplayText
        assertEquals(ms2, originalErrMsg)

        updateCellAct.updateCellDM(
            request = CellUpdateRequestDM(
                cellId = CellIdDM(CellAddress("B1"), wbws),
                cellContent = CellContentDM.fromFormula("=A1+1")
            ),
            publishError = false
        )
        val cell2 = sc.getCellOrDefault(wbws, CellAddress("A1"))!!
        assertEquals(originalErrMsg, cell2.cachedDisplayText)

        deleteMultiCellAction.deleteDataOfMultiCell(
            DeleteMultiCellRequest(
                cells = listOf(CellAddress("B1")),
                wbKey = wbws.wbKey,
                wsName = wbws.wsName
            ),
            undoable = true
        )
        assertEquals(originalErrMsg, sc.getCellOrDefault(wbws, CellAddress("A1"))!!.cachedDisplayText)
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
    fun `test cell value update after removing dependent cell`() {
        val appState = ts.sampleAppState()
        val wbCont = ts.sc.wbCont
        val wbKeySt = WorkbookKey("Wb1").toMs()
        val wsNameSt = "S1".toMs()
        val translator = ExUnitFormulaTranslator(
            visitor = ExUnitFormulaVisitor(
                wbKeySt = wbKeySt,
                wsNameSt = wsNameSt,
                functionMapMs = FunctionMapImp(
                    P6FunctionDefinitionsImp(
                        docCont = appState.documentContainer,
                    ).functionMap
                ).toMs(),
                appState.documentContainer
            ),
            treeExtractor = TreeExtractorImp()
        )
        val wb = WorkbookImp(
            keyMs = wbKeySt,
            worksheetMsList = listOf(
                WorksheetImp(wsNameSt, wbKeySt = wbKeySt).apply {
                    addOrOverwrite(
                        IndCellImp(
                            address = CellAddress("A1"),
                            content = CellContentImp(
                                cellValueMs = CellValue.from(1).toMs()
                            )
                        )
                    )
                    addOrOverwrite(
                        IndCellImp(
                            address = CellAddress("A2"),
                            content = CellContentImp(
                                cellValueMs = CellValue.from(2).toMs()
                            )
                        )
                    )
                    addOrOverwrite(
                        IndCellImp(
                            address = CellAddress("A3"),
                            content = CellContentImp(
                                cellValueMs = CellValue.from(3).toMs()
                            )
                        )
                    )
                    addOrOverwrite(
                        IndCellImp(
                            address = CellAddress("C1"),
                            content = CellContentImp(
                                exUnit = translator.translate("=SUM(A1:A3)").component1()!!,
                                originalText = "=SUM(A1:A3)"
                            )
                        )
                    )
                }
            ).map { ms(it) }
        )
        wbCont.addWb(wb)

        val wb2 = wb.apply {
            reRun()
        }
        assertEquals(6.0, wb2.getWs(wsNameSt)!!.getCell(CellAddress("C1"))!!.cellValueAfterRun.value)

        val newWs = wb2.getWs(wsNameSt)!!.apply{removeCell(CellAddress("A2"))}
        val wb3 = wb2.apply {
            addSheetOrOverwrite(newWs)
            reRun()
        }
        wbCont.overwriteWb(wb3)
        val wb4 = wb3.apply {
            reRun()
        }

        assertEquals(4.0, wb4.getWs(wsNameSt)!!.getCell(CellAddress("C1"))!!.cellValueAfterRun.value)
    }
}
