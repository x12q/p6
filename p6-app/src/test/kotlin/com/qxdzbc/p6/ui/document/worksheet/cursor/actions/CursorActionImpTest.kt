package com.qxdzbc.p6.ui.document.worksheet.cursor.actions

import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.actions.CellEditorAction
import test.TestSample
import kotlin.test.*

class CursorActionImpTest {

    lateinit var ts:TestSample
    lateinit var cursorAction: CursorAction
    lateinit var cellEditorAction:CellEditorAction
    @BeforeTest
    fun b(){
        ts = TestSample()
        cursorAction = ts.p6Comp.cursorAction()
        cellEditorAction = ts.p6Comp.cellEditorAction()
    }

    @Test
    fun getFormulaRangeDrawInfo(){
        ts.p6Comp.cellViewAction().updateCell(
            CellUpdateRequest(
                wbKey = ts.wbKey1, wsName = ts.wsn1, cellAddress = CellAddress("B3"),
                valueAsStr = null, formula = "=B1@'Sheet2'+A1+SUM(A3:A5,A20:A23) + K1@'Sheet1'@${ts.wbKey2.name}"
            )
        )
        val wbws = WbWs(ts.wbKey1,ts.wsn1)
        val r1 = cursorAction.getFormulaRangeDrawInfo(wbws)
        assertTrue(r1.isEmpty())
        cursorAction.moveCursorTo(wbws,"B3")
        cellEditorAction.openCellEditor(wbws)
        val r2 = cursorAction.getFormulaRangeDrawInfo(wbws)
        assertTrue(r2.isNotEmpty())

        assertEquals(
            listOf(
                RangeAddress(CellAddress("A1")),
                RangeAddress("A3:A5"),
                RangeAddress("A20:A23"),
            ),
            r2.keys.toList()
        )
    }

//    lateinit var action: CursorActionImp
//    lateinit var appStateMs: Ms<AppState>
//    lateinit var wsStateMs: Ms<WorksheetState>
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
//        wsStateMs = appStateMs.value.getWorkbookStateMs(TestSample.sampleWbKey1)?.value?.getWorksheetStateMs("Sheet1")!!
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
//            expectCellAddress = CellAddress(wsStateMs.value.firstCol,33)) { action.ctrlLeft() }
//    }
//
//    @Test
//    fun ctrlLeft3() {
//        testCtrlMove(
//            additionalCellAddresses=listOf(),
//            startingCellAddress = CellAddress(wsStateMs.value.firstCol,33),
//            expectCellAddress = CellAddress(wsStateMs.value.firstCol,33)) { action.ctrlLeft() }
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
//            expectCellAddress = CellAddress(wsStateMs.value.lastCol,4)) { action.ctrlRight() }
//    }
//
//    @Test
//    fun ctrlRight3() {
//        testCtrlMove(
//            additionalCellAddresses=listOf(),
//            startingCellAddress = CellAddress(wsStateMs.value.lastCol,4),
//            expectCellAddress = CellAddress(wsStateMs.value.lastCol,4)) { action.ctrlRight() }
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
//            expectCellAddress = CellAddress("X${wsStateMs.value.lastRow}")) { action.ctrlDown() }
//    }
//
//    @Test
//    fun ctrlDown3(){
//        testCtrlMove(
//            additionalCellAddresses=listOf(),
//            startingCellAddress = CellAddress("X${wsStateMs.value.lastRow}"),
//            expectCellAddress = CellAddress("X${wsStateMs.value.lastRow}")) { action.ctrlDown() }
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
