package com.qxdzbc.p6.app.action.cursor.handle_cursor_keyboard_event

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.cursor.copy_cursor_range_to_clipboard.CopyCursorRangeToClipboardAction
import com.qxdzbc.p6.app.action.cursor.on_cursor_changed_reactor.CommonReactionOnCursorChanged
import com.qxdzbc.p6.app.action.cursor.paste_range_to_cursor.PasteRangeToCursor
import com.qxdzbc.p6.app.action.cursor.undo_on_cursor.UndoRedoAction
import com.qxdzbc.p6.app.action.window.tool_bar.UpdateFormatIndicator
import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAtCursorRequest
import com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell.MoveSliderAction
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddressUtils
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell.SelectWholeColumnForAllSelectedCellAction
import com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells.SelectWholeRowForAllSelectedCellAction
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class)
@P6Singleton
@ContributesBinding(scope = P6AnvilScope::class)
class HandleCursorKeyboardEventActionImp @Inject constructor(

    private val wsAction: WorksheetAction,
    private val openCellEditor: OpenCellEditorAction,
    private val stateCont:StateContainer,
    private val pasteRangeToCursorAction: PasteRangeToCursor,
    private val selectWholeCol: SelectWholeColumnForAllSelectedCellAction,
    private val selectWholeRow: SelectWholeRowForAllSelectedCellAction,
    private val moveSliderAction: MoveSliderAction,
    private val copyCursorRangeToClipboardAction: CopyCursorRangeToClipboardAction,
    private val undoOnCursorAct: UndoRedoAction,
    private val cellEditorActionLz: dagger.Lazy<CellEditorAction>,
    private val commonReactionOnCursorChanged: CommonReactionOnCursorChanged,
    private val updateFormatIndicator:UpdateFormatIndicator,

) : HandleCursorKeyboardEventAction {

    private val sc  = stateCont
    private val cellEditorAction:CellEditorAction get()=cellEditorActionLz.get()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun handleKeyboardEvent(
        keyEvent: P6KeyEvent,
        wbws: WbWs,
    ): Boolean {
        val wsState = sc.getWsState(wbws)
        if (wsState != null) {
            val cursorState by wsState.cursorStateMs
            return if (keyEvent.type == KeyEventType.KeyDown) {
                if (keyEvent.isCtrlPressedAlone) {
                    handleKeyWithCtrlDown(keyEvent, wsState)
                } else if (keyEvent.isShiftPressedAlone) {
                    handleKeyboardEventWithShiftDown(keyEvent, cursorState)
                } else if (keyEvent.isCtrlShiftPressed) {
                    handleKeyWithCtrlShift(keyEvent, wsState)
                } else {
                    when (keyEvent.key) {
                        Key.Escape -> {
                            wsState.cursorStateMs.value = cursorState.removeClipboardRange()
                            true
                        }

                        Key.Delete -> {
                            _onDeleteKey(cursorState)
                            true
                        }

                        Key.F2 -> {
                            f2(wsState)
                            true
                        }

                        Key.DirectionUp -> {
                            up(wsState)
                            true
                        }

                        Key.DirectionDown, Key.Enter -> {
                            down(wsState)
                            true
                        }

                        Key.DirectionLeft -> {
                            left(wsState)
                            true
                        }

                        Key.DirectionRight -> {
                            right(wsState)
                            true
                        }

                        Key.Home -> {
                            this.home(wsState)
                            true
                        }

                        Key.MoveEnd -> {
                            this.end(wsState)
                            true
                        }
                        Key.PageDown->{
                            this.pageDown(wsState)
                            true
                        }
                        Key.PageUp->{
                            this.pageUp(wsState)
                            true
                        }
                        else -> {
                            handleOtherKey(keyEvent, wsState)
                        }
                    }
                }
            } else {
                false
            }

        } else {
            return false
        }
    }

    fun handleOtherKey(keyEvent: P6KeyEvent, wbwsSt: WbWsSt): Boolean {
        if (keyEvent.isTextual()) {
            if (sc.cellEditorState.isNotOpen) {
                openCellEditor.openCellEditor(wbwsSt)
            }
            return passKeyEventToCellEditor(keyEvent)
        } else {
            return false
        }
    }

    private fun passKeyEventToCellEditor(keyEvent: P6KeyEvent): Boolean {
        return cellEditorAction.handleKeyboardEvent(keyEvent)
    }

    private fun handleKeyboardEventWithShiftDown(
        keyEvent: P6KeyEvent,
        wbwsSt: WbWsSt,
    ): Boolean {
        if (keyEvent.isShiftPressedAlone) {
            return when (keyEvent.key) {
                Key.Enter->{
                    up(wbwsSt)
                    true
                }
                Key.DirectionUp -> {
                    shiftUp(wbwsSt)
                    true
                }

                Key.DirectionDown -> {
                    shiftDown(wbwsSt)
                    true
                }

                Key.DirectionLeft -> {
                    shiftLeft(wbwsSt)
                    true
                }

                Key.DirectionRight -> {
                    shiftRight(wbwsSt)
                    true
                }

                Key.Spacebar -> {
                    selectWholeRow.selectWholeRowForAllSelectedCells(wbwsSt)
                    true
                }

                else -> {
                    handleOtherKey(keyEvent, wbwsSt)
                }
            }
        } else {
            return false
        }
    }


    private fun handleKeyWithCtrlShift(keyEvent: P6KeyEvent, wbws: WbWsSt): Boolean {
        if (keyEvent.isCtrlShiftPressed) {
            return when (keyEvent.key) {
                Key.DirectionUp -> {
                    ctrlShiftUp(wbws)
                    true
                }

                Key.DirectionDown -> {
                    ctrlShiftDown(wbws)
                    true
                }

                Key.DirectionLeft -> {
                    ctrlShiftLeft(wbws)
                    true
                }

                Key.DirectionRight -> {
                    ctrlShiftRight(wbws)
                    true
                }

                else -> false
            }
        } else {
            return false
        }
    }

    private fun handleKeyWithCtrlDown(keyEvent: P6KeyEvent, wsState: WorksheetState): Boolean {
        val cursorState by wsState.cursorStateMs
        val wsState = wsState
        if (keyEvent.isCtrlPressedAlone) {
            return when (keyEvent.key) {
                Key.V -> {
                    pasteRangeToCursorAction.pasteRange(wsState.cursorState)
                    true
                }

                Key.C -> {
                    copyCursorRangeToClipboardAction.copyCursorRangeToClipboard(cursorState)
                    true
                }

                Key.Z -> {
                    undoOnCursorAct.undoOnWorksheet(cursorState)
                    true
                }

                Key.Y ->{
                    undoOnCursorAct.redoOnWorksheet(cursorState)
                    true
                }

                Key.DirectionUp -> {
                    ctrlUp(wsState)
                    true
                }

                Key.DirectionDown -> {
                    ctrlDown(wsState)
                    true
                }

                Key.DirectionRight -> {
                    ctrlRight(wsState)
                    true
                }

                Key.DirectionLeft -> {
                    ctrlLeft(wsState.cursorStateMs)
                    true
                }

                Key.Spacebar -> {
                    selectWholeCol.selectWholeColForAllSelectedCells(wsState)
                    true
                }

                else -> false
            }
        } else {
            return false
        }

    }

    fun _home(wsState: WorksheetState?) {
        wsState?.also {
            val wsState = it
            val cursorState by wsState.cursorStateMs
            val targetCell = CellAddress(wsState.firstCol, cursorState.mainCell.rowIndex)
            val newCursorState = cursorState.setMainCell(targetCell).removeAllExceptMainCell()
            wsState.cursorStateMs.value = newCursorState
            commonReactionOnCursorChanged.onCursorChanged(cursorState)
        }
    }

    override fun home(wbwsSt: WbWsSt) {
        _home(sc.getWsState(wbwsSt))
    }

    override fun home(wbws: WbWs) {
        _home(sc.getWsState(wbws))
    }

    fun _end(wsState: WorksheetState?) {
        wsState?.also {
            val cursorState by wsState.cursorStateMs
            val targetCell = CellAddress(wsState.lastCol, cursorState.mainCell.rowIndex)
            val newCursorState = cursorState.setMainCell(targetCell).removeAllExceptMainCell()
            wsState.cursorStateMs.value = newCursorState
            commonReactionOnCursorChanged.onCursorChanged(cursorState)
        }
    }

    override fun end(wbwsSt: WbWsSt) {
        _end(sc.getWsState(wbwsSt))
    }

    override fun end(wbws: WbWs) {
        _end(sc.getWsState(wbws))
    }

    private fun ctrlUpNoUpdate(wbws: WbWs): CursorState? {
        // go to the nearest non-empty cell on the same col of anchor cell
        val rt = sc.getWsState(wbws)?.let { wsState: WorksheetState ->
            val cursorState: CursorState by wsState.cursorStateMs
            val worksheet = wsState.worksheet
            val mainCell: CellAddress = cursorState.mainCell
            val colIndex = mainCell.colIndex
            val rowIndex = mainCell.rowIndex
            val row = worksheet.getCol(colIndex).map { it.address.rowIndex }.filter { it < rowIndex }.maxOrNull()
            if (row != null) {
                cursorState.setMainCell(CellAddress(colIndex, row))
            } else {
                cursorState
                    .setMainCell(CellAddress(colIndex, wsState.firstRow)).removeAllExceptMainCell()
            }
        }
        return rt
    }

    fun _ctrlUp(wsState: WorksheetState?) {
        wsState?.also {wsState->
            val cursorStateMs = wsState.cursorStateMs
            // go to the nearest non-empty cell on the same col of anchor cell
            val newCursorState = ctrlUpNoUpdate(wsState)
            if (newCursorState != null) {
                cursorStateMs.value = newCursorState
                commonReactionOnCursorChanged.onCursorChanged(newCursorState)
            }
        }
    }

    override fun ctrlUp(wbwsSt: WbWsSt) {
        _ctrlUp(sc.getWsState(wbwsSt))
    }

    override fun ctrlUp(wbws: WbWs) {
        _ctrlUp(sc.getWsState(wbws))
    }

    private fun ctrlDownNoUpdate(wbws: WbWs): CursorState? {
        val rt = sc.getWsState(wbws)?.let { wsState: WorksheetState ->
            val cursorState: CursorState by wsState.cursorStateMs
            val worksheet = wsState.worksheet
            val mainCell: CellAddress = cursorState.mainCell
            val colIndex = mainCell.colIndex
            val rowIndex = mainCell.rowIndex
            val row = worksheet.getCol(colIndex).map { it.address.rowIndex }.filter { it > rowIndex }.minOrNull()
            if (row != null) {
                cursorState.setMainCell(CellAddress(colIndex, row))
            } else {
                cursorState
                    .setMainCell(CellAddress(colIndex, wsState.lastRow))
                    .removeAllExceptMainCell()
            }
        }
        return rt
    }

    fun _ctrlDown(wsState: WorksheetState?) {
        wsState?.also {wsState->
            val cursorStateMs = wsState.cursorStateMs
            val newCursor = ctrlDownNoUpdate(wsState)
            if (newCursor != null) {
                cursorStateMs.value = newCursor
                commonReactionOnCursorChanged.onCursorChanged(newCursor)
            }
        }
    }

    override fun ctrlDown(wbwsSt: WbWsSt) {
        _ctrlDown(sc.getWsState(wbwsSt))
    }

    override fun ctrlDown(wbws: WbWs) {
        _ctrlDown(sc.getWsState(wbws))
    }


    fun _ctrlRight(wsState: WorksheetState?) {
        wsState?.also {wsState->
            val cursorStateMs = wsState.cursorStateMs
            val newCursorState = ctrlRightNoUpdate(wsState)
            if (newCursorState != null) {
                cursorStateMs.value = newCursorState
                commonReactionOnCursorChanged.onCursorChanged(newCursorState)
            }
        }
    }

    override fun ctrlRight(wbwsSt: WbWsSt) {
        _ctrlRight(sc.getWsState(wbwsSt))
    }

    override fun ctrlRight(wbws: WbWs) {
        _ctrlRight(sc.getWsState(wbws))
    }

    /**
     * produce a new cursor state without updating any ms
     */
    private fun ctrlRightNoUpdate(wbws: WbWs): CursorState? {
        val cursorState: CursorState? = sc.getCursorState(wbws)
        val wsState: WorksheetState? = sc.getWsState(wbws)
        if (cursorState != null && wsState != null) {
            val worksheet = wsState.worksheet
            val mainCell: CellAddress = cursorState.mainCell
            val colIndex = mainCell.colIndex
            val rowIndex = mainCell.rowIndex
            val col = worksheet.getRow(mainCell.rowIndex)
                .map { it.address.colIndex }
                .filter { it > colIndex }
                .minOrNull()
            if (col != null) {
                return cursorState.setMainCell(CellAddress(col, rowIndex))
            } else {
                return cursorState
                    .setMainCell(CellAddress(wsState.lastCol, rowIndex))
                    .removeAllExceptMainCell()
            }
        } else {
            return null
        }
    }

    fun ctrlShiftLeft(wsState: WorksheetState?) {
        if (wsState != null) {
            val cursorState by wsState.cursorStateMs
            val cell1 = cursorState.mainCell
            val minCol = cursorState.minCol
            if (minCol != null) {
                val anchor2 = (this.ctrlLeftNoUpdate(wsState) ?: cursorState).mainCell
                val minRow = cursorState.minRow
                val maxRow = cursorState.maxRow

                if (minRow != null && maxRow != null) {
                    val cell3 = CellAddress(cell1.colIndex, minRow)
                    val cell4 = CellAddress(cell1.colIndex, maxRow)
                    wsState.cursorStateMs.value = cursorState
                        .setMainCell(cell1)
                        .removeAllExceptMainCell()
                        .addFragRange(
                            RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                        )
                    commonReactionOnCursorChanged.onCursorChanged(wsState.cursorStateMs.value)
                }
            }
        }
    }

    override fun ctrlShiftLeft(wbwsSt: WbWsSt) {
        val wsState: WorksheetState? = sc.getWsState(wbwsSt)
        ctrlShiftLeft(wsState)
    }

    override fun ctrlShiftLeft(wbws: WbWs) {
        val wsState: WorksheetState? = sc.getWsState(wbws)
        ctrlShiftLeft(wsState)
    }

    fun ctrlShiftRight(wsState: WorksheetState?) {
        wsState?.also {
            val cursorState by wsState.cursorStateMs
            val cell1 = cursorState.mainCell
            val maxCol = cursorState.maxCol
            if (maxCol != null) {
                val anchor2 = this.ctrlRightNoUpdate(wsState)?.mainCell
                if (anchor2 != null) {
                    val minRow = cursorState.minRow
                    val maxRow = cursorState.maxRow

                    if (minRow != null && maxRow != null) {
                        val cell3 = CellAddress(cell1.colIndex, minRow)
                        val cell4 = CellAddress(cell1.colIndex, maxRow)
                        wsState.cursorStateMs.value = cursorState
                            .setMainCell(cell1)
                            .removeAllExceptMainCell()
                            .addFragRange(
                                RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                            )
                        commonReactionOnCursorChanged.onCursorChanged(wsState.cursorStateMs.value)
                    }
                }
            }
        }
    }

    override fun ctrlShiftRight(wbwsSt: WbWsSt) {
        ctrlShiftRight(sc.getWsState(wbwsSt))
    }

    override fun ctrlShiftRight(wbws: WbWs) {
        ctrlShiftRight(sc.getWsState(wbws))
    }

    fun ctrlShiftUp(wsState: WorksheetState?) {
        wsState?.also {
            val cursorState by wsState.cursorStateMs
            val cell1 = cursorState.mainCell
            val minRow = cursorState.minRow
            if (minRow != null) {
                val anchor2 = this.ctrlUpNoUpdate(wsState)?.mainCell
                if (anchor2 != null) {
                    val maxCol = cursorState.maxCol
                    val minCol = cursorState.minCol
                    if (maxCol != null && minCol != null) {
                        val cell3 = CellAddress(maxCol, cell1.rowIndex)
                        val cell4 = CellAddress(minCol, cell1.rowIndex)
                        wsState.cursorStateMs.value = cursorState
                            .setMainCell(cell1)
                            .removeAllExceptMainCell()
                            .addFragRange(
                                RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                            )
                        commonReactionOnCursorChanged.onCursorChanged(wsState.cursorStateMs.value)
                    }
                }
            }
        }

    }

    override fun ctrlShiftUp(wbwsSt: WbWsSt) {
        ctrlShiftUp(sc.getWsState(wbwsSt))
    }

    override fun ctrlShiftUp(wbws: WbWs) {
        ctrlShiftUp(sc.getWsState(wbws))
    }

    fun ctrlShiftDown(wsState: WorksheetState?) {
        wsState?.also {
            val cursorState by wsState.cursorStateMs
            val cell1 = cursorState.mainCell
            val maxRow = cursorState.maxRow
            if (maxRow != null) {
                val anchor2 = this.ctrlDownNoUpdate(
                    cursorState.setMainCell(CellAddress(cell1.colIndex, maxRow))
                )?.mainCell
                if (anchor2 != null) {
                    val maxCol = cursorState.maxCol
                    val minCol = cursorState.minCol
                    if (maxCol != null && minCol != null) {
                        val cell3 = CellAddress(maxCol, cell1.rowIndex)
                        val cell4 = CellAddress(minCol, cell1.rowIndex)
                        wsState.cursorStateMs.value = cursorState
                            .setMainCell(cell1)
                            .removeAllExceptMainCell()
                            .addFragRange(
                                RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                            )
                        commonReactionOnCursorChanged.onCursorChanged(cursorState)
                    }
                }
            }
        }
    }

    override fun ctrlShiftDown(wbwsSt: WbWsSt) {
        ctrlShiftDown(sc.getWsState(wbwsSt))
    }

    override fun ctrlShiftDown(wbws: WbWs) {
        ctrlShiftDown(sc.getWsState(wbws))
    }

    fun ctrlLeft(cursorStateMs: Ms<CursorState>?) {
        cursorStateMs?.also {
            val cs by cursorStateMs
            this.ctrlLeftNoUpdate(cs)?.also { newCursor ->
                cursorStateMs.value = newCursor
                commonReactionOnCursorChanged.onCursorChanged(newCursor)
            }
        }
    }

    override fun ctrlLeft(wbwsSt: WbWsSt) {
        ctrlLeft(sc.getCursorStateMs(wbwsSt))
    }

    override fun ctrlLeft(wbws: WbWs) {
        ctrlLeft(sc.getCursorStateMs(wbws))
    }

    private fun ctrlLeftNoUpdate(wbws: WbWs): CursorState? {
        val rt = sc.getWsState(wbws)?.let { wsState: WorksheetState ->
            val cursorState: CursorState by wsState.cursorStateMs
            val worksheet = wsState.worksheet
            val mainCell: CellAddress = cursorState.mainCell
            val anchorColIndex = mainCell.colIndex
            val anchoRowIndex = mainCell.rowIndex
            val col = worksheet.getRow(anchoRowIndex)
                .map { it.address.colIndex }
                .filter { it < anchorColIndex }
                .maxOrNull()
            if (col != null) {
                cursorState.setMainCell(CellAddress(col, anchoRowIndex))
            } else {
                cursorState
                    .setMainCell(CellAddress(wsState.firstCol, anchoRowIndex))
                    .removeAllExceptMainCell()
            }
        }
        return rt
    }


    fun up(cursorStateMs: Ms<CursorState>?) {
        cursorStateMs?.also {
            cursorStateMs.value = cursorStateMs.value.up()
            commonReactionOnCursorChanged.onCursorChanged(cursorStateMs.value)
        }
    }

    override fun up(wbwsSt: WbWsSt) {
        up(sc.getCursorStateMs(wbwsSt))
    }

    override fun up(wbws: WbWs) {
        up(sc.getCursorStateMs(wbws))
    }


    fun down(cursorStateMs: Ms<CursorState>?) {
        cursorStateMs?.also {
            cursorStateMs.value = cursorStateMs.value.down()
            commonReactionOnCursorChanged.onCursorChanged(cursorStateMs.value)
        }
    }

    override fun down(wbwsSt: WbWsSt) {
        down(sc.getCursorStateMs(wbwsSt))
    }

    override fun down(wbws: WbWs) {
        down(sc.getCursorStateMs(wbws))
    }

    fun left(cursorStateMs: Ms<CursorState>?) {
        cursorStateMs?.also {
            cursorStateMs.value = cursorStateMs.value.left()
            commonReactionOnCursorChanged.onCursorChanged(cursorStateMs.value)
        }
    }

    override fun left(wbwsSt: WbWsSt) {
        left(sc.getCursorStateMs(wbwsSt))
    }

    override fun left(wbws: WbWs) {
        left(sc.getCursorStateMs(wbws))
    }

    fun right(cursorStateMs: Ms<CursorState>?) {
        cursorStateMs?.also {
            cursorStateMs.value = cursorStateMs.value.right()
            commonReactionOnCursorChanged.onCursorChanged(cursorStateMs.value)
        }
    }

    override fun right(wbwsSt: WbWsSt) {
        right(sc.getCursorStateMs(wbwsSt))
    }

    override fun right(wbws: WbWs) {
        right(sc.getCursorStateMs(wbws))
    }

    fun shiftUp(cursorStateMs: Ms<CursorState>?) {
        cursorStateMs?.also {
            val cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorStateMs.value = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddressUtils.rangeFor2Cells(mainCell, theOtherCell.upOneRow()))
            val followTarget = cursorState.mainRange?.topLeft ?: cursorState.mainCell
            moveSliderAction.makeSliderFollowCell(cursorState, followTarget)
            commonReactionOnCursorChanged.onCursorChanged(cursorStateMs.value)
        }
    }

    override fun shiftUp(wbwsSt: WbWsSt) {
        shiftUp(sc.getCursorStateMs(wbwsSt))
    }

    override fun shiftUp(wbws: WbWs) {
        shiftUp(sc.getCursorStateMs(wbws))
    }


    fun shiftDown(cursorStateMs: Ms<CursorState>?) {
        cursorStateMs?.also {
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddressUtils.rangeFor2Cells(mainCell, theOtherCell.downOneRow()))
            val followTarget = cursorState.mainRange?.botRight ?: cursorState.mainCell
            moveSliderAction.makeSliderFollowCell(cursorState, followTarget)
            commonReactionOnCursorChanged.onCursorChanged(cursorStateMs.value)
        }
    }

    override fun shiftDown(wbwsSt: WbWsSt) {
        shiftDown(sc.getCursorStateMs(wbwsSt))
    }

    override fun shiftDown(wbws: WbWs) {
        shiftDown(sc.getCursorStateMs(wbws))
    }

    fun shiftLeft(cursorStateMs: Ms<CursorState>?) {
        cursorStateMs?.also {
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddressUtils.rangeFor2Cells(mainCell, theOtherCell.leftOneCol()))
            val followTarget = cursorState.mainRange?.topLeft ?: cursorState.mainCell
            moveSliderAction.makeSliderFollowCell(cursorState, followTarget)
            commonReactionOnCursorChanged.onCursorChanged(cursorStateMs.value)
        }

    }

    override fun shiftLeft(wbwsSt: WbWsSt) {
        shiftLeft(sc.getCursorStateMs(wbwsSt))
    }

    override fun shiftLeft(wbws: WbWs) {
        shiftLeft(sc.getCursorStateMs(wbws))
    }

    fun shiftRight(cursorStateMs: Ms<CursorState>?) {
        cursorStateMs?.also {
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddressUtils.rangeFor2Cells(mainCell, theOtherCell.rightOneCol()))
            val followTarget = cursorState.mainRange?.botRight ?: cursorState.mainCell
            moveSliderAction.makeSliderFollowCell(cursorState, followTarget)
            commonReactionOnCursorChanged.onCursorChanged(cursorStateMs.value)
        }
    }

    override fun shiftRight(wbwsSt: WbWsSt) {
        shiftRight(sc.getCursorStateMs(wbwsSt))
    }

    override fun shiftRight(wbws: WbWs) {
        shiftRight(sc.getCursorStateMs(wbws))
    }

    override fun f2(wbws: WbWs) {
        openCellEditor.openCellEditor(wbws)
    }

    override fun f2(wbwsSt: WbWsSt) {
        openCellEditor.openCellEditor(wbwsSt)
    }

    fun _onDeleteKey(cursorState: CursorState?) {
        cursorState?.also {
            val req = DeleteMultiCellAtCursorRequest(
                wbKey = cursorState.id.wbKey,
                wsName = cursorState.id.wsName,
                windowId = null
            )
            wsAction.deleteDataOfMultiCellAtCursor(req)
        }
    }

    override fun onDeleteKey(wbwsSt: WbWsSt) {
        _onDeleteKey(sc.getCursorState(wbwsSt))
    }

    override fun onPageDown(wbws: WbWs) {
        sc.getWsState(wbws)?.also{
            pageUp(it)
        }
    }

    override fun onPageDown(wbwsSt: WbWsSt) {
        sc.getWsState(wbwsSt)?.also {
            pageUp(it)
        }
    }

    fun pageUpOrdown(wsState:WorksheetState, isUp:Boolean){
        val visibleRowRange=wsState.slider.visibleRowRange
        val rowCountInOnePage = visibleRowRange.last-visibleRowRange.first + 1
        val cursorState by wsState.cursorStateMs
        val currentMainCell =  cursorState.mainCell
        val numberOfRowToShift = if(isUp) -rowCountInOnePage else rowCountInOnePage
        val newMainCellAddress = currentMainCell.increaseRowBy(numberOfRowToShift)
        val newCursorState=wsState.cursorState.removeAllExceptMainCell().setMainCell(newMainCellAddress)
        wsState.cursorStateMs.value = newCursorState
        /*
        this does not use the common reactor on cursor change because it is special
         */
        updateFormatIndicator.updateFormatIndicator(newCursorState)
        moveSliderAction.shiftSlider(newCursorState,numberOfRowToShift,0)
    }

    fun pageUp(wsState:WorksheetState){
        pageUpOrdown(wsState,true)
    }

    fun pageDown(wbws: WbWs) {
        this.sc.getWsState(wbws)?.also{
            this.pageDown(it)
        }
    }

    fun pageDown(wsState:WorksheetState){
        pageUpOrdown(wsState,false)
    }

    fun pageDown(wbwsSt: WbWsSt) {
        sc.getWsState(wbwsSt)?.also {wsState->
            pageDown(wsState)
        }
    }

    override fun onDeleteKey(wbws: WbWs) {
        _onDeleteKey(sc.getCursorState(wbws))
    }
}
