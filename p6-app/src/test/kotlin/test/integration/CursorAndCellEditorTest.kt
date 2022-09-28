package test.integration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsImp
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCell
import com.qxdzbc.common.compose.key_event.PKeyEvent
import com.qxdzbc.p6.app.common.utils.CellLabelNumberSystem
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.actions.CellEditorAction
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.actions.CellEditorActionImp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerSig
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerAction
import com.qxdzbc.p6.ui.window.formula_bar.FormulaBarState
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction
import org.mockito.kotlin.*
import test.TestSample
import test.test_implementation.MockPKeyEvent
import kotlin.test.*

class CursorAndCellEditorTest {

    lateinit var ts: TestSample
    val appState get() = ts.appState
    val p6Comp get() = ts.p6Comp
    val cellEditorAction get() = p6Comp.cellEditorAction()
    val sc get()=ts.stateCont

    @BeforeTest
    fun b() {
        ts = TestSample()
    }

    @Test
    fun `click on ruler item when editing cell, allow range select`(){
        val rulerAction: RulerAction = ts.p6Comp.rulerAction()
        val wbwsSt = sc.getWbWsSt(WbWsImp(ts.wbKey1,ts.wsn1))
        assertNotNull(wbwsSt)
        val rulerSig = object: RulerSig{
            override val type: RulerType
                get() = RulerType.Col
            override val wbKeySt: St<WorkbookKey>
                get() = wbwsSt.wbKeySt
            override val wsNameSt: St<String>
                get() = wbwsSt.wsNameSt
        }

        cellEditorAction.openCellEditor(wbwsSt)
        cellEditorAction.updateText("=")
        val col = 20
        val col2 = 220
        val colLabel = CellLabelNumberSystem.numberToLabel(col)
        val colLabel2 = CellLabelNumberSystem.numberToLabel(col2)

        assertEquals("=",sc.cellEditorState.displayText)
        rulerAction.clickRulerItem(col, rulerSig)
        assertEquals("=${colLabel}:${colLabel}",sc.cellEditorState.displayText)
        rulerAction.clickRulerItem(col2, rulerSig)
        assertEquals("=${colLabel2}:${colLabel2}",sc.cellEditorState.displayText)
    }

    @Test
    fun `test short formula convertion function`(){
        val wbk = ts.wbKey1Ms.value
        val wbk2 = ts.wbKey2Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs
        val mouseOnWsAction: MouseOnWorksheetAction = ts.p6Comp.mouseOnWsAction()

        assertNotNull(wds)
        assertNotNull(cursor1Ms)

        val cellEditorAction: CellEditorAction = ts.p6Comp.cellEditorAction()
        cellEditorAction.openCellEditor(cursor1Ms.value)
        cellEditorAction.updateText("=SUM(B2:C4)")
        cellEditorAction.runFormulaOrSaveValueToCell()
        cellEditorAction.openCellEditor(cursor1Ms.value)
        val formulaBar:FormulaBarState? = ts.stateContMs().value.getWindowStateMsByWbKey(cursor1Ms.value.wbKey)?.value?.formulaBarState

        assertEquals("=SUM(B2:C4)",formulaBar?.text)

        assertEquals("=SUM(B2:C4)",cellEditorState.currentText)

    }

    @Test
    fun `Shift click while range selector is activated`(){
        val wbk = ts.wbKey1Ms.value
        val wbk2 = ts.wbKey2Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs
        val mouseOnWsAction: MouseOnWorksheetAction = ts.p6Comp.mouseOnWsAction()

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.p6Comp.cellEditorAction()

        // x: open cell editor on a worksheet
        val cursorLoc = WbWs(wbk, wsn1)
        cellEditorAction.openCellEditor(cursorLoc)

        cellEditorState = cellEditorState.setCurrentText("=1+")
        assertTrue(cellEditorState.allowRangeSelector)

        // x: click
        val c = CellAddress("B5")
        mouseOnWsAction.clickOnCell(c,cursorLoc)
        val rangeSelector by appState.getCursorStateMs(cellEditorState.rangeSelectorCursorId!!)!!
        val c2 = CellAddress("K9")
        mouseOnWsAction.shiftClickSelectRange(c2,cursorLoc)
        val r = RangeAddress(c,c2)
        assertEquals(r, rangeSelector.mainRange)
        val t = "=1+${r.rawLabel}"
        assertEquals(t,cellEditorState.rangeSelectorText)
        assertEquals(t,cellEditorState.displayText)
        assertEquals("=1+",cellEditorState.currentText)
    }


