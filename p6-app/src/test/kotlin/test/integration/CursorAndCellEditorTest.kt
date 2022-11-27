package test.integration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsImp
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.qxdzbc.p6.app.action.worksheet.check_range_selector_state.CheckRangeSelectorStateActionImp.Companion.rangeSelectorActivationChars
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCell
import com.qxdzbc.p6.app.common.utils.CellLabelNumberSystem
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.cell_editor.RangeSelectorAllowState
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorActionImp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerSig
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerAction
import com.qxdzbc.p6.ui.window.formula_bar.FormulaBarState
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldBeEmpty
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import test.BaseTest
import test.test_implementation.MockP6KeyEvent
import kotlin.test.*

class CursorAndCellEditorTest : BaseTest() {
    val cellEditorAction get() = comp.cellEditorAction()
    val cursorAction get()=comp.cursorAction()

    @Test
    fun `bug-range selector is wrongfully turned on by non-formula text`(){
        val wbwsSt = sc.getWbWsSt(WbWsImp(ts.wbKey1, ts.wsn1))!!
        val cellEditorState by appState.cellEditorStateMs
        cellEditorAction.openCellEditor(wbwsSt)
        cellEditorAction.changeText("/")
        cellEditorState.rangeSelectorAllowState shouldBe RangeSelectorAllowState.DISALLOW
    }

    @Test
    fun `bug-input and run formula in a cell, then open editor in another cell, cell highlight information should be empty`(){
        val wbwsSt = sc.getWbWsSt(WbWsImp(ts.wbKey1, ts.wsn1))!!
        val cursorState by sc.getCursorStateMs(wbwsSt)!!

        cellEditorAction.openCellEditor(wbwsSt)
        cellEditorAction.changeText("=B2+F10+123")
        cellEditorAction.runFormulaOrSaveValueToCell()

        cursorAction.moveCursorTo(wbwsSt,"C1")

        cursorState.mainCell shouldBe CellAddress("C1")
        cellEditorAction.openCellEditor(wbwsSt)
        val hightlightInfo =cursorAction.getFormulaRangeAndColor(wbwsSt)
        hightlightInfo.shouldBeEmpty()
    }

    @Test
    fun `bug-test open cell editor on cel with error formula`(){
        val wbwsSt = sc.getWbWsSt(WbWsImp(ts.wbKey1, ts.wsn1))!!
        val cellEditorState by appState.cellEditorStateMs
        cellEditorAction.openCellEditor(wbwsSt)
        cellEditorAction.changeText("=B2+ww")
        cellEditorAction.runFormulaOrSaveValueToCell()
        val cellMs = sc.getCellMs(ts.wbKey1,ts.wsn1,CellAddress("A1"))!!

        cellEditorAction.openCellEditor(wbwsSt)
        cellEditorState.displayText shouldBe "=B2+ww"


    }

    @Test
    fun `click on ruler item when editing cell, allow range select`() {
        val rulerAction: RulerAction = ts.comp.rulerAction()
        val wbwsSt = sc.getWbWsSt(WbWsImp(ts.wbKey1, ts.wsn1))
        assertNotNull(wbwsSt)
        val rulerSig = object : RulerSig {
            override val type: RulerType
                get() = RulerType.Col
            override val wbKeySt: St<WorkbookKey>
                get() = wbwsSt.wbKeySt
            override val wsNameSt: St<String>
                get() = wbwsSt.wsNameSt
        }

        cellEditorAction.openCellEditor(wbwsSt)
        cellEditorAction.changeText("=")
        val col = 20
        val col2 = 220
        val colLabel = CellLabelNumberSystem.numberToLabel(col)
        val colLabel2 = CellLabelNumberSystem.numberToLabel(col2)

        assertEquals("=", sc.cellEditorState.displayText)
        rulerAction.clickRulerItem(col, rulerSig)
        assertEquals("=${colLabel}:${colLabel}", sc.cellEditorState.displayText)
        rulerAction.clickRulerItem(col2, rulerSig)
        assertEquals("=${colLabel2}:${colLabel2}", sc.cellEditorState.displayText)
    }

