package com.qxdzbc.p6.ui.worksheet.cursor.thumb.state

import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.ui.worksheet.cursor.di.qualifiers.MainCellState
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultSelectRectState
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.worksheet.di.comp.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.select_rect.SelectRectState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(WsAnvilScope::class)
data class ThumbStateImp @Inject constructor(
    private val cursorIdSt: St<@JvmSuppressWildcards CursorId>,
    @MainCellState
    private val mainCellSt: St<@JvmSuppressWildcards CellAddress>,
    private val cellLayoutCoorMapSt: St<@JvmSuppressWildcards Map<CellAddress, P6LayoutCoor>>,
    @DefaultSelectRectState
    override val selectRectState: SelectRectState,
) : ThumbState {

    override val size: DpSize = DpSize(8.dp, 8.dp)
    override val offsetNegate: DpSize = DpSize(5.dp, 5.dp)

    override val cursorId: CursorId by cursorIdSt

    override val mainCell: CellAddress by mainCellSt

    override val cellLayoutCoorMap: Map<CellAddress, P6LayoutCoor> by cellLayoutCoorMapSt

    override val isShowingSelectedRange: Boolean get() = selectRectState.isShow


    fun getCellAtTheCross(): Map<CellAddress, P6LayoutCoor> {
        return cellLayoutCoorMap.filterKeys {
            it.colIndex == mainCell.colIndex || it.rowIndex == mainCell.rowIndex
        }
    }

    fun getRelevantCells(): Map<CellAddress, P6LayoutCoor> {
        val crossCells = getCellAtTheCross()
        val relevantCells: Map<CellAddress, P6LayoutCoor> = with(selectRectState)
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
                        y + cellLayout.pixelSizeOrZero.height >= selectRectState.movingPoint.y
                    } ?: false
                    c1 && c2 && c3
                }
            } else if (isMovingToTheLeft(ratio)) {
                crossCells.filter { (cellAddress, cellLayout) ->
                    val c1 = cellAddress.rowIndex == mainCell.rowIndex
                    val c2 = cellAddress.colIndex <= mainCell.colIndex
                    val c3 = cellLayout.posInWindow?.x?.let{ x->
                        x + cellLayout.pixelSizeOrZero.width >= selectRectState.movingPoint.x
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

    override fun getTopBotCells(): Pair<CellAddress, CellAddress>? {
        return selectedRange?.let {
            it.topLeft to it.botRight
        }
    }

    override fun getStartEndCells(): Pair<CellAddress, CellAddress> {
        val rt=getTopBotCells()?.let{pair->
            val (top,bot) = pair
            if(top == mainCell){
                return pair
            }else{
                return bot to top
            }
        }?:(mainCell to mainCell)
        return rt
    }

    override val selectedRange: RangeAddress?
        get() {
            val relevantCells = getRelevantCells()
            if (relevantCells.isNotEmpty()) {
                val range = RangeAddress(relevantCells.keys.toList())
                return range
            } else {
                return null
            }
        }

    override val selectedRangeSizeOrZero: Size get() = selectedRangeSize ?: Size.Zero

    override val selectedRangeSize: Size?
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
                        val rt = Size(
                            width = maxOf(botOffset.x - topOffset.x, 0f),
                            height = maxOf(botOffset.y - topOffset.y, 0f),
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
                if (topCellLayout != null && topCellLayout.isAttached) {
                    return topCellLayout.posInWindowOrZero
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