    @Test
    fun `tes ctrl click while range selector is NOT active but editor is active`(){
        val wbk = ts.wbKey1Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs
        val mouseOnWsAction: MouseOnWorksheetAction = ts.p6Comp.mouseOnWsAction()

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.p6Comp.cellEditorAction()

        // x: open cell editor on a worksheet
        val cursorLoc = WbWs(wbk, wsn1)
        cellEditorAction.openCellEditor(cursorLoc)

        cellEditorState = cellEditorState.setCurrentText("=123")
        val rangeSelectorId = cellEditorState.rangeSelectorCursorId
        assertNotNull(rangeSelectorId)

        // x: ctrl click
        val c = CellAddress("B5")
        mouseOnWsAction.ctrlClickSelectCell(c,cursorLoc)
        assertFalse { cellEditorState.isActive }
        assertEquals(listOf(c),appState.getCursorState(rangeSelectorId)?.fragmentedCells?.toList())
    }

    @Test
    fun `tes ctrl click while range selector is active`(){
        val wbk = ts.wbKey1Ms.value
        val wbk2 = ts.wbKey2Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs
        val mouseOnWsAction: MouseOnWorksheetAction = ts.p6Comp.mouseOnWsAction()

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.p6Comp.cellEditorAction()

        // x: open cell editor on a worksheet
        val cursorLoc = WbWs(wbk, wsn1)
        cellEditorAction.openCellEditor(cursorLoc)

        cellEditorState = cellEditorState.setCurrentText("=1+")
        assertTrue(cellEditorState.allowRangeSelector)

        // x: click
        val c = CellAddress("B5")
        mouseOnWsAction.clickOnCell(c,cursorLoc)
        val expectedText = "=1+${c.toRawLabel()}"
        val rangeSelector by appState.getCursorStateMs(cellEditorState.rangeSelectorCursorId!!)!!
        fun test(){
            assertEquals(expectedText,cellEditorState.displayText)
            assertEquals(expectedText,cellEditorState.rangeSelectorText)
            assertNull(rangeSelector.mainRange)
            assertEquals(c, rangeSelector.mainCell)
            assertTrue(rangeSelector.fragmentedRanges.isEmpty())
            assertTrue(rangeSelector.fragmentedCells.isEmpty())
        }
        test()

        // x: ctrl click
        mouseOnWsAction.ctrlClickSelectCell(CellAddress("K9"),cursorLoc)
        test()

    }

