package com.emeraldblast.p6.ui.document.worksheet.ruler.actions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInWindow
import com.emeraldblast.p6.app.document.cell.address.CellAddresses
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddresses
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.worksheet.ruler.RulerState
import com.emeraldblast.p6.ui.document.worksheet.ruler.RulerType
import com.emeraldblast.p6.ui.document.worksheet.select_rect.SelectRectState
import javax.inject.Inject

class RulerActionImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>
) : RulerAction {

    private var appState by appStateMs

    private fun resizerIsNotActivate(rulerState: RulerState): Boolean {
        val wsStateMs = appState.getWsStateMs(rulerState)
        if (wsStateMs != null) {
            val wsState by wsStateMs
            return !wsState.colResizeBarState.isShow && !wsState.rowResizeBarState.isShow
        } else {
            return false
        }
    }

    private fun selectRowOrCol2(ii: Int, rulerState: RulerState): RangeAddress {
        return when (rulerState.dimen) {
            RulerType.Col -> RangeAddresses.wholeCol(ii)
            RulerType.Row -> RangeAddresses.wholeRow(ii)
        }
    }

    override fun clickRulerItem(itemIndex: Int, rulerState: RulerState) {
        val cursorStateMs = appState.getCursorStateMs(rulerState)

        if (resizerIsNotActivate(rulerState) && cursorStateMs != null) {
            var cursorState by cursorStateMs
            when (rulerState.dimen) {
                RulerType.Col -> {
                    cursorState = cursorState
                        .removeAllExceptAnchorCell()
                        .selectWholeCol(itemIndex)
                        .setMainCell(CellAddresses.firstOfCol(itemIndex))
                }
                RulerType.Row -> {
                    cursorState = cursorState
                        .removeAllExceptAnchorCell()
                        .selectWholeRow(itemIndex)
                        .setMainCell(CellAddresses.firstOfRow(itemIndex))
                }
            }
        }
    }

    override fun changeColWidth(colIndex: Int, sizeDiff: Float, rulerState: RulerState) {
        appState.getWsStateMs(rulerState)?.also { wsStateMs ->
            wsStateMs.value = wsStateMs.value.changeColSize(colIndex, sizeDiff)
        }

    }

    override fun changeRowHeight(rowIndex: Int, sizeDiff: Float, rulerState: RulerState) {
        appState.getWsStateMs(rulerState)?.also { wsStateMs ->
            val r = wsStateMs.value.changeRowSize(rowIndex, sizeDiff)
            wsStateMs.value = r
        }
    }

    override fun showColResizeBarThumb(index: Int, rulerState: RulerState) {
        appState.getWsStateMs(rulerState)?.also { wsStateMs ->
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
        appState.getWsState(rulerState)?.also { wsState ->
            var colResizeBar by wsState.colResizeBarStateMs
            if (!colResizeBar.isActive) {
                colResizeBar = colResizeBar.hideThumb()
            }
        }
    }

    override fun startColResizing(currentPos: Offset, rulerState: RulerState) {
        appState.getWsState(rulerState)?.also { wsState ->
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
        appState.getWsState(rulerState)?.also { wsState ->
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
        appState.getWsState(rulerState)?.also { wsState ->
            var colResizeBar by wsState.colResizeBarStateMs
            if (colResizeBar.isShow) {
                val sizeDiff = colResizeBar.position.x - colResizeBar.anchorPoint.x
                this.changeColWidth(colIndex, sizeDiff, rulerState)
                colResizeBar = colResizeBar.deactivate().hideThumb().hide()
            }
        }

    }

    override fun showRowResizeBarThumb(index: Int, rulerState: RulerState) {
        appState.getWsState(rulerState)?.also { wsState ->
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
        appState.getWsState(rulerState)?.also { wsState ->
            var rowResizeBar by wsState.rowResizeBarStateMs
            if (!rowResizeBar.isActive) {
                rowResizeBar = rowResizeBar.hideThumb()
            }
        }
    }

    override fun startRowResizing(currentPos: Offset, rulerState: RulerState) {
        appState.getWsState(rulerState)?.also { wsState ->
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
        appState.getWsState(rulerState)?.also { wsState ->
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
        appState.getWsState(rulerState)?.also { wsState ->
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
            appState.getWsState(rulerState)?.also { wsState ->
                var selectRectState by rulerState.itemSelectRectMs
                val rulerLayout = rulerState.rulerLayout
                val mouseWindowPos = if (rulerLayout != null && rulerLayout.isAttached()) {
                    rulerLayout.localToWindow(mousePosition)
                } else {
                    mousePosition
                }
                selectRectState = selectRectState.activate().setAnchorPoint(mouseWindowPos)
            }
        }
    }

    //
    override fun makeMouseDragSelectionIfPossible(mousePosition: Offset, rulerState: RulerState) {
        appState.getWsState(rulerState)?.also { wsState ->
            var selectRectState by rulerState.itemSelectRectMs
            var cursorState by wsState.cursorStateMs
            if (selectRectState.isActive) {
                val rulerLayout = rulerState.rulerLayout
                val mouseWindowPos = if (rulerLayout != null && rulerLayout.isAttached()) {
                    rulerLayout.localToWindow(mousePosition)
                } else {
                    mousePosition
                }
                selectRectState = selectRectState.setMovingPoint(mouseWindowPos).show()

                val selectedItems = rulerState.itemLayoutMap.entries.filter { (_, layout) ->
                    val itemRect = layout.boundInWindow
                    selectRectState.rect.overlaps(itemRect)
                }
                if (selectedItems.isNotEmpty()) {
                    var mergedRange = selectRowOrCol2(selectedItems.first().key, rulerState)
                    for ((i, l) in selectedItems) {
                        mergedRange = mergedRange.mergeWith(selectRowOrCol2(i, rulerState))
                    }
                    val newAnchorCell = if (cursorState.mainCell !in mergedRange) {
                        mergedRange.topLeft
                    } else {
                        cursorState.mainCell
                    }
                    cursorState = cursorState.setMainRange(mergedRange).setMainCell(newAnchorCell)
                } else {
                    cursorState = cursorState.removeMainRange()
                }

            }
        }

    }

    private fun getSelectRectState(rulerState: RulerState): Ms<SelectRectState> {
        return rulerState.itemSelectRectMs
    }

    private fun getRulerStateMs(rulerState: RulerState): Ms<RulerState>? {
        return appState.getWsStateMs(rulerState)?.let {
            if (rulerState.dimen == RulerType.Row) {
                it.value.rowRulerStateMs
            } else {
                it.value.colRulerStateMs
            }
        }
    }

    override fun stopDragSelection(rulerState: RulerState) {
        val srMs = getSelectRectState(rulerState)
        if (srMs != null) {
            srMs.value = srMs.value.deactivate().hide()
        }
    }

    override fun updateItemLayout(itemIndex: Int, itemLayout: LayoutCoorWrapper, rulerState: RulerState) {
        getRulerStateMs(rulerState)?.also {
            it.value = it.value.addItemLayout(itemIndex, itemLayout)
            val srMs = getSelectRectState(rulerState)
            if (srMs != null) {
                srMs.value = srMs.value.deactivate().hide()
            }
        }
    }

    override fun updateRulerLayout(layout: LayoutCoordinates, rulerState: RulerState) {
        getRulerStateMs(rulerState)?.also {
            it.value = rulerState.setLayout(layout)
        }
    }

    override fun shiftClick(itemIndex: Int,rulerState: RulerState) {
        appState.getWsState(rulerState)?.also { wsState ->
            var cursorState by wsState.cursorStateMs
            val newRange = when (rulerState.dimen) {
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

    override fun ctrlClick(itemIndex: Int,rulerState: RulerState) {
        appState.getWsState(rulerState)?.also { wsState ->
            var cursorState by wsState.cursorStateMs
            val newRange = when (rulerState.dimen) {
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

    override fun updateResizerLayout(itemIndex: Int, layout: LayoutCoordinates,rulerState: RulerState) {
        val rulerStateMs = getRulerStateMs(rulerState)
        if(rulerStateMs!=null){
            rulerStateMs.value = rulerStateMs.value.addResizerLayout(itemIndex, layout)
        }
    }
}
