package com.qxdzbc.p6.ui.document.workbook.state

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.IndCellImp
import com.qxdzbc.p6.app.document.cell.CellValue.Companion.toCellValue
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import org.mockito.kotlin.mock
import test.TestSample
import kotlin.test.*

class WorkbookStateImpTest {
    lateinit var wbState: WorkbookStateImp
    lateinit var wb0: Workbook
    lateinit var wb1: Workbook
    lateinit var ts: TestSample

    @BeforeTest
    fun b() {
        ts = TestSample()
        wb0 = WorkbookImp(
            ts.wbKey2Ms,
        ).apply {
            addMultiSheetOrOverwrite(
                listOf(
                    WorksheetImp("Sheet1_2".toMs(), ts.wbKey2Ms).apply {
                        addOrOverwrite(
                            IndCellImp(
                                CellAddress("A1"), CellContentImp(
                                    cellValueMs = "a1".toCellValue().toMs()
                                )
                            )
                        )
                    },
                )
            )
        }

        wb1 = WorkbookImp(
            WorkbookKey("Book2").toMs(),
        ).apply {
            addMultiSheetOrOverwrite(
                listOf(
                    WorksheetImp("Sheet1_2".toMs(), mock()).apply {
                        addOrOverwrite(
                            IndCellImp(
                                CellAddress("A1"),
                                CellContentImp(cellValueMs = "a1".toCellValue().toMs())
                            )
                        )
                        addOrOverwrite(
                            IndCellImp(
                                CellAddress("A2"),
                                CellContentImp(cellValueMs = "a2".toCellValue().toMs())
                            )
                        )
                    },
                    WorksheetImp("Sheet2_2".toMs(), mock()).apply {
                        addOrOverwrite(
                            IndCellImp(
                                CellAddress("B1"),
                                CellContentImp(cellValueMs = "b1".toCellValue().toMs())
                            )
                        )
                        addOrOverwrite(
                            IndCellImp(
                                CellAddress("B2"),
                                CellContentImp(cellValueMs = "b2".toCellValue().toMs())
                            )
                        )
                    }
                )
            )
        }

        val wbCont = ts.wbCont

        wbCont.apply {
            overwriteWb(wb0)
            overwriteWb(wb1)
        }

        wbState = WorkbookStateImp.forTesting(
            wbMs = wbCont.getWbMs(wb0.key)!!,
            wsStateFactory = ts.comp.worksheetStateFactory(),
            gridSliderFactory = ts.comp.gridSliderFactory(),
            cursorStateFactory = ts.comp.cursorStateFactory(),
            thumbStateFactory = ts.comp.thumbStateFactory()
        )
    }

    @Test
    fun setWorkbook() {
        val sheet1_1State: WorksheetState? = wbState.getWsStateMs(wb0.getWs(0)!!.name)
        assertNotNull(sheet1_1State)
        sheet1_1State.cursorStateMs.value = sheet1_1State.cursorState.setMainCell(CellAddress("B9"))
        val cursor1_1 = sheet1_1State.cursorState
        val oldTabBarState = wbState.sheetTabBarState
        val wbState2 = wbState.setWorkbookKeyAndRefreshState(wb1.key)
        val l2 = wbState.worksheetStateList
        assertEquals(2, l2.size)
        assertEquals(wb1, wbState.wb)

        val sheet11NewState = wbState.getWsStateMs(wb1.getWs(0)!!.name)
        assertNotNull(sheet11NewState)
        val newCursor11 = sheet11NewState.cursorState

        // cursor state is preserve
        assertEquals(cursor1_1, newCursor11)

        val newSheet11 = sheet11NewState.worksheet
        // sheet state points to new sheet obj
        assertEquals(wb1.getWs(0), newSheet11)

        // slider is preserved
        val newSlider11 = sheet11NewState.slider
        assertEquals(sheet1_1State.slider, newSlider11)

        // tab bar state is preserved
        assertEquals(oldTabBarState, wbState.sheetTabBarState)
    }
}

