package com.qxdzbc.p6.app.action.cursor.handle_cursor_keyboard_event

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiAtCursorRequest
import com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell.MakeSliderFollowCellAction
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.app.action.cursor.paste_range_to_cursor.PasteRangeToCursor
import com.qxdzbc.p6.app.action.cursor.undo_on_cursor.UndoOnCursorAction
import com.qxdzbc.p6.app.action.cursor.copy_cursor_range_to_clipboard.CopyCursorRangeToClipboardAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell.SelectWholeColumnForAllSelectedCellAction
import com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells.SelectWholeRowForAllSelectedCellAction
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class)
@P6Singleton
@ContributesBinding(scope= P6AnvilScope::class)
class HandleCursorKeyboardEventActionImp  @Inject constructor(

    private val wsAction: WorksheetAction,
    private val openCellEditor: OpenCellEditorAction,
    private val stateContSt:St<@JvmSuppressWildcards StateContainer>,
    private val pasteRangeToCursorAction: PasteRangeToCursor,
    private val selectWholeCol: SelectWholeColumnForAllSelectedCellAction,
    private val selectWholeRow: SelectWholeRowForAllSelectedCellAction,
    private val makeSliderFollowCellAct: MakeSliderFollowCellAction,
    private val copyCursorRangeToClipboardAction: CopyCursorRangeToClipboardAction,
    private val undoOnCursorAct: UndoOnCursorAction,

    ) : HandleCursorKeyboardEventAction {

    private val sc by stateContSt

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
                    handleKeyWithCtrlDown(keyEvent, cursorState)
                } else if (keyEvent.isShiftPressedAlone) {
                    handleKeyboardEventWhenShiftDown(keyEvent, cursorState)
                } else if (keyEvent.isCtrlShiftPressed) {
                    handleKeyWithCtrlShift(keyEvent, wbws)
                } else {

                    val wsStateMs =
                        sc.getWsStateMs(cursorState.id.wbKey, cursorState.id.wsName)
                    val cursorStateMs =
                        sc.getCursorStateMs(cursorState.id.wbKey, cursorState.id.wsName)
                    val colRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.colRulerStateMs
                    val rowRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.rowRulerStateMs

                    if (wsStateMs != null && cursorStateMs != null && colRulerStateMs != null && rowRulerStateMs != null) {
                        when (keyEvent.key) {
                            Key.Escape -> {
                                wsStateMs.value.cursorStateMs.value = cursorState.removeClipboardRange()
                                true
                            }
                            Key.Delete -> {
                                onDeleteKey(cursorState)
                                true
                            }
                            Key.F2 -> {
                                f2(wsStateMs.value.id)
                                true
                            }
                            Key.DirectionUp -> {
                                up(wbws)
                                true
                            }
                            Key.DirectionDown -> {
                                down(wbws)
                                true
                            }
                            Key.DirectionLeft -> {
                                left(wbws)
                                true
                            }
                            Key.DirectionRight -> {
                                right(wbws)
                                true
                            }
                            Key.Home -> {
                                this.home(wbws)
                                true
                            }
                            Key.MoveEnd -> {
                                this.end(wbws)
                                true
                            }
                            else -> false
                        }
                    } else {
                        false
                    }
                }
            } else {
                false
            }

        } else {
            return false
        }
    }

    private fun handleKeyboardEventWhenShiftDown(
        keyEvent: P6KeyEvent,
        wbws: WbWs
    ): Boolean {
        if (keyEvent.isShiftPressedAlone) {

            val wsStateMs = sc.getWsStateMs(wbws)
            val cursorStateMs = sc.getCursorStateMs(wbws)
            val colRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.colRulerStateMs
            val rowRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.rowRulerStateMs
            if (wsStateMs != null && cursorStateMs != null && colRulerStateMs != null && rowRulerStateMs != null) {
                return when (keyEvent.key) {
                    Key.DirectionUp -> {
                        shiftUp(wbws)
                        true
                    }
                    Key.DirectionDown -> {
                        shiftDown(wbws)
                        true
                    }
                    Key.DirectionLeft -> {
                        shiftLeft(wbws)
                        true
                    }
                    Key.DirectionRight -> {
                        shiftRight(wbws)
                        true
                    }
                    Key.Spacebar -> {
                        selectWholeRow.selectWholeRowForAllSelectedCells(wbws)
                        true
                    }
                    else -> false
                }

            } else {
                return false
            }
        } else {
            return false
        }
    }

    private fun handleKeyWithCtrlShift(keyEvent: P6KeyEvent, wbws: WbWs): Boolean {
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

    private fun handleKeyWithCtrlDown(keyEvent: P6KeyEvent, wbws: WbWs): Boolean {
        val cursorState = sc.getCursorState(wbws)
        if (cursorState != null) {
            if (keyEvent.isCtrlPressedAlone) {
                val wsStateMs = sc.getWsStateMs(cursorState.id.wbKey, cursorState.id.wsName)
                val cursorStateMs = sc.getCursorStateMs(cursorState.id.wbKey, cursorState.id.wsName)
                val colRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.colRulerStateMs
                val rowRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.rowRulerStateMs
                if (wsStateMs != null && cursorStateMs != null && colRulerStateMs != null && rowRulerStateMs != null) {
                    return when (keyEvent.key) {
                        Key.V -> {
                            pasteRangeToCursorAction.pasteRange(wsStateMs.value.cursorState)
                            true
                        }
                        Key.C -> {
                            copyCursorRangeToClipboardAction.copyCursorRangeToClipboard(cursorState)
                            true
                        }
                        Key.Z -> {
                            undoOnCursorAct.undoOnCursor(cursorState)
                            true
                        }
                        Key.DirectionUp -> {
                            ctrlUp(wbws)
                            true
                        }
                        Key.DirectionDown -> {
                            ctrlDown(wbws)
                            true
                        }
                        Key.DirectionRight -> {
                            ctrlRight(wbws)
                            true
                        }
                        Key.DirectionLeft -> {
                            ctrlLeft(wbws)
                            true
                        }
                        Key.Spacebar -> {
                            selectWholeCol.selectWholeColForAllSelectedCells(wbws)
                            true
                        }
                        else -> false
                    }

                } else {
                    return false
                }
            } else {
                return false
            }
        } else {
            return false
        }
    }


    override fun home(
        wbws: WbWs
    ) {
        sc.getWsStateMs(wbws)?.also { wsStateMs ->
            val wsState by wsStateMs
            val cursorState by wsState.cursorStateMs
            val targetCell = CellAddress(wsState.firstCol, cursorState.mainCell.rowIndex)
            val newCursorState = cursorState.setMainCell(targetCell).removeAllExceptMainCell()
            wsAction.makeSliderFollowCursorMainCell(newCursorState, wbws)
        }
    }

    override fun end(
        wbws: WbWs
    ) {
        sc.getWsStateMs(wbws)?.also { wsStateMs ->
            val wsState by wsStateMs
            val cursorState by wsState.cursorStateMs
            val targetCell = CellAddress(wsState.lastCol, cursorState.mainCell.rowIndex)
            val newCursorState = cursorState.setMainCell(targetCell).removeAllExceptMainCell()
            wsState.cursorStateMs.value = newCursorState
            wsAction.makeSliderFollowCursorMainCell(newCursorState, wbws)
        }
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

    override fun ctrlUp(wbws: WbWs) {
        sc.getWsStateMs(wbws)?.also { wsStateMs ->
            val cursorStateMs = wsStateMs.value.cursorStateMs
            // go to the nearest non-empty cell on the same col of anchor cell
            val cursorState by cursorStateMs
            val newCursorState = ctrlUpNoUpdate(wbws)
            if (newCursorState != null) {
                cursorStateMs.value = newCursorState
                wsAction.makeSliderFollowCursorMainCell(newCursorState, wbws)
            }
        }
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

    override fun ctrlDown(
        wbws: WbWs
    ) {
        sc.getWsStateMs(wbws)?.also { wsStateMs ->
            val cursorStateMs = wsStateMs.value.cursorStateMs
            val newCursor = ctrlDownNoUpdate(wbws)
            if (newCursor != null) {
                cursorStateMs.value = newCursor
                wsAction.makeSliderFollowCursorMainCell(newCursor, wbws)
            }
        }
    }

    override fun ctrlRight(
        wbws: WbWs
    ) {
        sc.getWsStateMs(wbws)?.also { wsStateMs ->
            val cursorStateMs = wsStateMs.value.cursorStateMs
            val newCursorState = ctrlRightNoUpdate(wbws)
            if (newCursorState != null) {
                cursorStateMs.value = newCursorState
                wsAction.makeSliderFollowCursorMainCell(newCursorState, wbws)
            }
        }
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

    override fun ctrlShiftLeft(wbws: WbWs) {
        val wsState: WorksheetState? = sc.getWsState(wbws)
        if (wsState != null) {
            val cursorState by wsState.cursorStateMs
            val cell1 = cursorState.mainCell
            val minCol = cursorState.minCol
            if (minCol != null) {
                val anchor2 = (this.ctrlLeftNoUpdate(wbws) ?: cursorState).mainCell
                val minRow = cursorState.minRow
                val maxRow = cursorState.maxRow

                if (minRow != null && maxRow != null) {
                    val cell3 = CellAddress(cell1.colIndex, minRow)
                    val cell4 = CellAddress(cell1.colIndex, maxRow)
                    wsState.cursorStateMs.value = cursorState.setMainCell(cell1).removeAllExceptMainCell().addFragRange(
                        RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                    )
                }
            }
        }
    }

    override fun ctrlShiftRight(wbws: WbWs) {
        sc.getWsState(wbws)?.also { wsState: WorksheetState ->
            val cursorState by wsState.cursorStateMs
            val cell1 = cursorState.mainCell
            val maxCol = cursorState.maxCol
            if (maxCol != null) {
                val anchor2 = this.ctrlRightNoUpdate(wbws)?.mainCell
                if (anchor2 != null) {
                    val minRow = cursorState.minRow
                    val maxRow = cursorState.maxRow

                    if (minRow != null && maxRow != null) {
                        val cell3 = CellAddress(cell1.colIndex, minRow)
                        val cell4 = CellAddress(cell1.colIndex, maxRow)
                        wsState.cursorStateMs.value =
                            cursorState.setMainCell(cell1).removeAllExceptMainCell().addFragRange(
                                RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                            )
                    }
                }
            }
        }
    }

    override fun ctrlShiftUp(wbws: WbWs) {
        sc.getWsState(wbws)?.also { wsState: WorksheetState ->
            val cursorState by wsState.cursorStateMs
            val cell1 = cursorState.mainCell
            val minRow = cursorState.minRow
            if (minRow != null) {
                val anchor2 = this.ctrlUpNoUpdate(wbws)?.mainCell
                if (anchor2 != null) {
                    val maxCol = cursorState.maxCol
                    val minCol = cursorState.minCol
                    if (maxCol != null && minCol != null) {
                        val cell3 = CellAddress(maxCol, cell1.rowIndex)
                        val cell4 = CellAddress(minCol, cell1.rowIndex)
                        wsState.cursorStateMs.value =
                            cursorState.setMainCell(cell1).removeAllExceptMainCell().addFragRange(
                                RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                            )
                    }
                }
            }
        }
    }

    override fun ctrlShiftDown(wbws: WbWs) {
        sc.getWsState(wbws)?.also { wsState: WorksheetState ->
            var cursorState by wsState.cursorStateMs
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
                        cursorState = cursorState.setMainCell(cell1).removeAllExceptMainCell().addFragRange(
                            RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                        )
                    }
                }
            }

        }
    }

    override fun ctrlLeft(wbws: WbWs) {
        sc.getCursorStateMs(wbws)?.also { cursorStateMs ->
            this.ctrlLeftNoUpdate(wbws)?.also { newCursor ->
                cursorStateMs.value = newCursor
                wsAction.makeSliderFollowCursorMainCell(newCursor, wbws)
            }
        }
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

    override fun up(
        wbws: WbWs,
    ) {
        sc.getCursorStateMs(wbws)?.also { cursorStateMs ->
            cursorStateMs.value = cursorStateMs.value.up()
            wsAction.makeSliderFollowCursorMainCell(cursorStateMs.value, wbws)
        }
    }

    override fun down(
        wbws: WbWs,
    ) {
        sc.getCursorStateMs(wbws)?.also { cursorStateMs ->
            cursorStateMs.value = cursorStateMs.value.down()
            wsAction.makeSliderFollowCursorMainCell(cursorStateMs.value, wbws)
        }
    }

    override fun left(
        wbws: WbWs
    ) {
        sc.getCursorStateMs(wbws)?.also { cursorStateMs ->
            cursorStateMs.value = cursorStateMs.value.left()
            wsAction.makeSliderFollowCursorMainCell(cursorStateMs.value, wbws)
        }
    }

    override fun right(wbws: WbWs) {
        sc.getCursorStateMs(wbws)?.also { cursorStateMs ->
            cursorStateMs.value = cursorStateMs.value.right()
            wsAction.makeSliderFollowCursorMainCell(cursorStateMs.value, wbws)
        }
    }

    override fun shiftUp(wbws: WbWs) {
        sc.getCursorStateMs(wbws)?.also { cursorStateMs ->
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.upOneRow()))
            wsAction.makeSliderFollowCursorMainCell(cursorState, wbws)
            val followTarget = cursorState.mainRange?.topLeft ?: cursorState.mainCell
            makeSliderFollowCellAct.makeSliderFollowCell(cursorState,followTarget)
        }

    }

    override fun shiftDown(
        wbws: WbWs
    ) {
        sc.getCursorStateMs(wbws)?.also { cursorStateMs ->
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.downOneRow()))
//            wsAction.makeSliderFollowCursorMainCell(cursorState, wbws)
            val followTarget = cursorState.mainRange?.botRight ?: cursorState.mainCell
            makeSliderFollowCellAct.makeSliderFollowCell(cursorState,followTarget)
        }
    }

    override fun shiftLeft(
        wbws: WbWs
    ) {
        sc.getCursorStateMs(wbws)?.also { cursorStateMs ->
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.leftOneCol()))
//            wsAction.makeSliderFollowCursorMainCell(cursorState, wbws)
            val followTarget = cursorState.mainRange?.topLeft ?: cursorState.mainCell
            makeSliderFollowCellAct.makeSliderFollowCell(cursorState,followTarget)
        }
    }

    override fun shiftRight(
        wbws: WbWs
    ) {
        sc.getCursorStateMs(wbws)?.also { cursorStateMs ->
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.rightOneCol()))
//            wsAction.makeSliderFollowCursorMainCell(cursorState, wbws)
            val followTarget = cursorState.mainRange?.botRight ?: cursorState.mainCell
            makeSliderFollowCellAct.makeSliderFollowCell(cursorState,followTarget)
        }
    }

    override fun f2(wbws: WbWs) {
        openCellEditor.openCellEditor(wbws)
    }

    override fun onDeleteKey(wbws: WbWs) {
        sc.getCursorState(wbws)?.also { cursorState: CursorState ->
            val req = DeleteMultiAtCursorRequest(
                wbKey = cursorState.id.wbKey,
                wsName = cursorState.id.wsName,
                windowId = null
            )
            wsAction.deleteMultiCellAtCursor(req)
        }
    }

}
