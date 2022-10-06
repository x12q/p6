package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state

import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.di.state.ws.DefaultSelectRectState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectStateImp
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

data class ThumbStateImp @AssistedInject constructor(
    @Assisted("1") private val cursorStateIdSt: St<CursorStateId>,
    @Assisted("2") private val mainCellSt: St<CellAddress>,
    @Assisted("3") private val cellLayoutCoorMapSt: St<Map<CellAddress, LayoutCoorWrapper>>,
    @DefaultSelectRectState
    override val selectRectState: SelectRectState = SelectRectStateImp(),
) : ThumbState {

    override val size: DpSize = DpSize(8.dp, 8.dp)
    override val offsetNegate: DpSize = DpSize(5.dp, 5.dp)

    override val cursorId: CursorStateId by cursorStateIdSt

    override val mainCell: CellAddress by mainCellSt

    override val cellLayoutCoorMap: Map<CellAddress, LayoutCoorWrapper> by cellLayoutCoorMapSt

    override val isShowingSelectedRange: Boolean get() = selectRectState.isShow


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
                    val c1 = cellAddress.colIndex == mainCell.colIndex
                    val c2 = cellAddress.rowIndex >= mainCell.rowIndex
                    val c3 = cellLayout.posInWindow?.y?.let { it <= selectRectState.movingPoint.y } ?: false
                    c1 && c2 && c3
                }
            } else if (isMovingUpward(ratio)) {
                crossCells.filter { (cellAddress, cellLayout) ->
                    val c1 = cellAddress.colIndex == mainCell.colIndex
                    val c2 = cellAddress.rowIndex <= mainCell.rowIndex
                    val c3 = cellLayout.posInWindow?.y?.let{y->
                        y + cellLayout.sizeOrZero.height.value >= selectRectState.movingPoint.y
                    } ?: false
                    c1 && c2 && c3
                }
            } else if (isMovingToTheLeft(ratio)) {
                crossCells.filter { (cellAddress, cellLayout) ->
                    val c1 = cellAddress.rowIndex == mainCell.rowIndex
                    val c2 = cellAddress.colIndex <= mainCell.colIndex
                    val c3 = cellLayout.posInWindow?.x?.let{ x->
                        x + cellLayout.sizeOrZero.width.value >= selectRectState.movingPoint.x
                    } ?: false
                    c1 && c2 && c3
                }
            } else if (isMovingToTheRight(ratio)) {
                crossCells.filter { (cellAddress, cellLayout) ->
                    val c1 = cellAddress.rowIndex == mainCell.rowIndex
                    val c2 = cellAddress.colIndex >= mainCell.colIndex
                    val c3 = cellLayout.posInWindow?.x?.let{x->
                        x <= selectRectState.movingPoint.x
                    } ?: false
                    c1 && c2 && c3
                }
            } else {
                emptyMap()
            }
        }
        return relevantCells
    }

    fun getTopBotCells(): Pair<CellAddress, CellAddress>? {
        val relevantCells = getRelevantCells()
        if (relevantCells.isNotEmpty()) {
            val range = RangeAddress(relevantCells.keys.toList())
            val topCell = range.topLeft
            val botCell = range.botRight
            return topCell to botCell
        } else {
            return null
        }
    }

    override val selectedRangeSizeOrZero: DpSize get() = selectedRangeSize ?: DpSize.Zero

    override val selectedRangeSize: DpSize?
        get() {
            val tb = getTopBotCells()
            if (tb != null) {
                val (topCell, botCell) = tb
                val topCellLayout = cellLayoutCoorMap[topCell]
                val botCellLayout = cellLayoutCoorMap[botCell]
                if (topCellLayout != null && botCellLayout != null) {
                    if (topCellLayout.isAttached && botCellLayout.isAttached) {
                        val topOffset = topCellLayout.boundInWindowOrZero.topLeft
                        val botOffset = botCellLayout.boundInWindowOrZero.bottomRight
                        val rt = DpSize(
                            width = (maxOf(botOffset.x - topOffset.x, 0f)).dp,
                            height = (maxOf(botOffset.y - topOffset.y, 0f)).dp
                        )
                        return rt
                    } else {
                        return null
                    }
                } else {
                    return null
                }
            } else {
                return null
            }
        }

    override val selectedRangeWindowOffsetOrZero: Offset get() = selectedRangeWindowOffset ?: Offset.Zero

    override val selectedRangeWindowOffset: Offset?
        get() {
            val tb = getTopBotCells()
            if (tb != null) {
                val (topCell, botCell) = tb
                val topCellLayout = cellLayoutCoorMap[topCell]
                if (topCellLayout != null && topCellLayout.isAttached ?: false) {
                    return topCellLayout.posInWindowOrZero ?: Offset.Zero
                } else {
                    return null
                }
            } else {
                return null
            }
        }

    override fun setSelectRectState(i: SelectRectState): ThumbStateImp {
        return this.copy(selectRectState = i)
    }
}
