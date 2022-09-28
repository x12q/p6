package com.qxdzbc.p6.ui.document.worksheet.ruler.actions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInWindow
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextAction
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.UpdateCellEditorTextWithRangeSelectorAction
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState
import javax.inject.Inject

class RulerActionImp @Inject constructor(
    @StateContainerMs private val stateContMs: Ms<StateContainer>,
    private val makeCellEditorText: MakeCellEditorDisplayTextAction,
    val updateCellEditorText: UpdateCellEditorTextWithRangeSelectorAction,
) : RulerAction {

    private var sc by stateContMs

    private fun resizerIsNotActivate(rulerState: RulerState): Boolean {
        val wsStateMs = sc.getWsStateMs(rulerState)
        if (wsStateMs != null) {
            val wsState by wsStateMs
            return !wsState.colResizeBarState.isShow && !wsState.rowResizeBarState.isShow
        } else {
            return false
        }
    }

    private fun selectRowOrCol2(ii: Int, rulerState: RulerState): RangeAddress {
        return when (rulerState.type) {
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

    override fun clickRulerItem(itemIndex: Int, wbwsSt: WbWsSt, type: RulerType) {

        val cursorStateMs = sc.getCursorStateMs(wbwsSt)
        val rulerState = sc.getRulerStateMs(wbwsSt, type)?.value

        if (rulerState != null && resizerIsNotActivate(rulerState) && cursorStateMs != null) {
            val cursorState by cursorStateMs
            when (type) {
                RulerType.Col -> {
                    cursorStateMs.value = cursorState
                        .removeAllExceptAnchorCell()
                        .selectWholeCol(itemIndex)
                        .setMainCell(CellAddresses.firstOfCol(itemIndex))
                }
                RulerType.Row -> {
                    cursorStateMs.value = cursorState
                        .removeAllExceptAnchorCell()
                        .selectWholeRow(itemIndex)
                        .setMainCell(CellAddresses.firstOfRow(itemIndex))
                }
            }
            this.updateCellEditorTextIfNeed()
        }
    }

    override fun changeColWidth(colIndex: Int, sizeDiff: Float, rulerState: RulerState) {
        sc.getWsStateMs(rulerState)?.also { wsStateMs ->
            wsStateMs.value = wsStateMs.value.changeColSize(colIndex, sizeDiff)
        }

    }

    override fun changeRowHeight(rowIndex: Int, sizeDiff: Float, rulerState: RulerState) {
        sc.getWsStateMs(rulerState)?.also { wsStateMs ->
            val newWsState = wsStateMs.value.changeRowSize(rowIndex, sizeDiff)
            wsStateMs.value = newWsState
        }
    }

    override fun showColResizeBarThumb(index: Int, rulerState: RulerState) {
        sc.getWsStateMs(rulerState)?.also { wsStateMs ->
            val wsState by wsStateMs
            val resizerLayout = rulerState.getResizerLayout(index)
            val wsLayout = wsState.wsLayoutCoorWrapper?.layout
            if (wsLayout != null && wsLayout.isAttached) {
                if (resizerLayout != null && resizerLayout.isAttached) {
                    val p = wsLayout.windowToLocal(resizerLayout.positionInWindow())
                    wsState.colResizeBarStateMs.value = wsState.colResizeBarStateMs.value.changePosition(p).showThumb()
                }
            }
        }

    }

    override fun hideColResizeBarThumb(rulerState: RulerState) {
        sc.getWsState(rulerState)?.also { wsState ->
            var colResizeBar by wsState.colResizeBarStateMs
            if (!colResizeBar.isActive) {
                colResizeBar = colResizeBar.hideThumb()
            }
        }
    }

    override fun startColResizing(currentPos: Offset, rulerState: RulerState) {
        sc.getWsState(rulerState)?.also { wsState ->
            var colResizeBar by wsState.colResizeBarStateMs
            val wsLayout = wsState.wsLayoutCoors
            if (wsLayout != null && wsLayout.isAttached) {
                val p = wsLayout.windowToLocal(currentPos).copy(y = colResizeBar.position.y)
                colResizeBar = colResizeBar
                    .changePosition(p)
                    .setAnchor(p)
                    .activate()
                    .show()
            }
        }
    }

    override fun moveColResizer(currentPos: Offset, rulerState: RulerState) {
        sc.getWsState(rulerState)?.also { wsState ->
            var colResizeBar by wsState.colResizeBarStateMs
            if (colResizeBar.isActive) {
                val wsLayout = wsState.wsLayoutCoorWrapper?.layout
                if (wsLayout != null && wsLayout.isAttached) {
                    val p = wsLayout.windowToLocal(currentPos).copy(y = colResizeBar.position.y)
                    colResizeBar = colResizeBar.changePosition(p).showThumb().show()
                }
            }
        }
    }

    override fun finishColResizing(colIndex: Int, rulerState: RulerState) {
        sc.getWsState(rulerState)?.also { wsState ->
            var colResizeBar by wsState.colResizeBarStateMs
            if (colResizeBar.isShow) {
                val sizeDiff = colResizeBar.position.x - colResizeBar.anchorPoint.x
                this.changeColWidth(colIndex, sizeDiff, rulerState)
                colResizeBar = colResizeBar.deactivate().hideThumb().hide()
            }
        }

    }

    override fun showRowResizeBarThumb(index: Int, rulerState: RulerState) {
        sc.getWsState(rulerState)?.also { wsState ->
            var rowResizeBar by wsState.rowResizeBarStateMs
            val resizerLayout = rulerState.getResizerLayout(index)
            val wsLayout = wsState.wsLayoutCoorWrapper?.layout
            if (wsLayout != null && wsLayout.isAttached) {
                if (resizerLayout != null && resizerLayout.isAttached) {
                    val p = wsLayout.windowToLocal(resizerLayout.positionInWindow())
                    rowResizeBar = rowResizeBar.changePosition(p).showThumb()
                }
            }
        }

    }

    override fun hideRowResizeBarThumb(rulerState: RulerState) {
        sc.getWsState(rulerState)?.also { wsState ->
            var rowResizeBar by wsState.rowResizeBarStateMs
            if (!rowResizeBar.isActive) {
                rowResizeBar = rowResizeBar.hideThumb()
            }
        }
    }

    override fun startRowResizing(currentPos: Offset, rulerState: RulerState) {
        sc.getWsState(rulerState)?.also { wsState ->
            var rowResizeBar by wsState.rowResizeBarStateMs
            val wsLayout = wsState.wsLayoutCoors
            if (wsLayout != null && wsLayout.isAttached) {
                val p = wsLayout.windowToLocal(currentPos).copy(x = rowResizeBar.position.x)
                rowResizeBar = rowResizeBar
                    .changePosition(p)
                    .setAnchor(p)
                    .activate()
                    .show()
            }
        }
    }

    override fun moveRowResizer(currentPos: Offset, rulerState: RulerState) {
        sc.getWsState(rulerState)?.also { wsState ->
            var rowResizeBar by wsState.rowResizeBarStateMs
            if (rowResizeBar.isActive) {
                val wsLayout = wsState.wsLayoutCoorWrapper?.layout
                if (wsLayout != null && wsLayout.isAttached) {
                    val p = wsLayout.windowToLocal(currentPos).copy(x = rowResizeBar.position.x)
                    rowResizeBar = rowResizeBar.changePosition(p).showThumb().show()
                }
            }
        }
    }

    override fun finishRowResizing(rowIndex: Int, rulerState: RulerState) {
        sc.getWsState(rulerState)?.also { wsState ->
            var rowResizeBar by wsState.rowResizeBarStateMs
            if (rowResizeBar.isShow) {
                val sizeDiff = rowResizeBar.position.y - rowResizeBar.anchorPoint.y
                this.changeRowHeight(rowIndex, sizeDiff, rulerState)
                rowResizeBar = rowResizeBar.hide().hideThumb().deactivate()
            }
        }
    }

    override fun startDragSelection(mousePosition: Offset, rulerState: RulerState) {
        if (resizerIsNotActivate(rulerState)) {
            sc.getWsState(rulerState)?.also { wsState ->
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

    //
    override fun makeMouseDragSelectionIfPossible(mousePosition: Offset, rulerState: RulerState) {
        sc.getWsState(rulerState)?.also { wsState ->
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
                    selectRectState.rect.overlaps(itemLayout.boundInWindow)
                }
                if (selectedItems.isNotEmpty()) {
                    val mergedRange:RangeAddress = selectedItems.fold(
                        selectRowOrCol2(selectedItems.first().key, rulerState)
                    ) { acc:RangeAddress, (i:Int, l:LayoutCoorWrapper) ->
                        acc.mergeWith(selectRowOrCol2(i, rulerState))
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

    private fun getSelectRectState(rulerState: RulerState): Ms<SelectRectState> {
        return rulerState.itemSelectRectMs
    }

    private fun getRulerStateMs(rulerState: RulerState): Ms<RulerState>? {
        return sc.getWsStateMs(rulerState)?.let {
            if (rulerState.type == RulerType.Row) {
                it.value.rowRulerStateMs
            } else {
                it.value.colRulerStateMs
            }
        }
    }

    override fun stopDragSelection(rulerState: RulerState) {
        val srMs = getSelectRectState(rulerState)
        srMs.value = srMs.value.deactivate().hide()
        this.updateCellEditorTextIfNeed()

    }

    override fun updateItemLayout(itemIndex: Int, itemLayout: LayoutCoorWrapper, rulerState: RulerState) {
        getRulerStateMs(rulerState)?.also {
            it.value = it.value.addItemLayout(itemIndex, itemLayout)
            val srMs = getSelectRectState(rulerState)
            srMs.value = srMs.value.deactivate().hide()
        }
    }

    override fun updateRulerLayout(layout: LayoutCoordinates, rulerState: RulerState) {
        getRulerStateMs(rulerState)?.also {
            it.value = rulerState.setLayout(layout)
        }
    }

    override fun shiftClick(itemIndex: Int, rulerState: RulerState) {
        sc.getWsState(rulerState)?.also { wsState ->
            var cursorState by wsState.cursorStateMs
            val newRange = when (rulerState.type) {
                RulerType.Col -> {
                    val currentCol = cursorState.mainCell.colIndex
                    RangeAddresses.wholeMultiCol(currentCol, itemIndex)
                }
                RulerType.Row -> {
                    val currentRow = cursorState.mainCell.rowIndex
                    RangeAddresses.wholeMultiRow(currentRow, itemIndex)
                }
            }
            cursorState = cursorState
                .setMainRange(newRange)
                .removeAllFragmentedCells()
                .removeAllSelectedFragRange()
        }
    }

    override fun ctrlClick(itemIndex: Int, rulerState: RulerState) {
        sc.getWsState(rulerState)?.also { wsState ->
            var cursorState by wsState.cursorStateMs
            val newRange = when (rulerState.type) {
                RulerType.Col -> {
                    RangeAddresses.wholeCol(itemIndex)
                }
                RulerType.Row -> {
                    RangeAddresses.wholeRow(itemIndex)
                }
            }
            if (newRange in cursorState.fragmentedRanges) {
                cursorState = cursorState.removeFragRange(newRange)
            } else {
                cursorState = cursorState.addFragRange(newRange)
            }
        }
    }

    override fun updateResizerLayout(itemIndex: Int, layout: LayoutCoordinates, rulerState: RulerState) {
        val rulerStateMs = getRulerStateMs(rulerState)
        if (rulerStateMs != null) {
            rulerStateMs.value = rulerStateMs.value.addResizerLayout(itemIndex, layout)
        }
    }
}
