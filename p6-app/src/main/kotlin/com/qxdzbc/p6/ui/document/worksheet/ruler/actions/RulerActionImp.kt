package com.qxdzbc.p6.ui.document.worksheet.ruler.actions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInWindow
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.UpdateCellEditorTextWithRangeSelectorAction
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerSig
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import javax.inject.Inject

class RulerActionImp @Inject constructor(
    private val stateContMs: Ms<StateContainer>,
    val updateCellEditorText: UpdateCellEditorTextWithRangeSelectorAction,
) : RulerAction {

    private var sc by stateContMs

    private fun resizerIsNotActivate(wbwsSt: WbWsSt): Boolean {
        val wsStateMs = sc.getWsStateMs(wbwsSt)
        if (wsStateMs != null) {
            val wsState by wsStateMs
            return !wsState.colResizeBarState.isShow && !wsState.rowResizeBarState.isShow
        } else {
            return false
        }
    }

    private fun makeWholeColOrRowAddress(ii: Int, type:RulerType): RangeAddress {
        return when (type) {
            RulerType.Col -> RangeAddresses.wholeCol(ii)
            RulerType.Row -> RangeAddresses.wholeRow(ii)
        }
    }

    /**
     * Update cell editor text if range selector is allowed. Otherwise, close cell editor
     */
    private fun updateCellEditorTextIfNeed(){
        updateCellEditorText.updateRangeSelectorText()
        if (sc.cellEditorState.isActive) {
            if (!sc.cellEditorState.allowRangeSelector) {
                sc.cellEditorState = sc.cellEditorState.close()
            }
        }
    }

    override fun clickRulerItem(itemIndex: Int, rulerSig: RulerSig) {

        val cursorStateMs = sc.getCursorStateMs(rulerSig)
        val rulerState = sc.getRulerStateMs(rulerSig)?.value

        if (rulerState != null && resizerIsNotActivate(rulerState) && cursorStateMs != null) {
            val cursorState by cursorStateMs
            when (rulerSig.type) {
                RulerType.Col -> {
                    cursorStateMs.value = cursorState
                        .removeAllExceptMainCell()
                        .selectWholeCol(itemIndex)
                        .setMainCell(CellAddresses.firstOfCol(itemIndex))
                }
                RulerType.Row -> {
                    cursorStateMs.value = cursorState
                        .removeAllExceptMainCell()
                        .selectWholeRow(itemIndex)
                        .setMainCell(CellAddresses.firstOfRow(itemIndex))
                }
            }
            this.updateCellEditorTextIfNeed()
        }
    }

    override fun changeColWidth(colIndex: Int, sizeDiff: Float, wbwsSt: WbWsSt) {
        sc.getWsStateMs(wbwsSt)?.also { wsStateMs ->
            wsStateMs.value = wsStateMs.value.changeColSize(colIndex, sizeDiff)
        }

    }

    override fun changeRowHeight(rowIndex: Int, sizeDiff: Float, wbwsSt: WbWsSt) {
        sc.getWsStateMs(wbwsSt)?.also { wsStateMs ->
            val newWsState = wsStateMs.value.changeRowSize(rowIndex, sizeDiff)
            wsStateMs.value = newWsState
        }
    }

    override fun showColResizeBarThumb(index: Int, wbwsSt: WbWsSt) {
        sc.getWsStateMs(wbwsSt)?.also { wsStateMs ->
            val colRulerState = wsStateMs.value.colRulerState
            val wsState by wsStateMs
            val resizerLayout = colRulerState.getResizerLayout(index)
            val wsLayout = wsState.wsLayoutCoorWrapper?.layout
            if (wsLayout != null && wsLayout.isAttached) {
                if (resizerLayout != null && resizerLayout.isAttached) {
                    val p = wsLayout.windowToLocal(resizerLayout.positionInWindow())
                    wsState.colResizeBarStateMs.value = wsState.colResizeBarStateMs.value.changePosition(p).showThumb()
                }
            }
        }
    }

    override fun hideColResizeBarThumb(wbwsSt: WbWsSt) {
        sc.getWsState(wbwsSt)?.also { wsState ->
            var colResizeBar by wsState.colResizeBarStateMs
            if (!colResizeBar.isActive) {
                wsState.colResizeBarStateMs.value = colResizeBar.hideThumb()
            }
        }
    }

    override fun startColResizing(currentPos: Offset, wbwsSt: WbWsSt) {
        sc.getWsState(wbwsSt)?.also { wsState ->
            val colResizeBar by wsState.colResizeBarStateMs
            val wsLayout = wsState.wsLayoutCoors
            if (wsLayout != null && wsLayout.isAttached) {
                val p = wsLayout.windowToLocal(currentPos).copy(y = colResizeBar.position.y)
                wsState.colResizeBarStateMs.value = colResizeBar
                    .changePosition(p)
                    .setAnchor(p)
                    .activate()
                    .show()
            }
        }
    }

    override fun moveColResizer(currentPos: Offset, wbwsSt: WbWsSt) {
        sc.getWsState(wbwsSt)?.also { wsState ->
            val colResizeBar by wsState.colResizeBarStateMs
            if (colResizeBar.isActive) {
                val wsLayout = wsState.wsLayoutCoorWrapper?.layout
                if (wsLayout != null && wsLayout.isAttached) {
                    val p = wsLayout.windowToLocal(currentPos).copy(y = colResizeBar.position.y)
                    wsState.colResizeBarStateMs.value = colResizeBar.changePosition(p).showThumb().show()
                }
            }
        }
    }

    override fun finishColResizing(colIndex: Int, wbwsSt: WbWsSt) {
        sc.getWsState(wbwsSt)?.also { wsState ->
            val colResizeBar by wsState.colResizeBarStateMs
            if (colResizeBar.isShow) {
                val sizeDiff = colResizeBar.position.x - colResizeBar.anchorPoint.x
                this.changeColWidth(colIndex, sizeDiff, wbwsSt)
                wsState.colResizeBarStateMs.value = colResizeBar.deactivate().hideThumb().hide()
            }
        }
    }

    override fun showRowResizeBarThumb(index: Int, wbwsSt: WbWsSt) {
        sc.getWsState(wbwsSt)?.also { wsState ->
            val rowRulerState by wsState.rowRulerStateMs
            val rowResizeBar by wsState.rowResizeBarStateMs
            val resizerLayout = rowRulerState.getResizerLayout(index)
            val wsLayout = wsState.wsLayoutCoorWrapper?.layout
            if (wsLayout != null && wsLayout.isAttached) {
                if (resizerLayout != null && resizerLayout.isAttached) {
                    val p = wsLayout.windowToLocal(resizerLayout.positionInWindow())
                    wsState.rowResizeBarStateMs.value = rowResizeBar.changePosition(p).showThumb()
                }
            }
        }

    }

    override fun hideRowResizeBarThumb(wbwsSt: WbWsSt) {
        sc.getWsState(wbwsSt)?.also { wsState ->
            val rowResizeBar by wsState.rowResizeBarStateMs
            if (!rowResizeBar.isActive) {
                wsState.rowResizeBarStateMs.value = rowResizeBar.hideThumb()
            }
        }
    }

    override fun startRowResizing(currentPos: Offset, wbwsSt: WbWsSt) {
        sc.getWsState(wbwsSt)?.also { wsState ->
            val rowResizeBar by wsState.rowResizeBarStateMs
            val wsLayout = wsState.wsLayoutCoors
            if (wsLayout != null && wsLayout.isAttached) {
                val p = wsLayout.windowToLocal(currentPos).copy(x = rowResizeBar.position.x)
                wsState.rowResizeBarStateMs.value = rowResizeBar
                    .changePosition(p)
                    .setAnchor(p)
                    .activate()
                    .show()
            }
        }
    }

    override fun moveRowResizer(currentPos: Offset, wbwsSt: WbWsSt) {
        sc.getWsState(wbwsSt)?.also { wsState ->
            val rowResizeBar by wsState.rowResizeBarStateMs
            if (rowResizeBar.isActive) {
                val wsLayout = wsState.wsLayoutCoorWrapper?.layout
                if (wsLayout != null && wsLayout.isAttached) {
                    val p = wsLayout.windowToLocal(currentPos).copy(x = rowResizeBar.position.x)
                    wsState.rowResizeBarStateMs.value = rowResizeBar.changePosition(p).showThumb().show()
                }
            }
        }
    }

    override fun finishRowResizing(rowIndex: Int, wbwsSt: WbWsSt) {
        sc.getWsState(wbwsSt)?.also { wsState ->
            val rowResizeBar by wsState.rowResizeBarStateMs
            if (rowResizeBar.isShow) {
                val sizeDiff = rowResizeBar.position.y - rowResizeBar.anchorPoint.y
                this.changeRowHeight(rowIndex, sizeDiff, wbwsSt)
                wsState.rowResizeBarStateMs.value = rowResizeBar.hide().hideThumb().deactivate()
            }
        }
    }

    override fun startDragSelection(mousePosition: Offset, rulerSig: RulerSig) {
        if (resizerIsNotActivate(rulerSig)) {
            sc.getWsState(rulerSig)?.also { wsState ->
                val rulerState = wsState.getRulerState(rulerSig.type)
                val selectRectStateMs = rulerState.itemSelectRectMs
                val selectRectState by selectRectStateMs
                val rulerLayout = rulerState.rulerLayout
                val mouseWindowPos = if (rulerLayout != null && rulerLayout.isAttached) {
                    rulerLayout.localToWindow(mousePosition)
                } else {
                    mousePosition
                }
                selectRectStateMs.value = selectRectState.activate().setAnchorPoint(mouseWindowPos)
                this.updateCellEditorTextIfNeed()
            }
        }
    }

    override fun makeMouseDragSelectionIfPossible(mousePosition: Offset, rulerSig: RulerSig) {
        sc.getWsState(rulerSig)?.also { wsState ->
            val rulerState = wsState.getRulerState(rulerSig.type)
            var selectRectState by rulerState.itemSelectRectMs
            val cursorStateMs = wsState.cursorStateMs
            val cursorState by cursorStateMs
            if (selectRectState.isActive) {
                val rulerLayout = rulerState.rulerLayout
                val mouseWindowPos = if (rulerLayout != null && rulerLayout.isAttached) {
                    rulerLayout.localToWindow(mousePosition)
                } else {
                    mousePosition
                }
                selectRectState = selectRectState.setMovingPoint(mouseWindowPos).show()

                val selectedItems = rulerState.itemLayoutMap.entries.filter { (_, itemLayout) ->
                    selectRectState.rect.overlaps(itemLayout.boundInWindowOrZero)
                }
                if (selectedItems.isNotEmpty()) {
                    val mergedRange:RangeAddress = selectedItems.fold(
                        makeWholeColOrRowAddress(selectedItems.first().key, rulerState.type)
                    ) { acc:RangeAddress, (i:Int, l:LayoutCoorWrapper) ->
                        acc.mergeWith(makeWholeColOrRowAddress(i, rulerState.type))
                    }
                    val newAnchorCell: CellAddress = if (cursorState.mainCell in mergedRange) {
                        cursorState.mainCell
                    } else {
                        mergedRange.topLeft
                    }
                    cursorStateMs.value = cursorState.setMainRange(mergedRange).setMainCell(newAnchorCell)
                } else {
                    cursorStateMs.value = cursorState.removeMainRange()
                }
                this.updateCellEditorTextIfNeed()
            }
        }

    }

    override fun stopDragSelection(rulerSig: RulerSig) {
        val rs = sc.getRulerState(rulerSig)
        rs?.also {
            val srMs = it.itemSelectRectMs
            srMs.value = srMs.value.deactivate().hide()
            this.updateCellEditorTextIfNeed()
        }
    }

    override fun updateItemLayout(itemIndex: Int, itemLayout: LayoutCoorWrapper, rulerSig: RulerSig) {
        sc.getRulerStateMs(rulerSig)?.also {
            it.value = it.value.addItemLayout(itemIndex, itemLayout)
            val srMs = it.value.itemSelectRectMs
            srMs.value = srMs.value.deactivate().hide()
        }
    }

    override fun updateRulerLayout(layout: LayoutCoordinates, rulerSig: RulerSig) {
        sc.getRulerStateMs(rulerSig)?.also {
            it.value = it.value.setLayout(layout)
        }
    }

    override fun shiftClick(itemIndex: Int, rulerSig: RulerSig) {
        sc.getWsState(rulerSig)?.also { wsState ->
            var cursorState by wsState.cursorStateMs
            val newRange = when (rulerSig.type) {
                RulerType.Col -> {
                    val currentCol = cursorState.mainCell.colIndex
                    RangeAddresses.wholeMultiCol(currentCol, itemIndex)
                }
                RulerType.Row -> {
                    val currentRow = cursorState.mainCell.rowIndex
                    RangeAddresses.wholeMultiRow(currentRow, itemIndex)
                }
            }
            wsState.cursorStateMs.value = cursorState
                .setMainRange(newRange)
                .removeAllFragmentedCells()
                .removeAllSelectedFragRange()
        }
    }

    override fun ctrlClick(itemIndex: Int, rulerSig: RulerSig) {
        sc.getWsState(rulerSig)?.also { wsState ->
            var cursorState by wsState.cursorStateMs
            val newRange = when (rulerSig.type) {
                RulerType.Col -> {
                    RangeAddresses.wholeCol(itemIndex)
                }
                RulerType.Row -> {
                    RangeAddresses.wholeRow(itemIndex)
                }
            }
            val newCursorState = if (newRange in cursorState.fragmentedRanges) {
                 cursorState.removeFragRange(newRange)
            } else {
                 cursorState.addFragRange(newRange)
            }
            wsState.cursorStateMs.value = newCursorState
        }
    }

    override fun updateResizerLayout(itemIndex: Int, layout: LayoutCoordinates, rulerState: RulerState) {
        val rulerStateMs = sc.getRulerStateMs(rulerState)
        if (rulerStateMs != null) {
            rulerStateMs.value = rulerStateMs.value.addResizerLayout(itemIndex, layout)
        }
    }
}