    @Test
    fun `test short formula conversion function`() {
        val wbk = ts.wbKey1Ms.value
        val wds = ts.sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = ts.sc.getCursorStateMs(wbk, wsn1)
        val cellEditorState by appState.cellEditorStateMs

        assertNotNull(wds)
        assertNotNull(cursor1Ms)

        val cellEditorAction: CellEditorAction = ts.comp.cellEditorAction()
        cellEditorAction.openCellEditor(cursor1Ms.value)
        cellEditorAction.changeText("=SUM(B2:C4)")
        cellEditorAction.runFormulaOrSaveValueToCell()
        cellEditorAction.openCellEditor(cursor1Ms.value)
        val formulaBar: FormulaBarState? =
            ts.stateContMs().value.getWindowStateMsByWbKey(cursor1Ms.value.wbKey)?.value?.formulaBarState
        assertEquals("=SUM(B2:C4)", cellEditorState.currentText)
    }

    @Test
    fun `Shift click while range selector is activated`() {
        val wbk = ts.wbKey1Ms.value
        val wbk2 = ts.wbKey2Ms.value
        val wds = ts.sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = ts.sc.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs
        val mouseOnWsAction: MouseOnWorksheetAction = ts.comp.mouseOnWsAction()

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.comp.cellEditorAction()

        // x: open cell editor on a worksheet
        val cursorLoc = WbWs(wbk, wsn1)
        cellEditorAction.openCellEditor(cursorLoc)

        cellEditorAction.changeText("=1+")
//        cellEditorState = cellEditorState.setCurrentText("=1+")
        assertTrue(cellEditorState.allowRangeSelector)

        // x: click
        val c = CellAddress("B5")
        mouseOnWsAction.clickOnCell(c, cursorLoc)
        val rangeSelector by sc.getCursorStateMs(cellEditorState.rangeSelectorCursorId!!)!!
        val c2 = CellAddress("K9")
        mouseOnWsAction.shiftClickSelectRange(c2, cursorLoc)
        val r = RangeAddress(c, c2)
        assertEquals(r, rangeSelector.mainRange)
        val t = "=1+${r.rawLabel}"
        assertEquals(t, cellEditorState.rangeSelectorText)
        assertEquals(t, cellEditorState.displayText)
        assertEquals("=1+", cellEditorState.currentText)
    }


    @Test
    fun `tes ctrl click while range selector is NOT active but editor is active`() {
        val wbk = ts.wbKey1Ms.value
        val wds = sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = sc.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs
        val mouseOnWsAction: MouseOnWorksheetAction = ts.comp.mouseOnWsAction()

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.comp.cellEditorAction()

        // x: open cell editor on a worksheet
        val cursorLoc = WbWs(wbk, wsn1)
        cellEditorAction.openCellEditor(cursorLoc)
        cellEditorAction.changeText("=123")
//        cellEditorState = cellEditorState.setCurrentText("=123")
        val rangeSelectorId = cellEditorState.rangeSelectorCursorId
        assertNotNull(rangeSelectorId)

        // x: ctrl click
        val c = CellAddress("B5")
        mouseOnWsAction.ctrlClickSelectCell(c, cursorLoc)
        assertFalse { cellEditorState.isOpen }
        assertEquals(listOf(c), sc.getCursorState(rangeSelectorId)?.fragmentedCells?.toList())
    }

    @Test
    fun `tes ctrl click while range selector is active`() {
        val wbk = ts.wbKey1Ms.value
        val wbk2 = ts.wbKey2Ms.value
        val wds = sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = sc.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs
        val mouseOnWsAction: MouseOnWorksheetAction = ts.comp.mouseOnWsAction()

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.comp.cellEditorAction()

        // x: open cell editor on a worksheet
        val cursorLoc = WbWs(wbk, wsn1)
        cellEditorAction.openCellEditor(cursorLoc)
        cellEditorAction.changeText("=1+")
//        cellEditorState = cellEditorState.setCurrentText("=1+")
        assertTrue(cellEditorState.allowRangeSelector)

        // x: click
        val c = CellAddress("B5")
        mouseOnWsAction.clickOnCell(c, cursorLoc)
        val expectedText = "=1+${c.label}"
        val rangeSelector by sc.getCursorStateMs(cellEditorState.rangeSelectorCursorId!!)!!
        fun test() {
            assertEquals(expectedText, cellEditorState.displayText)
            assertEquals(expectedText, cellEditorState.rangeSelectorText)
            assertNull(rangeSelector.mainRange)
            assertEquals(c, rangeSelector.mainCell)
            assertTrue(rangeSelector.fragmentedRanges.isEmpty())
            assertTrue(rangeSelector.fragmentedCells.isEmpty())
        }
        test()

        // x: ctrl click
        mouseOnWsAction.ctrlClickSelectCell(CellAddress("K9"), cursorLoc)
        test()

    }

