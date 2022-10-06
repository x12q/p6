package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb

import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectStateImp
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

data class ThumbStateImp @AssistedInject constructor(
    @Assisted("1") private val cursorStateSt: St<CursorState>,
    @Assisted("2")private val cellLayoutCoorMapSt: St<Map<CellAddress, LayoutCoorWrapper>> = ms(emptyMap()),
    override val isShowingSelectedRange: Boolean = false,
    override val selectRectState: SelectRectState = SelectRectStateImp(),
) : ThumbState {

    override val cursorId: CursorStateId get() = cursorStateSt.value.id

    override val mainCell: CellAddress get() = cursorStateSt.value.mainCell

    override val cellLayoutCoorMap: Map<CellAddress, LayoutCoorWrapper> by cellLayoutCoorMapSt

    override fun setIsShowingSelectedRange(i: Boolean): ThumbState {
        return this.copy(isShowingSelectedRange = i)
    }

    fun getCellAtTheCross(): Map<CellAddress, LayoutCoorWrapper> {
        return cellLayoutCoorMap.filterKeys {
            it.colIndex == mainCell.colIndex || it.rowIndex == mainCell.rowIndex
        }
    }

    fun getRelevantCells(): Map<CellAddress, LayoutCoorWrapper> {
        val crossCells = getCellAtTheCross()
        val relevantCells: Map<CellAddress, LayoutCoorWrapper> = with(selectRectState)
        {
            val ratio = 1.0
            if (isMovingDownward(ratio)) {
                crossCells.filter { (cellAddress, cellLayout) ->
                    cellAddress.colIndex == mainCell.colIndex
                            && cellAddress.rowIndex >= mainCell.rowIndex
                            && (cellLayout.posInWindow.y <= selectRectState.movingPoint.y)
                }
            } else if (isMovingUpward(ratio)) {
                crossCells.filter { (cellAddress, cellLayout) ->
                    cellAddress.colIndex == mainCell.colIndex
                            && cellAddress.rowIndex <= mainCell.rowIndex
                            && cellLayout.posInWindow.y + cellLayout.size.height.value >= selectRectState.movingPoint.y
                }
            } else if (isMovingToTheLeft(ratio)) {
                crossCells.filter { (cellAddress, cellLayout) ->
                    cellAddress.rowIndex == mainCell.rowIndex
                            && cellAddress.colIndex <= mainCell.colIndex
                            && cellLayout.posInWindow.x + cellLayout.size.width.value >= selectRectState.movingPoint.x
                }
            } else if (isMovingToTheRight(ratio)) {
                crossCells.filter { (cellAddress, cellLayout) ->
                    cellAddress.rowIndex == mainCell.rowIndex
                            && cellAddress.colIndex >= mainCell.colIndex
                            && cellLayout.posInWindow.x <= selectRectState.movingPoint.x
                }
            } else {
                emptyMap()
            }
        }
        return relevantCells
    }

    fun getTopBotCells(): Pair<CellAddress, CellAddress>? {
        val relevantCells = getRelevantCells()
        if(relevantCells.isNotEmpty()){
            val range = RangeAddress(relevantCells.keys.toList())
            val topCell = range.topLeft
            val botCell = range.botRight
            return topCell to botCell
        }else{
            return null
        }
    }

    override val selectedRangeSize: DpSize
        get() {
            val tb = getTopBotCells()
            if(tb!=null){
                val (topCell, botCell) = tb
                val topCellLayout = cellLayoutCoorMap[topCell]
                val botCellLayout = cellLayoutCoorMap[botCell]
                if (topCellLayout != null && botCellLayout != null) {
                    val topOffset = topCellLayout.boundInWindow.topLeft
                    val botOffset = botCellLayout.boundInWindow.bottomRight
                    return DpSize(
                        width = (botOffset.x - topOffset.x).dp,
                        height = (botOffset.y - topOffset.y).dp
                    )
                } else {
                    return DpSize.Zero
                }
            }else{
                return DpSize.Zero
            }
        }
    override val selectedRangeOffset: Offset
        get() {
            val tb = getTopBotCells()
            if(tb!=null){
                val (topCell, botCell) = tb
                val topCellLayout = cellLayoutCoorMap[topCell]
                return topCellLayout?.posInWindow ?: Offset.Zero
            }else{
                return Offset.Zero
            }
        }

    override fun setSelectRectState(i: SelectRectState): ThumbStateImp {
        return this.copy(selectRectState = i)
    }
}
