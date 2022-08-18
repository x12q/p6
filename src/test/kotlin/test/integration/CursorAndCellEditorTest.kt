package test.integration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import com.emeraldblast.p6.app.action.common_data_structure.WbWsImp
import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.emeraldblast.p6.app.common.utils.PKeyEvent
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.actions.CellEditorAction
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.actions.CellEditorActionImp
import com.emeraldblast.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction
import org.mockito.kotlin.*
import test.TestSample
import test.test_implementation.MockPKeyEvent
import kotlin.test.*

class CursorAndCellEditorTest {

    lateinit var testSample: TestSample
    val appState get() = testSample.appState
    val p6Comp get()=testSample.p6Comp
    val cellEditorAction get() = p6Comp.cellEditorAction()

    @BeforeTest
    fun b() {
        testSample = TestSample()
    }

    @Test
    fun `test running formula`() {
        val wbk = testSample.wbKey1Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = testSample.wsn1
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs

        assertNotNull(wds)
        assertNotNull(cursor1Ms)

        // x: open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        val targetCell = cellEditorState.targetCell
        val targetWb = cellEditorState.targetCursorId?.wbKey
        val targetWsName = cellEditorState.targetCursorId?.wsName

        assertNotNull(targetCell)
        assertNotNull(targetWb)
        assertNotNull(targetWsName)
        val focusState = appState.getFocusStateMsByWbKey(targetWb)
        assertNotNull(focusState)

        // x: input formula
        cellEditorState = cellEditorState.setCurrentText("=1+2+3")

        // x: run the formula
        cellEditorAction.runFormula()

        //
        assertEquals(6.0, appState.getCell(targetWb, targetWsName, targetCell)?.currentCellValue?.currentValue)
        assertNull(cellEditorState.targetCursorIdSt)
        assertNull(cellEditorState.targetCell)
        assertTrue(focusState.value.isCursorFocused)
        assertFalse(focusState.value.isEditorFocused)
    }