    @Test
    fun `test using range selector with dragging mouse on different sheet`() {
        val wbk = ts.wbKey1Ms.value
        val wbk2 = ts.wbKey2Ms.value
        val wds = sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = sc.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs
        val mouseOnWsAction: MouseOnWorksheetAction = ts.comp.mouseOnWsAction()

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.comp.cellEditorAction()

        // x: open cell editor on a wsn1/wbk
        val cursorLoc = WbWs(wbk, wsn1)
        cellEditorAction.openCellEditor(cursorLoc)
        cellEditorAction.changeText("=1+")
        assertTrue(cellEditorState.allowRangeSelector)

        // x: start dragging on I16/wsn1/wbk2
        val cursorLoc2 = WbWs(wbk2, wsn1)
        val c = CellAddress("I16")
        mouseOnWsAction.startDragSelection(cursorLoc2, c)
        val rangeSelectorMs = cellEditorState.rangeSelectorCursorId?.let { sc.getCursorStateMs(it) }
        assertNotNull(rangeSelectorMs)
        assertEquals(sc.getCursorState(cursorLoc2)?.id, cellEditorState.rangeSelectorCursorId)
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
        mouseOnWsAction.makeMouseDragSelectionIfPossible(cursorLoc2, c3)
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
        val wds = sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = sc.getCursorStateMs(wbk, wsn1)
        var cellEditorState by appState.cellEditorStateMs
        val mouseOnWsAction: MouseOnWorksheetAction = ts.comp.mouseOnWsAction()

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.comp.cellEditorAction()

        // x: open cell editor on a worksheet
        val cursorLoc = WbWs(wbk, wsn1)
        cellEditorAction.openCellEditor(cursorLoc)
        val rangeSelectorMs = cellEditorState.rangeSelectorCursorId?.let { sc.getCursorStateMs(it) }
        assertNotNull(rangeSelectorMs)
        cellEditorAction.changeText("=1+")
//        cellEditorState = cellEditorState.setCurrentText("=1+")
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
        mouseOnWsAction.makeMouseDragSelectionIfPossible(cursorLoc, c3)
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

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun `bug - click cell when text cursor in A1+|123`() {
        val wbk = ts.wbKey1Ms.value
        val wbk2 = ts.wbKey2Ms.value
        val wds = sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = sc.getCursorStateMs(wbk, wsn1)
        val cellEditorState by appState.cellEditorStateMs

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.comp.cellEditorAction()

        // x: open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        assertEquals(RangeSelectorAllowState.START, cellEditorState.rangeSelectorAllowState)
        val rangeSelectorMs = cellEditorState.rangeSelectorCursorId?.let { sc.getCursorStateMs(it) }

        // x: pre-condition
        assertNotNull(rangeSelectorMs)
        val t0 = "=A1+123"
        cellEditorAction.changeTextField(
            TextFieldValue(
                text = t0,
                selection = TextRange(t0.length)
            )
        )
        // x: cursor is at '3' => DISALLOW
        assertFalse(cellEditorState.allowRangeSelector)
        assertEquals(RangeSelectorAllowState.DISALLOW, cellEditorState.rangeSelectorAllowState)
        cellEditorAction.changeTextField(
            TextFieldValue(
                text = t0,
                selection = TextRange(4)
            )
        )
        assertEquals(RangeSelectorAllowState.ALLOW_MOUSE, cellEditorState.rangeSelectorAllowState)
        // x: click on a cell in the same sheet
        val clickOnCellAction: ClickOnCell = ts.comp.clickOnCellAction()
        val c = CellAddress("M5")
        clickOnCellAction.clickOnCell(c, WbWs(wbk, wsn1))
        // x: post-condition
        assertEquals(cellEditorState.targetCursorId, cellEditorState.rangeSelectorCursorId)
        val rangeSelectorState = sc.getCursorState(cellEditorState.rangeSelectorCursorId!!)
        assertEquals(c, rangeSelectorState?.mainCell)
        val expectText = "=A1+${c.label}123"
        assertEquals(expectText, cellEditorState.displayTextField.text)
        assertEquals(expectText, cellEditorState.rangeSelectorTextField?.text)
        assertEquals(t0, cellEditorState.currentText)
        val focusState = sc.getFocusStateMsByWbKey(cellEditorState.targetWbKey!!)!!.value
        assertTrue(focusState.isEditorFocused)

        listOf(
            "a", "+", "?","@",">","<","#",
            "*",".","/",">=","<=",
            "!","$","%","^","&",
           "|","\\","-","_","~","`"
        ).forEach { chr ->
            //"=A1+M5a123"
            //"=A1+M5+123"
            //"=A1+M5?123"
            // ...
            val expectText2 = "=A1+${c.label}${chr}123"
            cellEditorAction.changeTextField(
                TextFieldValue(
                    text = expectText2,
                    selection = TextRange(7)
                )
            )
            assertEquals(expectText2, cellEditorState.displayText, "violating char: ${chr}")
        }
    }
    @Test
    fun `test input text after generate formula using range selector`() {
        val wbk = ts.wbKey1Ms.value
        val wds = sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = sc.getCursorStateMs(wbk, wsn1)
        val cellEditorState by appState.cellEditorStateMs
        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.comp.cellEditorAction()
        // x: open cell editor on a worksheet
        val wbws = WbWsImp(wbk, wsn1)
        test("input '+' into a cell editor after clicking on a cell"){
            cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
            val text="=D3+"
            val clickOnCellAction: ClickOnCell = ts.comp.clickOnCellAction()
            cellEditorAction.changeText("=")
            clickOnCellAction.clickOnCell(CellAddress("D3"),wbws)
            cellEditorAction.changeText(text)
            postCondition {
                cellEditorState.displayText shouldBe text
            }
            cellEditorAction.closeEditor()
        }

        test("input 'a' into a cell editor after clicking on a cell"){
            cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
            val text="=D3a"
            val clickOnCellAction: ClickOnCell = ts.comp.clickOnCellAction()
            cellEditorAction.changeText("=")
            clickOnCellAction.clickOnCell(CellAddress("D3"),wbws)
            cellEditorAction.changeText(text)
            postCondition {
                cellEditorState.displayText shouldBe text
            }
        }
    }

    @Test
    fun `test click on cell while range selector is enabled`() {
        val wbk = ts.wbKey1Ms.value
        val wbk2 = ts.wbKey2Ms.value
        val wds = sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = sc.getCursorStateMs(wbk, wsn1)
        val cellEditorState by appState.cellEditorStateMs
        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.comp.cellEditorAction()

        // x: open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))

        val rangeSelectorMs = cellEditorState.rangeSelectorCursorId?.let { sc.getCursorStateMs(it) }

        val clickOnCellAction: ClickOnCell = ts.comp.clickOnCellAction()
        test(" click on a cell in the same sheet") {
            preCondition {
                rangeSelectorMs shouldNotBe null
                cellEditorAction.changeText("=1+")
                cellEditorState.allowRangeSelector shouldBe true
            }
            val c = CellAddress("M5")
            clickOnCellAction.clickOnCell(c, WbWs(wbk, wsn1))
            postCondition {
                cellEditorState.rangeSelectorCursorId shouldBe cellEditorState.targetCursorId
                val rangeSelectorState = sc.getCursorState(cellEditorState.rangeSelectorCursorId!!)
                rangeSelectorState?.mainCell shouldBe c
                val expectText = "=1+${c.label}"
                cellEditorState.displayTextField.text shouldBe expectText
                cellEditorState.rangeSelectorTextField?.text shouldBe expectText
                cellEditorState.currentText shouldBe "=1+"
                cellEditorState.displayTextField.annotatedString.spanStyles.shouldNotBeEmpty()
            }
        }

        test("click on another cell in the same sheet") {
            val c2 = CellAddress("L8")
            clickOnCellAction.clickOnCell(c2, WbWs(wbk, wsn1))
            postCondition {
                val expectText2 = "=1+${c2.label}"
                assertEquals(cellEditorState.targetCursorId, cellEditorState.rangeSelectorCursorId)
                assertEquals(expectText2, cellEditorState.displayTextField.text)
                assertEquals(expectText2, cellEditorState.rangeSelectorTextField?.text)
                assertEquals("=1+", cellEditorState.currentText)
            }
        }

        test("click on a cell in a different sheet, in a different workbook"){
            val c3 = CellAddress("Z78")
            clickOnCellAction.clickOnCell(c3, WbWs(wbk2, wsn1))
            postCondition {
                assertEquals(sc.getCursorState(WbWs(wbk2, wsn1))?.id, cellEditorState.rangeSelectorCursorId)
                val expectText3 = "=1+${c3.label}@${wsn1}@${wbk2.name}"
                assertEquals(expectText3, cellEditorState.displayTextField.text)
                assertEquals(expectText3, cellEditorState.rangeSelectorTextField?.text)
                assertEquals("=1+", cellEditorState.currentText)
            }
        }
    }


    @Test
    fun `test running formula`() {
        val wbk = ts.wbKey1Ms.value
        val wds = sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = sc.getCursorStateMs(wbk, wsn1)
        val cellEditorState by appState.cellEditorStateMs

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
        val focusState = sc.getFocusStateMsByWbKey(targetWb)
        assertNotNull(focusState)

        // x: input formula
        cellEditorAction.changeText("=1+2+3")

        // x: run the formula
        cellEditorAction.runFormulaOrSaveValueToCell()

        assertEquals(6.0, sc.getCellOrDefault(targetWb, targetWsName, targetCell)?.currentCellValue?.value)
        assertNull(cellEditorState.targetCursorIdSt)
        assertNull(cellEditorState.targetCell)
        assertTrue(focusState.value.isCursorFocused)
        assertFalse(focusState.value.isEditorFocused)
    }


    @Test
    fun `test appending range selector address to cell editor when navigating and editing using keyboard`() {
        val wbk = ts.wbKey1Ms.value
        val wds = sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = sc.getCursorStateMs(wbk, wsn1)
        val cellEditorState by appState.cellEditorStateMs

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.comp.cellEditorAction()

        // x: open cell editor on a cursor
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        val rangeSelectorMs = cellEditorState.rangeSelectorCursorId?.let { sc.getCursorStateMs(it) }
        assertNotNull(rangeSelectorMs)
        val rangeSelector by rangeSelectorMs

        /**
         * input a text that enables the range selector, then move around using keyboard arrows
         */
        fun stdCase() {
            // x: input text
            cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
            cellEditorAction.changeText("=1+")
            assertEquals("=1+", cellEditorState.currentText)
            assertEquals("=1+", cellEditorState.displayTextField.text)
            assertEquals("=1+", cellEditorState.rangeSelectorText)
            assertTrue(cellEditorState.allowRangeSelector)

            // x: issue arrow down key event
            val arrowDownKey = MockP6KeyEvent.arrowDown
            cellEditorAction.handleKeyboardEvent(arrowDownKey)

            // x: check state after handle keyboard event
            assertTrue(cellEditorState.allowRangeSelector)
            assertEquals(CellAddress("A2"), rangeSelector.mainCell)
            assertEquals("=1+A2", cellEditorState.rangeSelectorTextField?.text)
            assertEquals(TextRange(5), cellEditorState.rangeSelectorTextField?.selection)
            assertEquals("=1+A2", cellEditorState.displayTextField.text)
            assertEquals(TextRange(5), cellEditorState.displayTextField.selection)
            assertEquals("=1+", cellEditorState.currentText)

            // x: handle another down array key event
            cellEditorAction.handleKeyboardEvent(arrowDownKey)
            assertTrue(cellEditorState.allowRangeSelector)
            assertEquals(CellAddress("A3"), rangeSelector.mainCell)
            assertEquals("=1+A3", cellEditorState.rangeSelectorTextField?.text)
            assertEquals(TextRange(5), cellEditorState.rangeSelectorTextField?.selection)
            assertEquals("=1+A3", cellEditorState.displayTextField.text)
            assertEquals(TextRange(5), cellEditorState.displayTextField.selection)
            assertEquals("=1+", cellEditorState.currentText)
            cellEditorAction.closeEditor()
        }

        fun handleKeyEvenThatDisableRangeSelector() {
            // x: handle key event that ends range selector state
            // x: temporary text is copied to the current text field.
            val key = MockP6KeyEvent(
                key = mock(),
                type = KeyEventType.KeyDown,
                isAcceptedByRangeSelector = false,
                isRangeSelectorNavKey = false,
            )
            cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
            cellEditorAction.changeText("=1+A3")
            cellEditorAction.handleKeyboardEvent(key)
            assertEquals("=1+A3", cellEditorState.currentText)
            assertNull(cellEditorState.rangeSelectorTextField)
            assertEquals("=1+A3", cellEditorState.displayTextField.text)
            assertFalse(cellEditorState.allowRangeSelector)
            cellEditorAction.closeEditor()
        }

        /**
         *  moving from allow range selector to NOT allow by typing in a not tolerated character
         */
        fun disableRangeSelectorWithNewText() {
            cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
            cellEditorAction.changeText("=1+A3+")
            assertTrue(cellEditorState.allowRangeSelector)
            assertNull(cellEditorState.rangeSelectorTextField)
            assertEquals("=1+A3+", cellEditorState.displayTextField.text)
            assertEquals("=1+A3+", cellEditorState.currentText)

            // x: simulate typing "1" into the cell editor
            cellEditorAction.changeText("=1+A3+1")
            assertFalse(cellEditorState.allowRangeSelector)
            assertNull(cellEditorState.rangeSelectorTextField?.text)
            assertEquals("=1+A3+1", cellEditorState.displayTextField.text)
            assertEquals("=1+A3+1", cellEditorState.currentText)
            cellEditorAction.closeEditor()
        }

        fun `select range after braces auto completion`() {
            cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
            // x: select range after braces "()" auto completion
            cellEditorAction.changeTextField(
                TextFieldValue(
                    "=()",
                    TextRange(2) // cursor right behind the "("
                )
            )
            assertTrue(cellEditorState.allowRangeSelector)
            cellEditorAction.handleKeyboardEvent(MockP6KeyEvent.arrowDown)
            val displayTextField = cellEditorState.displayTextField
            assertEquals("=(A2)", displayTextField.text)
            assertEquals(TextRange(4), displayTextField.selection)
            cellEditorAction.closeEditor()
        }

        stdCase()
        // TODO move the below tests out
//        disableRangeSelectorWithNewText()
//        handleKeyEvenThatDisableRangeSelector()
//        `select range after braces auto completion`()
    }

    @Test
    fun `test formula text generated by range selector after activation char`() {
//        for (ac in rangeSelectorActivationChars){
//            `test formula text generated by range selector after activation char`(ac)
//        }
        `test formula text generated by range selector after activation char`('+')
    }

    fun `test formula text generated by range selector after activation char`(activationChar: Char) {
        val wbk = ts.wbKey1Ms.value
        val wds = sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val cursor1Ms = sc.getCursorStateMs(wbk, wsn1)
        val cellEditorState by appState.cellEditorStateMs

        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val cellEditorAction: CellEditorAction = ts.comp.cellEditorAction()

        // x: open cell editor on a cursor
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        val rangeSelectorMs = cellEditorState.rangeSelectorCursorId?.let { sc.getCursorStateMs(it) }
        assertNotNull(rangeSelectorMs)
        val rangeSelector by rangeSelectorMs
        // x: input text
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        val initText = "=A1" + activationChar
        cellEditorAction.changeText(initText)
        assertEquals(initText, cellEditorState.currentText)
        assertEquals(initText, cellEditorState.displayTextField.text, "activation char= ${activationChar}")
        assertEquals(initText, cellEditorState.rangeSelectorText, "activation char= ${activationChar}")
//        assertNull(cellEditorState.rangeSelectorTextField)
        assertTrue(cellEditorState.allowRangeSelector)

        // x: issue arrow down key event
        val arrowDownKey = MockP6KeyEvent.arrowDown
        cellEditorAction.handleKeyboardEvent(arrowDownKey)

        // x: check state after handle keyboard event
        assertTrue(cellEditorState.allowRangeSelector)
        assertEquals(CellAddress("A2"), rangeSelector.mainCell)
        val expectText = initText + "A2"
        assertEquals(expectText, cellEditorState.rangeSelectorTextField?.text)
        assertEquals(expectText, cellEditorState.displayTextField.text)
        assertEquals(initText, cellEditorState.currentText)

        // x: handle another down array key event
        cellEditorAction.handleKeyboardEvent(arrowDownKey)
        assertTrue(cellEditorState.allowRangeSelector)
        assertEquals(CellAddress("A3"), rangeSelector.mainCell)
        val expectText2 = initText + "A3"
        assertEquals(expectText2, cellEditorState.rangeSelectorTextField?.text)
        assertEquals(expectText2, cellEditorState.displayTextField.text)
        assertEquals(initText, cellEditorState.currentText)
    }

    @Test
    fun `test passing key event from cell editor to range selector cursor`() {
        val wbk = ts.wbKey1Ms.value
        val wds = sc.getWindowStateMsByWbKey(wbk)
        val wsn1 = ts.wsn1
        val wsn2 = ts.wsn2
        val cursor1Ms = sc.getCursorStateMs(wbk, wsn1)
        val wbAction = ts.comp.workbookAction()
        assertNotNull(wds)
        assertNotNull(cursor1Ms)
        val spyCursorAction = spy(ts.comp.cursorAction())
        val cellEditorAction: CellEditorAction = CellEditorActionImp(
            cellLiteralParser = comp.cellLiteralParser(),
            updateCellAction = comp.cellViewAction(),
            handleCursorKeyboardEventAct = spyCursorAction,
            makeDisplayText = comp.makeDisplayText(),
            openCellEditor = comp.openCellEditorAction(),
            stateContMs = ts.stateContMs(),
            textDiffer = comp.textDiffer(),
            cycleLockStateAct = comp.cycleFormulaLockStateAct(),
            treeExtractor = comp.partialFormulaTreeExtractor(),
            colorFormulaAction = comp.colorFormulaAction()
        )

        val keyEvent = MockP6KeyEvent.arrowDown
        // x: open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        cellEditorAction.changeText("=")

        // x: move to another sheet
        val request = SetActiveWorksheetRequest(
            wbKey = wbk,
            wsName = wsn2,
        )
        wbAction.switchToWorksheet(request)
        val rangeCursorMs = sc.getCursorStateMs(request)
        assertNotNull(rangeCursorMs)

        cellEditorAction.handleKeyboardEvent(keyEvent)
        verify(spyCursorAction).handleKeyboardEvent(
            keyEvent,
            appState.cellEditorState.rangeSelectorCursorId!!
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
        val cursor1Ms = sc.getCursorStateMs(wbk, wsn1)
        val cellEditorMs = appState.cellEditorStateMs
        val wds = sc.getWindowStateMsByWbKey(wbk)

        assertNotNull(wds)
        assertNotNull(cursor1Ms)

        // x: open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn1))
        cellEditorAction.changeText("=")
        assertEquals(cursor1Ms.value.idMs, cellEditorMs.value.targetCursorIdSt)
        assertEquals(cursor1Ms.value.idMs, cellEditorMs.value.rangeSelectorCursorIdSt)

        // x:switch sheet
        val wbAction = ts.comp.workbookAction()
        wbAction.setFocusStateConsideringRangeSelector(wbk)
        val request = SetActiveWorksheetRequest(
            wbKey = wbk,
            wsName = wsn2,
        )
        wbAction.switchToWorksheet(request)
        val cursor2Ms = sc.getCursorStateMs(wbk, wsn2)
        assertNotNull(cursor2Ms)
        assertEquals(cursor1Ms.value.idMs, cellEditorMs.value.targetCursorIdSt)
        assertEquals(cursor2Ms.value.idMs, cellEditorMs.value.rangeSelectorCursorIdSt)


        // x: switch workbook
        val moveWbAction = ts.comp.moveToWbAction()
        val cursor3Ms = sc.getCursorStateMs(wbk2, wsn1)
        assertNotNull(cursor3Ms)
        moveWbAction.moveToWorkbook(wbk2)
        assertEquals(cursor1Ms.value.idMs, cellEditorMs.value.targetCursorIdSt)
        assertEquals(cursor3Ms.value.idMs, cellEditorMs.value.rangeSelectorCursorIdSt)
    }

    @Test
    fun `test opened cell editor when moving to different wb`() {
        val wbk1 = ts.wbKey1Ms.value
        val wsn = "Sheet1"
        val wbk2 = ts.wbKey2Ms.value
        val cursorMs = sc.getCursorStateMs(wbk1, wsn)

        val ces by appState.cellEditorStateMs
        val wbTabBarAction: WorkbookTabBarAction = ts.comp.wbTabBarAction()
        assertNotNull(cursorMs)

        // x: open cursor editor + set normal text
        cellEditorAction.openCellEditor(WbWsImp(wbk1, wsn))
        cellEditorAction.changeText("abc")

        // x: move to another workbook
        wbTabBarAction.moveToWorkbook(wbk2)
        //
        assertFalse(ces.isOpen)
        assertTrue(sc.getFocusStateMsByWbKey(wbk2)?.value?.isCursorFocused!!)
        assertFalse(sc.getFocusStateMsByWbKey(wbk2)?.value?.isEditorFocused!!)

        // x: open cursor editor + set range-selector-enable text
        cellEditorAction.openCellEditor(WbWsImp(wbk2, wsn))
        cellEditorAction.changeText("=")
        wbTabBarAction.moveToWorkbook(wbk1)
        assertTrue(ces.isOpen)
    }

    @Test
    fun `test click,drag on cell while cell editor is opened`() {
        val wbk = ts.wbKey1Ms.value
        val wsn = "Sheet1"
        val cursorMs = sc.getCursorStateMs(wbk, wsn)
        val cellEditorMs = appState.cellEditorStateMs
        val wsAction = ts.comp.wsAction()
        val wds = sc.getWindowStateMsByWbKey(wbk)
        val wsStateMs = sc.getWsStateMs(wbk, wsn)

        cursorMs.shouldNotBeNull()
        wds.shouldNotBeNull()
        wsStateMs.shouldNotBeNull()

        val wsState by wsStateMs
        // open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn))
        // click on another cell
        assertTrue(cellEditorMs.value.isOpen)
        val clickedCell = CellAddress("D10")
        wsAction.startDragSelection(wsState, clickedCell)
        //
        cellEditorMs.value.isOpen shouldBe false
        cellEditorMs.value.currentText shouldBe ""
        cursorMs.value.mainCell shouldBe clickedCell
        cursorMs.value.fragmentedCells.isEmpty() shouldBe true
        cursorMs.value.fragmentedRanges.isEmpty() shouldBe true
        cursorMs.value.mainRange shouldBe null
        cursorMs.value.allFragCells shouldBe listOf(clickedCell)

        // open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn))
        val text2 = "=123+"
        cellEditorAction.changeText(text2)

        val clickedCell2 = CellAddress("K8")
        wsAction.startDragSelection(wsState, clickedCell2)

        cellEditorMs.value.isOpen shouldBe true
        cellEditorMs.value.currentText shouldBe text2
        cursorMs.value.mainCell shouldBe clickedCell2
        // drag the mouse
        val dragCell = CellAddress("Q30")
        wsAction.makeMouseDragSelectionIfPossible(wsState, dragCell)
        cellEditorMs.value.isOpen shouldBe true
        cellEditorMs.value.currentText shouldBe text2
        cursorMs.value.mainCell shouldBe clickedCell2

        cursorMs.value.mainRange shouldBe RangeAddress(clickedCell2, dragCell)

        // stop dragging
        wsAction.stopDragSelection(wsState)
        cellEditorMs.value.isOpen shouldBe true
        cellEditorMs.value.currentText shouldBe text2
        cursorMs.value.mainCell shouldBe clickedCell2
        cursorMs.value.mainRange shouldBe RangeAddress(clickedCell2, dragCell)
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
        val cursorMs = sc.getCursorStateMs(wbk, wsn)
        val cellEditorMs = appState.cellEditorStateMs
        cursorMs shouldNotBe null

        // open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn))
        val wds = sc.getWindowStateMsByWbKey(wbk)
        wds.shouldNotBeNull()


        // switch sheet
        val wbAction = ts.comp.workbookAction()
        wbAction.setFocusStateConsideringRangeSelector(wbk)
        val request = SetActiveWorksheetRequest(
            wbKey = wbk,
            wsName = "Sheet2",
        )
        wbAction.switchToWorksheet(request)

        cellEditorMs.value.isOpen shouldBe false
        cellEditorMs.value.currentText.shouldBeEmpty()

        wds.value.focusState.isEditorFocused shouldBe false
        wds.value.focusState.isCursorFocused shouldBe true
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
        val cursorMs = sc.getCursorStateMs(wbk, wsn)
        val cellEditorMs = appState.cellEditorStateMs
        cursorMs shouldNotBe null

        // open cell editor on a worksheet
        cellEditorAction.openCellEditor(WbWsImp(wbk, wsn))
        val wds = sc.getWindowStateMsByWbKey(wbk)
        wds shouldNotBe null

        // add content
        cellEditorAction.changeText("=123+")
        assertTrue(cellEditorMs.value.allowRangeSelector)

        // switch sheet
        val wbAction = ts.comp.workbookAction()
        val request = SetActiveWorksheetRequest(
            wbKey = wbk,
            wsName = "Sheet2",
        )
        wbAction.switchToWorksheet(request)

        // expect:
        cellEditorMs.value.isOpen shouldBe true
        cellEditorMs.value.currentText shouldBe "=123+"
    }
}