    @Test
    fun `test using range selector with dragging mouse on different sheet`() {
        val wbk = ts.wbKey1Ms.value
        val wbk2 = ts.wbKey2Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs
        val mouseOnWsAction: MouseOnWorksheetAction = ts.p6Comp.mouseOnWsAction()

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.p6Comp.cellEditorAction()

        // x: open cell editor on a wsn1/wbk
        val cursorLoc = WbWs(wbk, wsn1)
        cellEditorAction.openCellEditor(cursorLoc)

        cellEditorState = cellEditorState.setCurrentText("=1+")
        assertTrue(cellEditorState.allowRangeSelector)

        // x: start dragging on I16/wsn1/wbk2
        val cursorLoc2 = WbWs(wbk2,wsn1)
        val c = CellAddress("I16")
        mouseOnWsAction.startDragSelection(cursorLoc2, c)
        val rangeSelectorMs = cellEditorState.rangeSelectorCursorId?.let { appState.getCursorStateMs(it) }
        assertNotNull(rangeSelectorMs)
        assertEquals(appState.getCursorState(cursorLoc2)?.id, cellEditorState.rangeSelectorCursorId)
        val rangeSelectorState by rangeSelectorMs
        assertEquals(c, rangeSelectorState.mainCell)

        // x: drag to D9/wsn1/wbk2
        val c2 = CellAddress("D9")
        val range = RangeAddress(c, c2)
        mouseOnWsAction.makeMouseDragSelectionIfPossible(cursorLoc2, c2)
        assertEquals(range, rangeSelectorState.mainRange)
        val expectedText = "=1+${range.rawLabel}@${wsn1}@${wbk2.name}"
        assertEquals(expectedText, cellEditorState.displayText)
        assertEquals(expectedText, cellEditorState.rangeSelectorText)

        // x: drag to L20/wsn1/wbk2
        val c3 = CellAddress("L20")
        val range2 = RangeAddress(c, c3)
        mouseOnWsAction.makeMouseDragSelectionIfPossible(cursorLoc2,c3)
        assertEquals(range2, rangeSelectorState.mainRange)
        val expectedText2 = "=1+${range2.rawLabel}@${wsn1}@${wbk2.name}"
        assertEquals(expectedText2, cellEditorState.displayText)
        assertEquals(expectedText2, cellEditorState.rangeSelectorText)

        // x: stop dragging
        mouseOnWsAction.stopDragSelection(cursorLoc2)
        assertEquals(range2, rangeSelectorState.mainRange)
        assertEquals(expectedText2, cellEditorState.displayText)
        assertEquals(expectedText2, cellEditorState.rangeSelectorText)
    }

    @Test
    fun `test using range selector with dragging mouse`() {
        val wbk = ts.wbKey1Ms.value
        val wbk2 = ts.wbKey2Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs
        val mouseOnWsAction: MouseOnWorksheetAction = ts.p6Comp.mouseOnWsAction()

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.p6Comp.cellEditorAction()

        // x: open cell editor on a worksheet
        val cursorLoc = WbWs(wbk, wsn1)
        cellEditorAction.openCellEditor(cursorLoc)
        val rangeSelectorMs = cellEditorState.rangeSelectorCursorId?.let { appState.getCursorStateMs(it) }
        assertNotNull(rangeSelectorMs)
        cellEditorState = cellEditorState.setCurrentText("=1+")
        assertTrue(cellEditorState.allowRangeSelector)

        // x: start draggin on I16
        val c = CellAddress("I16")
        mouseOnWsAction.startDragSelection(cursorLoc, c)
        assertEquals(cellEditorState.targetCursorId, cellEditorState.rangeSelectorCursorId)
        val rangeSelectorState by rangeSelectorMs
        assertEquals(c, rangeSelectorState.mainCell)

        // x: drag to D9
        val c2 = CellAddress("D9")
        val range = RangeAddress(c, c2)
        mouseOnWsAction.makeMouseDragSelectionIfPossible(cursorLoc, c2)
        assertEquals(range, rangeSelectorState.mainRange)
        val expectedText = "=1+${range.rawLabel}"
        assertEquals(expectedText, cellEditorState.displayText)
        assertEquals(expectedText, cellEditorState.rangeSelectorText)

        // x: drag to L20
        val c3 = CellAddress("L20")
        val range2 = RangeAddress(c, c3)
        mouseOnWsAction.makeMouseDragSelectionIfPossible(cursorLoc,c3)
        assertEquals(range2, rangeSelectorState.mainRange)
        val expectedText2 = "=1+${range2.rawLabel}"
        assertEquals(expectedText2, cellEditorState.displayText)
        assertEquals(expectedText2, cellEditorState.rangeSelectorText)

        // x: stop dragging
        mouseOnWsAction.stopDragSelection(cursorLoc)
        assertEquals(range2, rangeSelectorState.mainRange)
        assertEquals(expectedText2, cellEditorState.displayText)
        assertEquals(expectedText2, cellEditorState.rangeSelectorText)
    }


