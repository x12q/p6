package com.qxdzbc.p6.ui.document.worksheet.cursor.actions

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import io.kotest.matchers.collections.shouldContainAll
import test.TestSample
import kotlin.test.*

class CursorActionImpTest {

    lateinit var ts:TestSample
    lateinit var cursorAction: CursorAction
    lateinit var cellEditorAction: CellEditorAction
    lateinit var wsState:WorksheetState

    lateinit var sliderMs:Ms<GridSlider>
    lateinit var cursorMs:Ms<CursorState>
    val slider: GridSlider get()= sliderMs.value
    val cursor:CursorState get()=cursorMs.value
    @BeforeTest
    fun b(){
        ts = TestSample()
        cursorAction = ts.comp.cursorAction()
        cellEditorAction = ts.comp.cellEditorAction()
        val w = ts.sc.getWsState(WbWs(ts.wbKey1,ts.wsn1))
        assertNotNull(w)
        wsState = w
        sliderMs = wsState.sliderMs
        cursorMs = wsState.cursorStateMs
    }

    @Test
    fun `shift +arrow out of the slider boundary`(){
        val wbws = wsState
        val oldSlider = slider
        val cursor by cursorMs

        // shift + down
        val e1 = CellAddress(
            col = 5,
            row = 20
        )
        cursorMs.value = cursor.setMainCell(e1)

        assertEquals(e1,cursor.mainCell)
        cursorAction.shiftDown(wbws)
        assertEquals(
            RangeAddress(CellAddress(5,21),e1),
            cursor.mainRange
        )
        val eSlider1 = oldSlider.setVisibleRowRange(2 .. 21)
        assertEquals(eSlider1,slider)

        // shift + right
        val e2 = CellAddress(
            col = 20,
            row = 3
        )
        cursorMs.value = cursor.removeAllExceptMainCell().setMainCell(e2)
        cursorAction.shiftRight(wbws)
        assertEquals(
            RangeAddress(CellAddress(21,3),e2),
            cursor.mainRange
        )
        val eSlider2 = eSlider1.setVisibleColRange(2 .. 21)
        assertEquals(eSlider2,slider)

        // shift + left
        val e3 = CellAddress(
            col = 2,
            row = 3
        )
        cursorMs.value = cursor.removeAllExceptMainCell().setMainCell(e3)
        cursorAction.shiftLeft(wbws)
        assertEquals(
            RangeAddress(CellAddress(1,3),e3),
            cursor.mainRange
        )
        val eSlider3 = eSlider2.setVisibleColRange(1 .. 20)
        assertEquals(eSlider3,slider)

        // shift + up
        val e4 =CellAddress(
            col = 1,
            row = 2
        )

        cursorMs.value = cursor.removeAllExceptMainCell().setMainCell(e4)
        cursorAction.shiftUp(wbws)
        assertEquals(
            RangeAddress(CellAddress(1,1),e4),
            cursor.mainRange
        )
        val eSlider4 = eSlider3.setVisibleRowRange(1 .. 20)
        assertEquals(eSlider4,slider)
    }

    @Test
    fun `shift +arrow within slider`(){
        val oldSlider = slider
        val cursor by cursorMs
        val e5 = CellAddress("E5")
        cursorMs.value = cursor.setMainCell(e5)
        val wbws = wsState

        fun testUnChange(){
            assertEquals(e5,cursor.mainCell)
            assertEquals(oldSlider,slider)
        }

        assertEquals(e5,cursor.mainCell)
        assertNull(cursor.mainRange)

        cursorAction.shiftDown(wbws)
        val r1 = RangeAddress(e5,e5.downOneRow())
        assertEquals(r1,cursor.mainRange)
        testUnChange()


        cursorAction.shiftRight(wbws)
        val r2 = RangeAddress(e5,e5.downOneRow().rightOneCol())
        assertEquals(r2,cursor.mainRange)
        testUnChange()

        cursorAction.shiftUp(wbws)
        val r3 = RangeAddress(e5, e5.rightOneCol())
        assertEquals(r3,cursor.mainRange)
        testUnChange()

        cursorAction.shiftLeft(wbws)
        val r4 = RangeAddress(e5)
        assertEquals(r4,cursor.mainRange)
        testUnChange()
    }

