package com.qxdzbc.p6.ui.document.worksheet.cursor.actions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardRequest
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.app.document.worksheet.WorksheetErrors
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiAtCursorRequest
import com.qxdzbc.common.compose.key_event.PKeyEvent
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorProvider
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import javax.inject.Inject



@OptIn(ExperimentalComposeUiApi::class)
class CursorActionImp @Inject constructor(
    private val wsAction: WorksheetAction,
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val errorRouter: ErrorRouter,
    private val openCellEditor: OpenCellEditorAction,
    @StateContainerSt
    private val stateContSt:St<@JvmSuppressWildcards StateContainer>,
    private val formulaColorProvider: FormulaColorProvider,
    private val pasteRangeAction: PasteRangeAction,
) : CursorAction {

    private var appState by appStateMs
    private val stateCont by stateContSt

    override fun pasteRange(wbws: WbWs) {
        val cursorState = appState.getCursorState(wbws)
        if(cursorState!=null){
            pasteRangeAction.pasteRange(wbws,cursorState.mainRange ?: RangeAddress(cursorState.mainCell),)
        }
    }

    override fun rangeToClipboard(wbws: WbWs) {
        val cursorState: CursorState? = appState.getCursorState(wbws)
        if(cursorState!=null){
            val mergeAllCursorState = cursorState.attemptToMergeAllIntoOne()
            if (mergeAllCursorState.fragmentedCells.isNotEmpty() || mergeAllCursorState.fragmentedRanges.isNotEmpty()) {
                // raise error
                errorRouter.publishToWindow(WorksheetErrors.CantCopyOnFragmentedSelection, cursorState.id.wbKey)
            } else {
                val targetRange = mergeAllCursorState.mainRange
                if (targetRange != null) {
                    wsAction.rangeToClipboard(
                        RangeToClipboardRequest(
                            rangeId = RangeIdImp(
                                rangeAddress = targetRange,
                                wbKeySt = cursorState.id.wbKeySt,
                                wsNameSt = cursorState.id.wsNameSt
                            ),
                            windowId = appState.getWindowStateMsByWbKey(cursorState.id.wbKey)?.value?.id
                        )
                    )

                } else {
                    wsAction.rangeToClipboard(
                        RangeToClipboardRequest(
                            rangeId = RangeIdImp(
                                rangeAddress = RangeAddress(cursorState.mainCell),
                                wbKeySt = cursorState.id.wbKeySt,
                                wsNameSt = cursorState.id.wsNameSt
                            ),
                            windowId = appState.getWindowStateMsByWbKey(cursorState.id.wbKey)?.value?.id
                        )
                    )
                }
            }
        }
    }

    override fun getFormulaRangeAndColor(wbws: WbWs): Map<RangeAddress, Color> {
        val cellEditorState by stateCont.cellEditorStateMs
        if(cellEditorState.isActive){
            val targetCell: Cell? = cellEditorState.targetCell?.let {
                stateCont.getCell(wbws.wbKey,wbws.wsName,it)
            }
            val ranges = targetCell?.content?.exUnit?.getRangeIds()?: emptyList()
            val colors = formulaColorProvider.getColors(ranges.size)
            val colorMap:Map<RangeAddress, Color> = buildMap {
                for((i,rid) in ranges.withIndex()){
                    if(rid.wbKey == wbws.wbKey && rid.wsName == wbws.wsName){
                        put(rid.rangeAddress,colors[i])
                    }
                }
            }
            return colorMap
        }else{
            return emptyMap()
        }
    }

    override fun handleKeyboardEvent(
        keyEvent: PKeyEvent,
        wbws: WbWs,
    ): Boolean {
        val wsState = appState.getWsState(wbws)
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
        keyEvent: PKeyEvent,
        wbws: WbWs
    ): Boolean {
        if (keyEvent.isShiftPressedAlone) {

            val wsStateMs = appState.getWsStateMs(wbws)
            val cursorStateMs = appState.getCursorStateMs(wbws)
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
                        selectWholeRow(wbws)
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

    private fun handleKeyWithCtrlShift(keyEvent: PKeyEvent, wbws: WbWs): Boolean {
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

    private fun handleKeyWithCtrlDown(keyEvent: PKeyEvent, wbws: WbWs): Boolean {
        val cursorState = appState.getCursorState(wbws)
        if (cursorState != null) {
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
                            rangeToClipboard(cursorState)
                            true
                        }
                        Key.Z -> {
                            undo(cursorState)
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
                            selectWholeCol(wbws)
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
        appState.getWsStateMs(wbws)?.also { wsStateMs ->
            val wsState by wsStateMs
            val cursorState by wsState.cursorStateMs
            val targetCell = CellAddress(wsState.firstCol, cursorState.mainCell.rowIndex)
            val newCursorState = cursorState.setMainCell(targetCell).removeAllExceptAnchorCell()
            wsAction.makeSliderFollowCursor(newCursorState, wbws)
        }
    }

    override fun end(
        wbws: WbWs
    ) {
        appState.getWsStateMs(wbws)?.also { wsStateMs ->
            val wsState by wsStateMs
            val cursorState by wsState.cursorStateMs
            val targetCell = CellAddress(wsState.lastCol, cursorState.mainCell.rowIndex)
            val newCursorState = cursorState.setMainCell(targetCell).removeAllExceptAnchorCell()
            wsState.cursorStateMs.value = newCursorState
            wsAction.makeSliderFollowCursor(newCursorState, wbws)
        }
    }

    private fun ctrlUpNoUpdate(wbws: WbWs): CursorState? {
        // go to the nearest non-empty cell on the same col of anchor cell
        val rt = appState.getWsState(wbws)?.let { wsState: WorksheetState ->
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
                    .setMainCell(CellAddress(colIndex, wsState.firstRow)).removeAllExceptAnchorCell()
            }
        }
        return rt
    }

    override fun ctrlUp(wbws: WbWs) {
        appState.getWsStateMs(wbws)?.also { wsStateMs ->
            val cursorStateMs = wsStateMs.value.cursorStateMs
            // go to the nearest non-empty cell on the same col of anchor cell
            val cursorState by cursorStateMs
            val newCursorState = ctrlUpNoUpdate(wbws)
            if (newCursorState != null) {
                cursorStateMs.value = newCursorState
                wsAction.makeSliderFollowCursor(newCursorState, wbws)
            }
        }
    }

    private fun ctrlDownNoUpdate(wbws: WbWs): CursorState? {
        val rt = appState.getWsState(wbws)?.let { wsState: WorksheetState ->
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
                    .removeAllExceptAnchorCell()
            }
        }
        return rt
    }

    override fun ctrlDown(
        wbws: WbWs
    ) {
        appState.getWsStateMs(wbws)?.also { wsStateMs ->
            val cursorStateMs = wsStateMs.value.cursorStateMs
            val newCursor = ctrlDownNoUpdate(wbws)
            if (newCursor != null) {
                cursorStateMs.value = newCursor
                wsAction.makeSliderFollowCursor(newCursor, wbws)
            }
        }
    }

    override fun ctrlRight(
        wbws: WbWs
    ) {
        appState.getWsStateMs(wbws)?.also { wsStateMs ->
            val cursorStateMs = wsStateMs.value.cursorStateMs
            val newCursorState = ctrlRightNoUpdate(wbws)
            if (newCursorState != null) {
                cursorStateMs.value = newCursorState
                wsAction.makeSliderFollowCursor(newCursorState, wbws)
            }
        }
    }

    /**
     * produce a new cursor state without updating any ms
     */
    private fun ctrlRightNoUpdate(wbws: WbWs): CursorState? {
        val cursorState: CursorState? = appState.getCursorState(wbws)
        val wsState: WorksheetState? = appState.getWsState(wbws)
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
                    .removeAllExceptAnchorCell()
            }
        } else {
            return null
        }
    }

    override fun ctrlShiftLeft(wbws: WbWs) {
        val wsState: WorksheetState? = appState.getWsState(wbws)
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
                    wsState.cursorStateMs.value = cursorState.setMainCell(cell1).removeAllExceptAnchorCell().addFragRange(
                        RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                    )
                }
            }
        }
    }

    override fun ctrlShiftRight(wbws: WbWs) {
        appState.getWsState(wbws)?.also { wsState: WorksheetState ->
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
                            cursorState.setMainCell(cell1).removeAllExceptAnchorCell().addFragRange(
                                RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                            )
                    }
                }
            }
        }
    }

    override fun ctrlShiftUp(wbws: WbWs) {
        appState.getWsState(wbws)?.also { wsState: WorksheetState ->
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
                            cursorState.setMainCell(cell1).removeAllExceptAnchorCell().addFragRange(
                                RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                            )
                    }
                }
            }
        }
    }

    override fun ctrlShiftDown(wbws: WbWs) {
        appState.getWsState(wbws)?.also { wsState: WorksheetState ->
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
                        cursorState = cursorState.setMainCell(cell1).removeAllExceptAnchorCell().addFragRange(
                            RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                        )
                    }
                }
            }

        }
    }

    override fun ctrlLeft(wbws: WbWs) {
        appState.getCursorStateMs(wbws)?.also { cursorStateMs ->
            this.ctrlLeftNoUpdate(wbws)?.also { newCursor ->
                cursorStateMs.value = newCursor
                wsAction.makeSliderFollowCursor(newCursor, wbws)
            }
        }
    }

    private fun ctrlLeftNoUpdate(wbws: WbWs): CursorState? {
        val rt = appState.getWsState(wbws)?.let { wsState: WorksheetState ->
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
                    .removeAllExceptAnchorCell()
            }
        }
        return rt
    }

    override fun selectWholeCol(wbws: WbWs) {
        val wsStateMs = appState.getWsStateMs(wbws)
        if (wsStateMs != null) {
            val wsState by wsStateMs
            val cursorStateMs = wsStateMs.value.cursorStateMs
            val cursorState by cursorStateMs
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
            cursorStateMs.value = newCursor
        }
    }

    override fun selectWholeRow(wbws: WbWs) {
        val wsStateMs = appState.getWsStateMs(wbws)
        if (wsStateMs != null) {
            val wsState by wsStateMs
            val cursorStateMs = wsStateMs.value.cursorStateMs
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
    }

    override fun up(
        wbws: WbWs,
    ) {
        appState.getCursorStateMs(wbws)?.also { cursorStateMs ->
            cursorStateMs.value = cursorStateMs.value.up()
            wsAction.makeSliderFollowCursor(cursorStateMs.value, wbws)
        }
    }

    override fun down(
        wbws: WbWs,
    ) {
        appState.getCursorStateMs(wbws)?.also { cursorStateMs ->
            cursorStateMs.value = cursorStateMs.value.down()
            wsAction.makeSliderFollowCursor(cursorStateMs.value, wbws)
        }
    }

    override fun left(
        wbws: WbWs
    ) {
        appState.getCursorStateMs(wbws)?.also { cursorStateMs ->
            cursorStateMs.value = cursorStateMs.value.left()
            wsAction.makeSliderFollowCursor(cursorStateMs.value, wbws)
        }
    }

    override fun right(wbws: WbWs) {
        appState.getCursorStateMs(wbws)?.also { cursorStateMs ->
            cursorStateMs.value = cursorStateMs.value.right()
            wsAction.makeSliderFollowCursor(cursorStateMs.value, wbws)
        }
    }

    override fun moveCursorTo(wbws: WbWs, cellLabel: String) {
        appState.getCursorStateMs(wbws)?.also { cursorStateMs ->
            cursorStateMs.value = cursorStateMs.value.setMainCell(CellAddress(cellLabel))
            wsAction.makeSliderFollowCursor(cursorStateMs.value, wbws)
        }
    }

    override fun shiftUp(wbws: WbWs) {
        appState.getCursorStateMs(wbws)?.also { cursorStateMs ->
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.upOneRow()))
            wsAction.makeSliderFollowCursor(cursorState, wbws)
        }

    }

    override fun shiftDown(
        wbws: WbWs
    ) {
        appState.getCursorStateMs(wbws)?.also { cursorStateMs ->
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.downOneRow()))
            wsAction.makeSliderFollowCursor(cursorState, wbws)
        }
    }

    override fun shiftLeft(
        wbws: WbWs
    ) {
        appState.getCursorStateMs(wbws)?.also { cursorStateMs ->
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.leftOneCol()))
            wsAction.makeSliderFollowCursor(cursorState, wbws)
        }
    }

    override fun shiftRight(
        wbws: WbWs
    ) {
        appState.getCursorStateMs(wbws)?.also { cursorStateMs ->
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.rightOneCol()))
            wsAction.makeSliderFollowCursor(cursorState, wbws)
        }
    }

    override fun f2(wbws: WbWs) {
        openCellEditor.openCellEditor(wbws)
    }

    override fun delete(wbws: WbWs) {
        appState.getCursorState(wbws)?.also { cursorState: CursorState ->
            val req = DeleteMultiAtCursorRequest(
                wbKey = cursorState.id.wbKey,
                wsName = cursorState.id.wsName,
                windowId = null
            )
            wsAction.deleteMultiCellAtCursor(req)
        }
    }

    override fun undo(wbws: WbWs) {
        this.appState.queryStateByWorkbookKey(wbws.wbKey).ifOk {
            val commandStack by it.workbookState.commandStackMs
            val command = commandStack.pop()
            if (command != null) {
                command.undo()
            }
        }
    }
}