    @Test
    fun `test using range selector with clicking mouse`() {
        val wbk = ts.wbKey1Ms.value
        val wbk2 = ts.wbKey2Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.p6Comp.cellEditorAction()

        // x: open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        val rangeSelectorMs = cellEditorState.rangeSelectorCursorId?.let { appState.getCursorStateMs(it) }
        assertNotNull(rangeSelectorMs)
        cellEditorState = cellEditorState.setCurrentText("=1+")
        assertTrue(cellEditorState.allowRangeSelector)

        // x: click on a cell in the same sheet
        val clickOnCellAction: ClickOnCell = ts.p6Comp.clickOnCellAction()
        val c = CellAddress("M5")
        clickOnCellAction.clickOnCell(c, WbWs(wbk, wsn1))
        assertEquals(cellEditorState.targetCursorId, cellEditorState.rangeSelectorCursorId)
        val rangeSelectorState = appState.getCursorState(cellEditorState.rangeSelectorCursorId!!)
        assertEquals(c, rangeSelectorState?.mainCell)

        val expectText = "=1+${c.toRawLabel()}"
        assertEquals(expectText, cellEditorState.displayTextField.text)
        assertEquals(expectText, cellEditorState.rangeSelectorTextField?.text)
        assertEquals("=1+", cellEditorState.currentText)

        // x: click on another cell in the same sheet
        val c2 = CellAddress("L8")
        clickOnCellAction.clickOnCell(c2, WbWs(wbk, wsn1))
        assertEquals(cellEditorState.targetCursorId, cellEditorState.rangeSelectorCursorId)
        val expectText2 = "=1+${c2.toRawLabel()}"
        assertEquals(expectText2, cellEditorState.displayTextField.text)
        assertEquals(expectText2, cellEditorState.rangeSelectorTextField?.text)
        assertEquals("=1+", cellEditorState.currentText)

        // x: click on a cell in a different sheet, in a different workbook
        val c3 = CellAddress("Z78")
        clickOnCellAction.clickOnCell(c3, WbWs(wbk2, wsn1))
        assertEquals(appState.getCursorState(WbWs(wbk2, wsn1))?.id, cellEditorState.rangeSelectorCursorId)
        val expectText3 = "=1+${c3.toRawLabel()}@${wsn1}@${wbk2.name}"
        assertEquals(expectText3, cellEditorState.displayTextField.text)
        assertEquals(expectText3, cellEditorState.rangeSelectorTextField?.text)
        assertEquals("=1+", cellEditorState.currentText)
    }