    @Test
    fun getFormulaRangeDrawInfo(){
        ts.comp.cellViewAction().updateCellDM(
            CellUpdateRequestDM(
                cellId= CellIdDM(
                    wbKey = ts.wbKey1, wsName = ts.wsn1, address = CellAddress("B3")
                )
            ,
                 cellContent = CellContentDM.fromFormula("=B1@'Sheet2'+A1+SUM(A3:A5,A20:A23) + K1@'Sheet1'@${ts.wbKey2.name}")
            )
        )
        val wbws = WbWs(ts.wbKey1,ts.wsn1)
        val r1 = cursorAction.getFormulaRangeAndColor(wbws)
        assertTrue(r1.isEmpty())
        cursorAction.moveCursorTo(wbws,"B3")
        cellEditorAction.openCellEditor(wbws)
        val r2 = cursorAction.getFormulaRangeAndColor(wbws)
        assertTrue(r2.isNotEmpty())

        r2.keys.shouldContainAll(
            RangeAddress("A1"),
            RangeAddress("A3:A5"),
            RangeAddress("A20:A23"),
        )
    }

//    lateinit var action: CursorActionImp
//    lateinit var appStateMs: Ms<AppState>
//    lateinit var wsState: WorksheetState
//    lateinit var wsAction: WorksheetAction
//    lateinit var errorRouter: ErrorRouter
//    lateinit var cursorStateMs: Ms<CursorState>
//    val b1 get() = appStateMs.value.globalWbCont.getWb(TestSample.sampleWbKey1)!!
//    val ws1Ofb1 get() = b1.worksheets[0]
//    lateinit var rangeApplier: RangeApplier
//    lateinit var rangeRM: RangeRM
//    lateinit var testSample: TestSample
//    @BeforeTest
//    fun b() {
//        testSample = TestSample()
//        val wbid: Ms<WorkbookStateID> = ms(WorkbookStateIDImp(WorkbookKey("")))
//        wsAction = mock()
//        appStateMs = testSample.sampleAppStateMs()
//        wsState = appStateMs.value.getWorkbookStateMs(TestSample.sampleWbKey1)?.value?.getWorksheetStateMs("Sheet1")!!
//        errorRouter = mock()
//        cursorStateMs = ms(
//            CursorStateImp.default(
//                ms(
//                    CursorIdImp(
//                        ms(WorksheetStateIDImp("asd", wbid) as WorksheetStateID)
//                    )
//                )
//            )
//        )
//        rangeRM = mock()
//        rangeApplier = mock()
//        action = CursorActionImp(
//            wsAction = wsAction,
//            appStateMs = appStateMs,
//            errorRouter = errorRouter,
//            rangeRM = rangeRM,
//            rangeApplier = rangeApplier,
//        )
//    }
//
//    @Test
//    fun home() {
//        cursorStateMs.value = cursorStateMs.value.setAnchorCell(CellAddress("GJ55"))
//        action.home()
//        assertEquals(CellAddress("A55"), cursorStateMs.value.mainCell)
//        verify(wsAction, times(1)).makeSliderFollowCursor(any())
//    }
//
//    @Test
//    fun end() {
//        cursorStateMs.value = cursorStateMs.value.setAnchorCell(CellAddress("GJ55"))
//        action.end()
//        assertEquals(CellAddress("BJOER55"), cursorStateMs.value.mainCell)
//        verify(wsAction, times(1)).makeSliderFollowCursor(any())
//    }
//
//    @Test
//    fun ctrlLeft() {
//        testCtrlMove(
//            additionalCellAddresses=listOf(CellAddress("X55"),CellAddress("B55")),
//            startingCellAddress = CellAddress("GJ55"),
//            expectCellAddress = CellAddress("X55")) { action.ctrlLeft() }
//    }
//
//    @Test
//    fun ctrlLeft2() {
//        testCtrlMove(
//            additionalCellAddresses=listOf(),
//            startingCellAddress = CellAddress(20,33),
//            expectCellAddress = CellAddress(wsState.value.firstCol,33)) { action.ctrlLeft() }
//    }
//
//    @Test
//    fun ctrlLeft3() {
//        testCtrlMove(
//            additionalCellAddresses=listOf(),
//            startingCellAddress = CellAddress(wsState.value.firstCol,33),
//            expectCellAddress = CellAddress(wsState.value.firstCol,33)) { action.ctrlLeft() }
//    }
//
//    @Test
//    fun ctrlRight() {
//        testCtrlMove(
//            additionalCellAddresses=listOf(CellAddress("X55"),CellAddress("YT55")),
//            startingCellAddress = CellAddress("F55"),
//            expectCellAddress = CellAddress("X55")) { action.ctrlRight() }
//    }
//
//    @Test
//    fun ctrlRight2() {
//        testCtrlMove(
//            additionalCellAddresses=listOf(),
//            startingCellAddress = CellAddress("D4"),
//            expectCellAddress = CellAddress(wsState.value.lastCol,4)) { action.ctrlRight() }
//    }
//
//    @Test
//    fun ctrlRight3() {
//        testCtrlMove(
//            additionalCellAddresses=listOf(),
//            startingCellAddress = CellAddress(wsState.value.lastCol,4),
//            expectCellAddress = CellAddress(wsState.value.lastCol,4)) { action.ctrlRight() }
//    }
//
//    @Test
//    fun ctrlUp(){
//        testCtrlMove(
//            additionalCellAddresses=listOf(CellAddress("X55"),CellAddress("X33")),
//            startingCellAddress = CellAddress("X100"),
//            expectCellAddress = CellAddress("X55")) { action.ctrlUp() }
//    }
//
//    @Test
//    fun ctrlUp2(){
//        testCtrlMove(
//            additionalCellAddresses=listOf(),
//            startingCellAddress = CellAddress("X100"),
//            expectCellAddress = CellAddress("X1")) { action.ctrlUp() }
//    }
//
//    @Test
//    fun ctrlUp3(){
//        testCtrlMove(
//            additionalCellAddresses=listOf(),
//            startingCellAddress = CellAddress("X1"),
//            expectCellAddress = CellAddress("X1")) { action.ctrlUp() }
//    }
//
//    @Test
//    fun ctrlDown(){
//        testCtrlMove(
//            additionalCellAddresses=listOf(CellAddress("X55"),CellAddress("X33")),
//            startingCellAddress = CellAddress("X3"),
//            expectCellAddress = CellAddress("X33")) { action.ctrlDown() }
//    }
//
//    @Test
//    fun ctrlDown2(){
//        testCtrlMove(
//            additionalCellAddresses=listOf(),
//            startingCellAddress = CellAddress("X3"),
//            expectCellAddress = CellAddress("X${wsState.value.lastRow}")) { action.ctrlDown() }
//    }
//
//    @Test
//    fun ctrlDown3(){
//        testCtrlMove(
//            additionalCellAddresses=listOf(),
//            startingCellAddress = CellAddress("X${wsState.value.lastRow}"),
//            expectCellAddress = CellAddress("X${wsState.value.lastRow}")) { action.ctrlDown() }
//    }
//
//    private fun testCtrlMove(
//        additionalCellAddresses: List<CellAddress>,
//        startingCellAddress: CellAddress,
//        expectCellAddress: CellAddress,
//        action: () -> Unit
//    ) {
//        cursorStateMs.value = cursorStateMs.value.setAnchorCell(startingCellAddress)
//        val newWorksheet: Worksheet = additionalCellAddresses.fold(ws1Ofb1) { ws, ce ->
//            ws.addOrOverwrite(CellImp(ce))
//        }
//        val newWb = b1.addSheetOrOverwrite(newWorksheet)
//        appStateMs.value.globalWbContMs.value = appStateMs.value.globalWbCont.overwriteWB(newWb)
//        action()
//        assertEquals(expectCellAddress, cursorStateMs.value.mainCell)
//    }
//    @Test
//    fun ctrlSpace(){
//        cursorStateMs.value = cursorStateMs.value
//            .setAnchorCell(CellAddress("C3"))
//            .addFragRanges(listOf(RangeAddress("B5:K4"), RangeAddress("H1:J2")))
//            .addFragCell(CellAddress("Z4"))
//        action.ctrlSpace()
//        assertTrue(RangeAddress("C:C") in cursorStateMs.value.fragmentedRanges)
//        assertTrue(RangeAddress("Z:Z") in cursorStateMs.value.fragmentedRanges)
//        assertTrue (RangeAddress("B:K") in cursorStateMs.value.fragmentedRanges)
//        assertTrue (RangeAddress("H:J") in cursorStateMs.value.fragmentedRanges)
//    }
//
//    @Test
//    fun shiftSpace(){
//        cursorStateMs.value = cursorStateMs.value
//            .setAnchorCell(CellAddress("C3"))
//            .addFragRanges(listOf(RangeAddress("B5:K4"), RangeAddress("H1:J2")))
//            .addFragCell(CellAddress("Z4"))
//        action.shiftSpace()
//        assertTrue(RangeAddress("3:3") in cursorStateMs.value.fragmentedRanges)
//        assertTrue(RangeAddress("4:4") in cursorStateMs.value.fragmentedRanges)
//        assertTrue (RangeAddress("4:5") in cursorStateMs.value.fragmentedRanges)
//        assertTrue (RangeAddress("1:2") in cursorStateMs.value.fragmentedRanges)
//    }
}
