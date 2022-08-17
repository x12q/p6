package com.emeraldblast.p6.ui.document.worksheet.cursor.actions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.*
import com.emeraldblast.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.emeraldblast.p6.app.action.common_data_structure.WithWbWs
import com.emeraldblast.p6.app.action.range.RangeApplier
import com.emeraldblast.p6.app.action.range.RangeRM
import com.emeraldblast.p6.app.action.range.range_to_clipboard.RangeToClipboardRequest
import com.emeraldblast.p6.app.action.range.RangeId
import com.emeraldblast.p6.app.action.range.paste_range.PasteRangeRequest2
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddresses
import com.emeraldblast.p6.app.document.worksheet.WorksheetErrors
import com.emeraldblast.p6.ui.app.ErrorRouter
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.app.action.worksheet.WorksheetAction
import com.emeraldblast.p6.app.action.worksheet.delete_multi.DeleteMultiRequest2
import com.emeraldblast.p6.app.common.utils.PKeyEvent
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import com.emeraldblast.p6.ui.document.worksheet.ruler.RulerState
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class)
class CursorActionImp @Inject constructor(
    private val wsAction: WorksheetAction,
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val errorRouter: ErrorRouter,
    private val rangeRM: RangeRM,
    private val rangeApplier: RangeApplier,
    private val openCellEditor: OpenCellEditorAction
) : CursorAction {

    private var appState by appStateMs

    override fun pasteRange(cursorState: CursorState) {
        val req = PasteRangeRequest2(
            rangeId = RangeId(
                rangeAddress = cursorState.mainRange ?: RangeAddress(cursorState.mainCell),
                wbKey = cursorState.id.wbKey,
                wsName = cursorState.id.wsName,
            ),
            windowId = appState.getWindowStateMsByWbKey(cursorState.id.wbKey)?.value?.id
        )
        val out = rangeRM.pasteRange2(req)
        out?.let {
            rangeApplier.applyPasteRange(it)
        }
    }

    override fun rangeToClipboard2(cursorState: CursorState) {
        val mergeAllCursorState = cursorState.attemptToMergeAllIntoOne()
        if (mergeAllCursorState.fragmentedCells.isNotEmpty() || mergeAllCursorState.fragmentedRanges.isNotEmpty()) {
            // raise error
            errorRouter.toWindow(WorksheetErrors.CantCopyOnFragmentedSelection, cursorState.id.wbKey)
        } else {
            val targetRange = mergeAllCursorState.mainRange
            if (targetRange != null) {
                wsAction.rangeToClipboard(
                    RangeToClipboardRequest(
                        rangeId = RangeId(
                            rangeAddress = targetRange,
                            wbKey = cursorState.id.wbKey,
                            wsName = cursorState.id.wsName
                        ),
                        windowId = appState.getWindowStateMsByWbKey(cursorState.id.wbKey)?.value?.id
                    )
                )

            } else {
                wsAction.rangeToClipboard(
                    RangeToClipboardRequest(
                        rangeId = RangeId(
                            rangeAddress = RangeAddress(cursorState.mainCell),
                            wbKey = cursorState.id.wbKey,
                            wsName = cursorState.id.wsName
                        ),
                        windowId = appState.getWindowStateMsByWbKey(cursorState.id.wbKey)?.value?.id
                    )
                )
            }
        }
    }

    override fun handleKeyboardEvent(
        keyEvent: PKeyEvent,
        cursorState: CursorState,
    ): Boolean {
        val wsState = appState.getWsState(
            cursorState.id.wbKey, cursorState.id.wsName
        )
        if (wsState != null) {
            return if (keyEvent.type == KeyEventType.KeyDown) {
                if (keyEvent.isCtrlPressedAlone) {
                    handleKeyWithCtrlDown(keyEvent, cursorState)
                } else if (keyEvent.isShiftPressedAlone) {
                    handleKeyboardEventWhenShiftDown(keyEvent, cursorState)
                } else if (keyEvent.isCtrlShiftPressed) {
                    handleKeyWithCtrlShift(keyEvent, wsState)
                } else {

                    val wsStateMs =
                        appState.getWsStateMs(cursorState.id.wbKey, cursorState.id.wsName)
                    val cursorStateMs =
                        appState.getCursorStateMs(cursorState.id.wbKey, cursorState.id.wsName)
                    val colRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.colRulerStateMs
                    val rowRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.rowRulerStateMs

                    if (wsStateMs != null && cursorStateMs != null && colRulerStateMs != null && rowRulerStateMs != null) {
                        when (keyEvent.key) {
                            Key.Escape -> {
                                wsStateMs.value.cursorStateMs.value = cursorState.removeClipboardRange()
                                true
                            }
                            Key.Delete -> {
                                delete(cursorState)
                                true
                            }
                            Key.F2 -> {
                                f2(wsStateMs.value.id)
                                true
                            }
                            Key.DirectionUp -> {
                                up(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                                true
                            }
                            Key.DirectionDown -> {
                                down(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                                true
                            }
                            Key.DirectionLeft -> {
                                left(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                                true
                            }
                            Key.DirectionRight -> {
                                right(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                                true
                            }
                            Key.Home -> {
                                this.home(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                                true
                            }
                            Key.MoveEnd -> {
                                this.end(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
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
        keyEvent: PKeyEvent,
        cursorState: CursorState,
    ): Boolean {
        if (keyEvent.isShiftPressedAlone) {

            val wsStateMs = appState.getWsStateMs(cursorState.id.wbKey, cursorState.id.wsName)
            val cursorStateMs = appState.getCursorStateMs(cursorState.id.wbKey, cursorState.id.wsName)
            val colRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.colRulerStateMs
            val rowRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.rowRulerStateMs
            if (wsStateMs != null && cursorStateMs != null && colRulerStateMs != null && rowRulerStateMs != null) {
                return when (keyEvent.key) {
                    Key.DirectionUp -> {
                        shiftUp(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                        true
                    }
                    Key.DirectionDown -> {
                        shiftDown(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                        true
                    }
                    Key.DirectionLeft -> {
                        shiftLeft(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                        true
                    }
                    Key.DirectionRight -> {
                        shiftRight(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                        true
                    }
                    Key.Spacebar -> {
                        shiftSpace(cursorStateMs, wsStateMs.value)
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

    private fun handleKeyWithCtrlShift(keyEvent: PKeyEvent, wsState: WorksheetState): Boolean {
        if (keyEvent.isCtrlShiftPressed) {
            return when (keyEvent.key) {
                Key.DirectionUp -> {
                    ctrlShiftUp(wsState)
                    true
                }
                Key.DirectionDown -> {
                    ctrlShiftDown(wsState)
                    true
                }
                Key.DirectionLeft -> {
                    ctrlShiftLeft(wsState)
                    true
                }
                Key.DirectionRight -> {
                    ctrlShiftRight(wsState)
                    true
                }
                else -> false
            }
        } else {
            return false
        }
    }

    private fun handleKeyWithCtrlDown(keyEvent: PKeyEvent, cursorState: CursorState): Boolean {
        if (keyEvent.isCtrlPressedAlone) {
            val wsStateMs = appState.getWsStateMs(cursorState.id.wbKey, cursorState.id.wsName)
            val cursorStateMs = appState.getCursorStateMs(cursorState.id.wbKey, cursorState.id.wsName)
            val colRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.colRulerStateMs
            val rowRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.rowRulerStateMs
            if (wsStateMs != null && cursorStateMs != null && colRulerStateMs != null && rowRulerStateMs != null) {
                return when (keyEvent.key) {
                    Key.V -> {
                        pasteRange(wsStateMs.value.cursorState)
                        true
                    }
                    Key.C -> {
                        rangeToClipboard2(cursorState)
                        true
                    }
                    Key.Z -> {
                        undo(cursorState)
                        true
                    }
                    Key.DirectionUp -> {
                        ctrlUp(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                        true
                    }
                    Key.DirectionDown -> {
                        ctrlDown(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                        true
                    }
                    Key.DirectionRight -> {
                        ctrlRight(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                        true
                    }
                    Key.DirectionLeft -> {
                        ctrlLeft(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
                        true
                    }
                    Key.Spacebar -> {
                        ctrlSpace(wsStateMs.value)
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

    override fun home(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        val wsState by wsStateMs
        val cursorState by cursorStateMs
        val targetCell = CellAddress(wsState.firstCol, cursorState.mainCell.rowIndex)
        val newCursorState = cursorState.setAnchorCell(targetCell).removeAllExceptAnchorCell()
        wsAction.makeSliderFollowCursor(newCursorState, wsStateMs, colRulerStateMs, rowRulerStateMs)
    }

    override fun end(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        val wsState by wsStateMs
        val cursorState by cursorStateMs
        val targetCell = CellAddress(wsState.lastCol, cursorState.mainCell.rowIndex)
        val newCursorState = cursorState.setAnchorCell(targetCell).removeAllExceptAnchorCell()
        cursorStateMs.value = newCursorState
        wsAction.makeSliderFollowCursor(newCursorState, wsStateMs, colRulerStateMs, rowRulerStateMs)
    }

    private fun ctrlUpNoUpdate(cursorState: CursorState, wsState: WorksheetState): CursorState? {
        // go to the nearest non-empty cell on the same col of anchor cell
        val worksheet = wsState.worksheet
        if (worksheet != null) {
            val mainCell: CellAddress = cursorState.mainCell
            val colIndex = mainCell.colIndex
            val rowIndex = mainCell.rowIndex
            val row = worksheet.getCol(colIndex).map { it.address.rowIndex }.filter { it < rowIndex }.maxOrNull()
            if (row != null) {
                return cursorState.setAnchorCell(CellAddress(colIndex, row))
            } else {
                return cursorState
                    .setAnchorCell(CellAddress(colIndex, wsState.firstRow)).removeAllExceptAnchorCell()
            }
        }
        return null
    }

    override fun ctrlUp(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        // go to the nearest non-empty cell on the same col of anchor cell
        val cursorState by cursorStateMs
        val newCursorState = ctrlUpNoUpdate(cursorState, wsStateMs.value)
        if (newCursorState != null) {
            cursorStateMs.value = newCursorState
            wsAction.makeSliderFollowCursor(newCursorState, wsStateMs, colRulerStateMs, rowRulerStateMs)
        }
    }

    private fun ctrlDownNoUpdate(cursorState: CursorState, wsState: WorksheetState): CursorState? {
        val worksheet = wsState.worksheet
        if (worksheet != null) {
            val mainCell: CellAddress = cursorState.mainCell
            val colIndex = mainCell.colIndex
            val rowIndex = mainCell.rowIndex
            val row = worksheet.getCol(colIndex).map { it.address.rowIndex }.filter { it > rowIndex }.minOrNull()
            if (row != null) {
                return cursorState.setAnchorCell(CellAddress(colIndex, row))
            } else {
                return cursorState
                    .setAnchorCell(CellAddress(colIndex, wsState.lastRow))
                    .removeAllExceptAnchorCell()
            }
        }
        return null
    }

    override fun ctrlDown(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        val newCursor = ctrlDownNoUpdate(cursorStateMs.value, wsStateMs.value)
        if (newCursor != null) {
            cursorStateMs.value = newCursor
            wsAction.makeSliderFollowCursor(newCursor, wsStateMs, colRulerStateMs, rowRulerStateMs)
        }
    }

    override fun ctrlRight(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        val newCursorState = ctrlRightNoUpdate(cursorStateMs.value, wsStateMs.value)
        if (newCursorState != null) {
            cursorStateMs.value = newCursorState
            wsAction.makeSliderFollowCursor(newCursorState, wsStateMs, colRulerStateMs, rowRulerStateMs)
        }
    }

    /**
     * produce a new cursor state without updating any ms
     */
    private fun ctrlRightNoUpdate(cursorState: CursorState, wsState: WorksheetState): CursorState? {
        val worksheet = wsState.worksheet
        if (worksheet != null) {
            val mainCell: CellAddress = cursorState.mainCell
            val colIndex = mainCell.colIndex
            val rowIndex = mainCell.rowIndex
            val col = worksheet.getRow(mainCell.rowIndex)
                .map { it.address.colIndex }
                .filter { it > colIndex }
                .minOrNull()
            if (col != null) {
                return cursorState.setAnchorCell(CellAddress(col, rowIndex))
            } else {
                return cursorState
                    .setAnchorCell(CellAddress(wsState.lastCol, rowIndex))
                    .removeAllExceptAnchorCell()
            }
        } else {
            return null
        }
    }

    override fun ctrlShiftLeft(wsState: WorksheetState) {
        var cursorState by wsState.cursorStateMs
        val cell1 = cursorState.mainCell
        val minCol = cursorState.minCol
        if (minCol != null) {
            val anchor2 = this.ctrlLeftNoUpdate(
                cursorState.setAnchorCell(CellAddress(minCol, cell1.rowIndex)),
                wsState
            ).mainCell
            val minRow = cursorState.minRow
            val maxRow = cursorState.maxRow

            if (minRow != null && maxRow != null) {
                val cell3 = CellAddress(cell1.colIndex, minRow)
                val cell4 = CellAddress(cell1.colIndex, maxRow)
                cursorState = cursorState.setAnchorCell(cell1).removeAllExceptAnchorCell().addFragRange(
                    RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                )
            }
        }
    }

    override fun ctrlShiftRight(wsState: WorksheetState) {
        var cursorState by wsState.cursorStateMs
        val cell1 = cursorState.mainCell
        val maxCol = cursorState.maxCol
        if (maxCol != null) {
            val anchor2 = this.ctrlRightNoUpdate(
                cursorState.setAnchorCell(CellAddress(maxCol, cell1.rowIndex)), wsState
            )?.mainCell
            if (anchor2 != null) {
                val minRow = cursorState.minRow
                val maxRow = cursorState.maxRow

                if (minRow != null && maxRow != null) {
                    val cell3 = CellAddress(cell1.colIndex, minRow)
                    val cell4 = CellAddress(cell1.colIndex, maxRow)
                    cursorState = cursorState.setAnchorCell(cell1).removeAllExceptAnchorCell().addFragRange(
                        RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                    )
                }
            }
        }
    }

    override fun ctrlShiftUp(wsState: WorksheetState) {
        var cursorState by wsState.cursorStateMs
        val cell1 = cursorState.mainCell
        val minRow = cursorState.minRow
        if (minRow != null) {
            val anchor2 = this.ctrlUpNoUpdate(
                cursorState.setAnchorCell(CellAddress(cell1.colIndex, minRow)),
                wsState
            )?.mainCell
            if (anchor2 != null) {
                val maxCol = cursorState.maxCol
                val minCol = cursorState.minCol
                if (maxCol != null && minCol != null) {
                    val cell3 = CellAddress(maxCol, cell1.rowIndex)
                    val cell4 = CellAddress(minCol, cell1.rowIndex)
                    cursorState = cursorState.setAnchorCell(cell1).removeAllExceptAnchorCell().addFragRange(
                        RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                    )
                }
            }
        }
    }

    override fun ctrlShiftDown(wsState: WorksheetState) {
        var cursorState by wsState.cursorStateMs
        val cell1 = cursorState.mainCell
        val maxRow = cursorState.maxRow
        if (maxRow != null) {
            val anchor2 = this.ctrlDownNoUpdate(
                cursorState.setAnchorCell(CellAddress(cell1.colIndex, maxRow)), wsState
            )?.mainCell
            if (anchor2 != null) {
                val maxCol = cursorState.maxCol
                val minCol = cursorState.minCol
                if (maxCol != null && minCol != null) {
                    val cell3 = CellAddress(maxCol, cell1.rowIndex)
                    val cell4 = CellAddress(minCol, cell1.rowIndex)
                    cursorState = cursorState.setAnchorCell(cell1).removeAllExceptAnchorCell().addFragRange(
                        RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                    )
                }
            }
        }

    }

    override fun ctrlLeft(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        val newCursor = this.ctrlLeftNoUpdate(cursorStateMs.value, wsStateMs.value)
        cursorStateMs.value = newCursor
        wsAction.makeSliderFollowCursor(newCursor, wsStateMs, colRulerStateMs, rowRulerStateMs)
    }

    private fun ctrlLeftNoUpdate(cursorState: CursorState, wsState: WorksheetState): CursorState {
        val worksheet = wsState.worksheet
        if (worksheet != null) {
            val mainCell: CellAddress = cursorState.mainCell
            val anchorColIndex = mainCell.colIndex
            val anchoRowIndex = mainCell.rowIndex
            val col = worksheet.getRow(anchoRowIndex)
                .map { it.address.colIndex }
                .filter { it < anchorColIndex }
                .maxOrNull()
            if (col != null) {
                return cursorState.setAnchorCell(CellAddress(col, anchoRowIndex))
            } else {
                return cursorState
                    .setAnchorCell(CellAddress(wsState.firstCol, anchoRowIndex))
                    .removeAllExceptAnchorCell()
            }
        } else {
            return cursorState
        }
    }

    override fun ctrlSpace(wsState: WorksheetState) {
        var cursorState by wsState.cursorStateMs
        val selectCols: List<Int> = cursorState.colFromFragCells
        val colFromRange: List<IntRange> = cursorState.colFromRange
        var newCursor = cursorState
        newCursor = newCursor.addFragRanges(
            selectCols.map { col ->
                RangeAddresses.wholeCol(col)
            }
        ).addFragRanges(
            colFromRange.map { colRange ->
                RangeAddress(
                    CellAddress(colRange.first, wsState.firstRow),
                    CellAddress(colRange.last, wsState.lastRow)
                )
            }
        )
        cursorState = newCursor
    }

    override fun shiftSpace(cursorStateMs: Ms<CursorState>, wsState: WorksheetState) {
        var cursorState: CursorState by cursorStateMs
        val selectRows: List<Int> = cursorState.allFragCells.map { it.rowIndex }
        val rowFromRange: List<IntRange> = cursorState.allRanges.map {
            it.rowRange
        }
        var newCursor = cursorState

        newCursor = newCursor.addFragRanges(
            selectRows.map { row ->
                RangeAddresses.wholeRow(row)
            }
        ).addFragRanges(
            rowFromRange.map { rowRange ->
                RangeAddress(
                    CellAddress(wsState.firstCol, rowRange.first),
                    CellAddress(wsState.lastCol, rowRange.last)
                )
            }
        )
        cursorState = newCursor
    }

    override fun up(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        cursorStateMs.value = cursorStateMs.value.up()
        wsAction.makeSliderFollowCursor(cursorStateMs.value, wsStateMs, colRulerStateMs, rowRulerStateMs)
    }

    override fun down(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        cursorStateMs.value = cursorStateMs.value.down()
        wsAction.makeSliderFollowCursor(cursorStateMs.value, wsStateMs, colRulerStateMs, rowRulerStateMs)
    }

    override fun left(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        cursorStateMs.value = cursorStateMs.value.left()
        wsAction.makeSliderFollowCursor(cursorStateMs.value, wsStateMs, colRulerStateMs, rowRulerStateMs)
    }

    override fun right(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        cursorStateMs.value = cursorStateMs.value.right()
        wsAction.makeSliderFollowCursor(cursorStateMs.value, wsStateMs, colRulerStateMs, rowRulerStateMs)
    }

    override fun shiftUp(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        var cursorState by cursorStateMs
        val mainCell = cursorState.mainCell
        val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
        cursorState = cursorState.removeAllFragmentedCells()
            .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.upOneRow()))
        wsAction.makeSliderFollowCursor(cursorState, wsStateMs, colRulerStateMs, rowRulerStateMs)
    }

    override fun shiftDown(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        var cursorState by cursorStateMs
        val mainCell = cursorState.mainCell
        val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
        cursorState = cursorState.removeAllFragmentedCells()
            .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.downOneRow()))
        wsAction.makeSliderFollowCursor(cursorState, wsStateMs, colRulerStateMs, rowRulerStateMs)
    }

    override fun shiftLeft(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        var cursorState by cursorStateMs
        val mainCell = cursorState.mainCell
        val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
        cursorState = cursorState.removeAllFragmentedCells()
            .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.leftOneCol()))
        wsAction.makeSliderFollowCursor(cursorState, wsStateMs, colRulerStateMs, rowRulerStateMs)
    }

    override fun shiftRight(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    ) {
        var cursorState by cursorStateMs
        val mainCell = cursorState.mainCell
        val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
        cursorState = cursorState.removeAllFragmentedCells()
            .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.rightOneCol()))
        wsAction.makeSliderFollowCursor(cursorState, wsStateMs, colRulerStateMs, rowRulerStateMs)
    }

    override fun f2(wsState: WithWbWs) {
        openCellEditor.openCellEditor(wsState)
    }

    override fun delete(cursorState: CursorState) {
        val req = DeleteMultiRequest2(
            wbKey = cursorState.id.wbKey,
            wsName = cursorState.id.wsName,
            windowId = null
        )
        wsAction.deleteMulti2(req)
    }

    override fun undo(cursorState: CursorState) {
        this.appState.queryStateByWorkbookKey(
            cursorState.id.wbKey
        ).ifOk {
            val commandStack by it.workbookState.commandStackMs
            val command = commandStack.pop()
            if (command != null) {
                command.undo()
            }
        }
    }
}