    @Test
    fun `test moving in and out of range selection state`() {
        val wbk = testSample.wbKey1Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = testSample.wsn1
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = testSample.p6Comp.cellEditorAction()

        // x: open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        val rangeSelectorMs = cellEditorState.rangeSelectorCursorId?.let { appState.getCursorStateMs(it) }
        assertNotNull(rangeSelectorMs)
        cellEditorState = cellEditorState.setCurrentText("=1+")
        assertTrue(cellEditorState.allowRangeSelector)
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun `test appending range selector address to cell editor when navigating and editing using keyboard`() {
        val wbk = testSample.wbKey1Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = testSample.wsn1
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        val cellEditorState by appState.cellEditorStateMs

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = testSample.p6Comp.cellEditorAction()

        // x: open cell editor on a cursor
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        val rangeSelectorMs = cellEditorState.rangeSelectorCursorId?.let { appState.getCursorStateMs(it) }
        assertNotNull(rangeSelectorMs)
        val rangeSelector by rangeSelectorMs

        // x: input text
        cellEditorAction.updateText("=1+")

        assertEquals("=1+", cellEditorState.currentText)
        assertEquals("=1+", cellEditorState.displayText.text)
        assertNull(cellEditorState.rangeSelectorText)
        assertTrue(cellEditorState.allowRangeSelector)

        // x: issue directional key event
        val arrowDownKey = MockPKeyEvent(
            key = Key.DirectionDown,
            type = KeyEventType.KeyDown,
            isRangeSelectorToleratedKey = true,
            isRangeSelectorNavKey = true
        )
        cellEditorAction.handleKeyboardEvent(arrowDownKey)

        // x: check state after handle keyboard event
        assertTrue(cellEditorState.allowRangeSelector)
        assertEquals(CellAddress("A2"), rangeSelector.mainCell)
        assertEquals("=1+A2", cellEditorState.rangeSelectorText?.text)
        assertEquals("=1+A2", cellEditorState.displayText.text)
        assertEquals("=1+", cellEditorState.currentText)

        // x: handle another key event
        cellEditorAction.handleKeyboardEvent(arrowDownKey)
        assertTrue(cellEditorState.allowRangeSelector)
        assertEquals(CellAddress("A3"), rangeSelector.mainCell)
        assertEquals("=1+A3", cellEditorState.rangeSelectorText?.text)
        assertEquals("=1+A3", cellEditorState.displayText.text)
        assertEquals("=1+", cellEditorState.currentText)

        // x: handle key that ends range selector state
        val key = mock<PKeyEvent> {
            whenever(it.isRangeSelectorToleratedKey()) doReturn false
        }
        cellEditorAction.handleKeyboardEvent(key)
        assertEquals("=1+A3", cellEditorState.currentText)
        assertNull(cellEditorState.rangeSelectorText)
        assertEquals("=1+A3", cellEditorState.displayText.text)
        assertFalse(cellEditorState.allowRangeSelector)

        // x: another case: moving from allow range selector to not allow by typing in a not tolerated character
        cellEditorAction.updateText("=1+A3+")
        assertTrue(cellEditorState.allowRangeSelector)
        assertNull(cellEditorState.rangeSelectorText)
        assertEquals("=1+A3+", cellEditorState.displayText.text)
        assertEquals("=1+A3+", cellEditorState.currentText)

        // x: simulate typing "1" into the cell editor
        cellEditorAction.updateText("=1+A3+1")
        assertFalse(cellEditorState.allowRangeSelector)
        assertNull(cellEditorState.rangeSelectorText?.text)
        assertEquals("=1+A3+1", cellEditorState.displayText.text)
        assertEquals("=1+A3+1", cellEditorState.currentText)
    }

    @Test
    fun `test passing key event from cell editor to range selector cursor`() {
        val wbk = testSample.wbKey1Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = testSample.wsn1
        val wsn2 = testSample.wsn2
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        val wbAction = testSample.p6Comp.workbookAction()
        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val spyCursorAction = spy(testSample.p6Comp.cursorAction())
        val cellEditorAction: CellEditorAction = CellEditorActionImp(
            appStateMs = testSample.appStateMs,
            cellLiteralParser = p6Comp.cellLiteralParser(),
            cellViewAction = p6Comp.cellViewAction(),
            cursorAction = spyCursorAction,
            makeDisplayText = p6Comp.makeDisplayText(),
            open = p6Comp.openCellEditorAction()
        )
        val keyEvent = mock<PKeyEvent> {
            whenever(it.isRangeSelectorToleratedKey()) doReturn true
        }
        // x: open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        cellEditorAction.updateText("=")

        // x: move to another sheet
        val request = SetActiveWorksheetRequest(
            wbKey = wbk,
            wsName = wsn2,
        )
        wbAction.switchToWorksheet(request)
        val rangeCursorMs = appState.getCursorStateMs(request)
        assertNotNull(rangeCursorMs)

        cellEditorAction.handleKeyboardEvent(keyEvent)
        verify(spyCursorAction).handleKeyboardEvent(
            keyEvent,
            appState.getCursorState(appState.cellEditorState.rangeSelectorCursorId!!)!!
        )
    }

    /**
     * Cell editor is open with non-range-selector content
     * Switch sheet
     * Expect: cell editor is closed, content is clear, cursor is focused
     */
    @Test
    fun `test transfer control to new cursor after switching ws`() {
        val wbk = testSample.wbKey1Ms.value
        val wbk2 = testSample.wbKey2Ms.value
        val wsn1 = testSample.wsn1
        val wsn2 = testSample.wsn2
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        val cellEditorMs = appState.cellEditorStateMs
        val wds = appState.getWindowStateMsByWbKey(wbk)

        assertNotNull(wds)
        assertNotNull(cursor1Ms)

        // x: open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        cellEditorAction.updateText("=")
        assertTrue { wds.value.focusState.isEditorFocused }
        assertEquals(cursor1Ms.value.idMs, cellEditorMs.value.targetCursorIdSt)
        assertEquals(cursor1Ms.value.idMs, cellEditorMs.value.rangeSelectorCursorIdSt)

        // x:switch sheet
        val wbAction = testSample.p6Comp.workbookAction()
        wbAction.setFocusStateConsideringRangeSelector(wbk)
        val request = SetActiveWorksheetRequest(
            wbKey = wbk,
            wsName = wsn2,
        )
        wbAction.switchToWorksheet(request)
        val cursor2Ms = appState.getCursorStateMs(wbk, wsn2)
        assertNotNull(cursor2Ms)
        assertTrue { wds.value.focusState.isEditorFocused }
        assertEquals(cursor1Ms.value.idMs, cellEditorMs.value.targetCursorIdSt)
        assertEquals(cursor2Ms.value.idMs, cellEditorMs.value.rangeSelectorCursorIdSt)


        // x: switch workbook
        val moveWbAction = testSample.p6Comp.moveToWbAction()
        val cursor3Ms = appState.getCursorStateMs(wbk2, wsn1)
        assertNotNull(cursor3Ms)
        moveWbAction.moveToWorkbook(wbk2)
        assertTrue { wds.value.focusState.isEditorFocused }
        assertEquals(cursor1Ms.value.idMs, cellEditorMs.value.targetCursorIdSt)
        assertEquals(cursor3Ms.value.idMs, cellEditorMs.value.rangeSelectorCursorIdSt)
    }

    @Test
    fun `test opened cell editor when moving to different wb`() {
        val wbk1 = testSample.wbKey1Ms.value
        val wsn = "Sheet1"
        val wbk2 = testSample.wbKey2Ms.value
        val cursorMs = appState.getCursorStateMs(wbk1, wsn)

        var ces by appState.cellEditorStateMs
        val wbTabBarAction: WorkbookTabBarAction = testSample.p6Comp.wbTabBarAction()
        assertNotNull(cursorMs)

        // x: open cursor editor + set normal text
        cellEditorAction.openCellEditor(WbWsImp(wbk1, wsn))
        ces = ces.setCurrentText("abc")

        // x: move to another workbook
        wbTabBarAction.moveToWorkbook(wbk2)
        //
        assertFalse(ces.isActive)
        assertTrue(appState.getFocusStateMsByWbKey(wbk2)?.value?.isCursorFocused!!)
        assertFalse(appState.getFocusStateMsByWbKey(wbk2)?.value?.isEditorFocused!!)


        // x: open cursor editor + set range-selector-enable text
        cellEditorAction.openCellEditor(WbWsImp(wbk2, wsn))
        ces = ces.setCurrentText("=")
        wbTabBarAction.moveToWorkbook(wbk1)
        assertTrue(ces.isActive)
        assertFalse(appState.getFocusStateMsByWbKey(wbk1)?.value?.isCursorFocused!!)
        assertTrue(appState.getFocusStateMsByWbKey(wbk1)?.value?.isEditorFocused!!)
    }

    @Test
    fun `test click,drag on cell while cell editor is opened`() {
        val wbk = testSample.wbKey1Ms.value
        val wsn = "Sheet1"
        val cursorMs = appState.getCursorStateMs(wbk, wsn)
        val cellEditorMs = appState.cellEditorStateMs
        val wsAction = testSample.p6Comp.wsAction()
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsStateMs = appState.getWsStateMs(wbk, wsn)

        assertNotNull(cursorMs)
        assertNotNull(wds)
        assertNotNull(wsStateMs)
        val wsState by wsStateMs
        // open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn))
        // click on another cell
        val clickedCell = CellAddress("D10")
        wsAction.startDragSelection(wsState, clickedCell)
        //
        assertFalse(cellEditorMs.value.isActive)
        assertEquals("", cellEditorMs.value.currentText)
        assertEquals(clickedCell, cursorMs.value.mainCell)
        assertTrue(cursorMs.value.fragmentedCells.isEmpty())
        assertTrue(cursorMs.value.fragmentedRanges.isEmpty())
        assertTrue(cursorMs.value.mainRange == null)
        assertEquals(listOf(clickedCell), cursorMs.value.allFragCells)

        // open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn))
        val text2 = "=123+"
        cellEditorMs.value = cellEditorMs.value.setCurrentText(text2)

        val clickedCell2 = CellAddress("K8")
        wsAction.startDragSelection(wsState, clickedCell2)

        assertTrue(cellEditorMs.value.isActive)
        assertEquals(text2, cellEditorMs.value.currentText)
        assertEquals(clickedCell2, cursorMs.value.mainCell)
        // drag the mouse
        val dragCell = CellAddress("Q30")
        wsAction.makeMouseDragSelectionIfPossible(wsState, dragCell)
        assertTrue(cellEditorMs.value.isActive)
        assertEquals(text2, cellEditorMs.value.currentText)
        assertEquals(clickedCell2, cursorMs.value.mainCell)
        assertEquals(RangeAddress(clickedCell2, dragCell), cursorMs.value.mainRange)

        // stop dragging
        wsAction.stopDragSelection(wsState)
        assertTrue(cellEditorMs.value.isActive)
        assertEquals(text2, cellEditorMs.value.currentText)
        assertEquals(clickedCell2, cursorMs.value.mainCell)
        assertEquals(RangeAddress(clickedCell2, dragCell), cursorMs.value.mainRange)
    }


    /**
     * Cell editor is open with non-range-selector content
     * Switch sheet
     * Expect: cell editor is closed, content is clear, cursor is focused
     */
    @Test
    fun `test cursor & cell editor state after switching ws`() {
        val wbk = testSample.wbKey1Ms.value
        val wsn = "Sheet1"
        val cursorMs = appState.getCursorStateMs(wbk, wsn)
        val cellEditorMs = appState.cellEditorStateMs
        assertNotNull(cursorMs)

        // open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn))
        val wds = appState.getWindowStateMsByWbKey(wbk)
        assertNotNull(wds)
        assertTrue { wds.value.focusState.isEditorFocused }

        // switch sheet
        val wbAction = testSample.p6Comp.workbookAction()
        wbAction.setFocusStateConsideringRangeSelector(wbk)
        val request = SetActiveWorksheetRequest(
            wbKey = wbk,
            wsName = "Sheet2",
        )
        wbAction.switchToWorksheet(request)
        //
        assertFalse { cellEditorMs.value.isActive }
        assertEquals("", cellEditorMs.value.currentText)

        assertFalse(wds.value.focusState.isEditorFocused)
        assertTrue(wds.value.focusState.isCursorFocused)
    }

    /**
     * Cell editor is opened, content is range-selector-enable
     * Switch sheet
     * expect: cursor is not focused, editor is kept open, opened and editor's content is preserved
     */
    @Test
    fun `test preserving cell editor focus after switching ws`() {
        val wbk = testSample.wbKey1Ms.value
        val wsn = "Sheet1"
        val cursorMs = appState.getCursorStateMs(wbk, wsn)
        val cellEditorMs = appState.cellEditorStateMs
        assertNotNull(cursorMs)

        // open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn))
        val wds = appState.getWindowStateMsByWbKey(wbk)
        assertNotNull(wds)
        assertTrue { wds.value.focusState.isEditorFocused }
        // add content
        cellEditorMs.value = cellEditorMs.value.setCurrentText("=123+") //range-selector-enable content
        assertTrue(cellEditorMs.value.allowRangeSelector)

        // switch sheet
        val wbAction = testSample.p6Comp.workbookAction()
        val request = SetActiveWorksheetRequest(
            wbKey = wbk,
            wsName = "Sheet2",
        )
        wbAction.switchToWorksheet(request)

        // expect:
        assertTrue(cellEditorMs.value.isActive)
        assertEquals("=123+", cellEditorMs.value.currentText)
        assertTrue(wds.value.focusState.isEditorFocused)
        assertFalse(wds.value.focusState.isCursorFocused)
    }
}
