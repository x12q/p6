package com.emeraldblast.p6.ui.document.workbook.state

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.d.CellContentImp
import com.emeraldblast.p6.app.document.cell.d.CellImp
import com.emeraldblast.p6.app.document.cell.d.CellValue.Companion.toCellValue
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookImp
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.worksheet.WorksheetImp
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState
import org.mockito.kotlin.mock
import test.TestSample
import kotlin.test.*

class WorkbookStateImpTest {
    lateinit var wbState: WorkbookStateImp
    lateinit var wb0: Workbook
    lateinit var wb1: Workbook
    lateinit var testSample:TestSample

    @BeforeTest
    fun b() {
        testSample = TestSample()
        wb0 = WorkbookImp(
            testSample.wbKey2Ms,
        ).addMultiSheetOrOverwrite(
            listOf(
                WorksheetImp("Sheet1_2".toMs(),testSample.wbKey2Ms).let {
                    val z = it
                        .addOrOverwrite(CellImp(CellAddress("A1"), CellContentImp(
                            cellValueMs="a1".toCellValue().toMs())))
                    z
                },
            )
        )


        wb1 = WorkbookImp(
            WorkbookKey("Book2").toMs(),
        ).addMultiSheetOrOverwrite(
            listOf(
                WorksheetImp("Sheet1_2".toMs(),mock()).let {
                    val z = it
                        .addOrOverwrite(CellImp(CellAddress("A1"), CellContentImp(cellValueMs ="a1".toCellValue().toMs())))
                        .addOrOverwrite(CellImp(CellAddress("A2"), CellContentImp(cellValueMs ="a2".toCellValue().toMs())))

                    z
                },
                WorksheetImp("Sheet2_2".toMs(),mock()).let {
                    val z = it
                        .addOrOverwrite(CellImp(CellAddress("B1"), CellContentImp(cellValueMs ="b1".toCellValue().toMs())))
                        .addOrOverwrite(CellImp(CellAddress("B2"), CellContentImp(cellValueMs ="b2".toCellValue().toMs())))

                    z
                }
            )
        )
        val wbContMs = testSample.wbContMs
        wbContMs.value = wbContMs.value.overwriteWB(wb0).overwriteWB(wb1)

        wbState = WorkbookStateImp.default(
            wbMs = wbContMs.value.getWbMs(wb0.key)!!,
            wsStateFactory = testSample.p6Comp.worksheetStateFactory(),
            gridSliderFactory = testSample.p6Comp.gridSliderFactory(),
            cursorStateFactory = testSample.p6Comp.cursorStateFactory(),
        )
    }

    @Test
    fun addCommand() {
        assertTrue { wbState.commandStack.isEmpty() }
        wbState.addCommand(mock())
        assertTrue { wbState.commandStack.isNotEmpty() }
    }

    @Test
    fun setWorkbook() {
        val sheet1_1State:Ms<WorksheetState>? = wbState.getWorksheetStateMs(wb0.getWs(0)!!.name)
        assertNotNull(sheet1_1State)
        sheet1_1State.value.cursorStateMs.value = sheet1_1State.value.cursorState.setMainCell(CellAddress("B9"))
        val cursor1_1 = sheet1_1State.value.cursorState
        val oldTabBarState = wbState.sheetTabBarState
        val wbState2 = wbState.setWorkbookKeyAndRefreshState(wb1.key)
        val l2 = wbState2.worksheetStateList
        assertEquals(2, l2.size)
        assertEquals(wb1, wbState2.wb)

        val sheet11NewState = wbState2.getWorksheetStateMs(wb1.getWs(0)!!.name)
        assertNotNull(sheet11NewState)
        val newCursor11 = sheet11NewState.value.cursorState

        // cursor state is preserve
        assertEquals(cursor1_1, newCursor11)

        val newSheet11 = sheet11NewState.value.worksheet
        // sheet state points to new sheet obj
        assertEquals(wb1.getWs(0), newSheet11)

        // slider is preserved
        val newSlider11 = sheet11NewState.value.slider
        assertEquals(sheet1_1State.value.slider, newSlider11)

        // tab bar state is preserved
        assertEquals(oldTabBarState, wbState2.sheetTabBarState)
    }
}