    @Test
    fun `test running formula`() {
        val wbk = ts.wbKey1Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
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
        cellEditorAction.runFormulaOrSaveValueToCell()

        //
        assertEquals(6.0, appState.getCell(targetWb, targetWsName, targetCell)?.currentCellValue?.currentValue)
        assertNull(cellEditorState.targetCursorIdSt)
        assertNull(cellEditorState.targetCell)
        assertTrue(focusState.value.isCursorFocused)
        assertFalse(focusState.value.isEditorFocused)
    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun `test appending range selector address to cell editor when navigating and editing using keyboard`() {
        val wbk = ts.wbKey1Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        val cellEditorState by appState.cellEditorStateMs

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.p6Comp.cellEditorAction()

        // x: open cell editor on a cursor
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        val rangeSelectorMs = cellEditorState.rangeSelectorCursorId?.let { appState.getCursorStateMs(it) }
        assertNotNull(rangeSelectorMs)
        val rangeSelector by rangeSelectorMs

        // x: input text
        cellEditorAction.updateText("=1+")

        assertEquals("=1+", cellEditorState.currentText)
        assertEquals("=1+", cellEditorState.displayTextField.text)
        assertNull(cellEditorState.rangeSelectorTextField)
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
        assertEquals("=1+A2", cellEditorState.rangeSelectorTextField?.text)
        assertEquals("=1+A2", cellEditorState.displayTextField.text)
        assertEquals("=1+", cellEditorState.currentText)

        // x: handle another key event
        cellEditorAction.handleKeyboardEvent(arrowDownKey)
        assertTrue(cellEditorState.allowRangeSelector)
        assertEquals(CellAddress("A3"), rangeSelector.mainCell)
        assertEquals("=1+A3", cellEditorState.rangeSelectorTextField?.text)
        assertEquals("=1+A3", cellEditorState.displayTextField.text)
        assertEquals("=1+", cellEditorState.currentText)

        // x: handle key that ends range selector state
        val key = mock<PKeyEvent> {
            whenever(it.isRangeSelectorAcceptedKey()) doReturn false
        }
        cellEditorAction.handleKeyboardEvent(key)
        assertEquals("=1+A3", cellEditorState.currentText)
        assertNull(cellEditorState.rangeSelectorTextField)
        assertEquals("=1+A3", cellEditorState.displayTextField.text)
        assertFalse(cellEditorState.allowRangeSelector)

        // x: another case: moving from allow range selector to not allow by typing in a not tolerated character
        cellEditorAction.updateText("=1+A3+")
        assertTrue(cellEditorState.allowRangeSelector)
        assertNull(cellEditorState.rangeSelectorTextField)
        assertEquals("=1+A3+", cellEditorState.displayTextField.text)
        assertEquals("=1+A3+", cellEditorState.currentText)

        // x: simulate typing "1" into the cell editor
        cellEditorAction.updateText("=1+A3+1")
        assertFalse(cellEditorState.allowRangeSelector)
        assertNull(cellEditorState.rangeSelectorTextField?.text)
        assertEquals("=1+A3+1", cellEditorState.displayTextField.text)
        assertEquals("=1+A3+1", cellEditorState.currentText)
    }

    @Test
    fun `test passing key event from cell editor to range selector cursor`() {
        val wbk = ts.wbKey1Ms.value
        val wds = appState.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val wsn2 = ts.wsn2
        val cursor1Ms = appState.getCursorStateMs(wbk, wsn1)
        val wbAction = ts.p6Comp.workbookAction()
        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val spyCursorAction = spy(ts.p6Comp.cursorAction())
        val cellEditorAction: CellEditorAction = CellEditorActionImp(
            cellLiteralParser = p6Comp.cellLiteralParser(),
            updateCellAction = p6Comp.cellViewAction(),
            cursorAction = spyCursorAction,
            makeDisplayText = p6Comp.makeDisplayText(),
            openCellEditor = p6Comp.openCellEditorAction(),
            stateContMs = ts.stateContMs()
        )
        val keyEvent = mock<PKeyEvent> {
            whenever(it.isRangeSelectorAcceptedKey()) doReturn true
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
        val wbk = ts.wbKey1Ms.value
        val wbk2 = ts.wbKey2Ms.value
        val wsn1 = ts.wsn1
        val wsn2 = ts.wsn2
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
        val wbAction = ts.p6Comp.workbookAction()
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
        val moveWbAction = ts.p6Comp.moveToWbAction()
        val cursor3Ms = appState.getCursorStateMs(wbk2, wsn1)
        assertNotNull(cursor3Ms)
        moveWbAction.moveToWorkbook(wbk2)
        assertTrue { wds.value.focusState.isEditorFocused }
        assertEquals(cursor1Ms.value.idMs, cellEditorMs.value.targetCursorIdSt)
        assertEquals(cursor3Ms.value.idMs, cellEditorMs.value.rangeSelectorCursorIdSt)
    }

    @Test
    fun `test opened cell editor when moving to different wb`() {
        val wbk1 = ts.wbKey1Ms.value
        val wsn = "Sheet1"
        val wbk2 = ts.wbKey2Ms.value
        val cursorMs = appState.getCursorStateMs(wbk1, wsn)

        var ces by appState.cellEditorStateMs
        val wbTabBarAction: WorkbookTabBarAction = ts.p6Comp.wbTabBarAction()
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
        val wbk = ts.wbKey1Ms.value
        val wsn = "Sheet1"
        val cursorMs = appState.getCursorStateMs(wbk, wsn)
        val cellEditorMs = appState.cellEditorStateMs
        val wsAction = ts.p6Comp.wsAction()
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
        val wbk = ts.wbKey1Ms.value
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
        val wbAction = ts.p6Comp.workbookAction()
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
        val wbk = ts.wbKey1Ms.value
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
        val wbAction = ts.p6Comp.workbookAction()
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
