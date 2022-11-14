package com.qxdzbc.p6.app.action.cursor.thumb.drag_thumb_action

import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateAction
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class DragThumbActionImp @Inject constructor(
    val stateContainerSt: St<@JvmSuppressWildcards StateContainer>,
//    private val copyCellAct: CopyCellAction,
//    private val multiCellUpdateAct:MultiCellUpdateAction,
    private val endThumbDragAction: EndThumbDragAction,
) : DragThumbAction {

    val sc by stateContainerSt

    private fun forTest(wbws: WbWsSt, cellAddress: CellAddress, f: (cellLayoutCoor: LayoutCoorWrapper) -> Unit) {
        sc.getThumbStateMsRs(wbws)
            .onSuccess { thumbStateMs ->
                val ts by thumbStateMs
                ts.cellLayoutCoorMap[cellAddress]?.also { cellLayoutCoor ->
                    f(cellLayoutCoor)
                }
            }
    }

    private fun findThumbStateThen(wbws: WbWsSt, f: (Ms<ThumbState>) -> Unit) {
        sc.getThumbStateMsRs(wbws)
            .onSuccess { thumbStateMs ->
                f(thumbStateMs)
            }
    }

    override fun startDrag_forTest(wbws: WbWsSt, cellAddress: CellAddress) {
        forTest(wbws, cellAddress) {
            startDrag(wbws, it.posInWindowOrZero)
        }
    }

    override fun startDrag(wbws: WbWsSt, mouseWindowOffset: Offset) {
        findThumbStateThen(wbws) { thumbStateMs ->
            val ts by thumbStateMs
            val rect = ts.selectRectState
            thumbStateMs.value = ts
                .setSelectRectState(
                    rect.setAnchorPoint(mouseWindowOffset)
                        .setMovingPoint(mouseWindowOffset)
                        .setActiveStatus(true)
                )
        }
    }

    override fun drag_forTest(wbws: WbWsSt, cellAddress: CellAddress) {
        forTest(wbws, cellAddress) {
            drag(wbws, it.posInWindowOrZero)
        }
    }

    override fun drag(wbws: WbWsSt, mouseWindowOffset: Offset) {
        findThumbStateThen(wbws) { thumbStateMs ->
            val ts by thumbStateMs
            if (ts.selectRectState.isActive) {
                thumbStateMs.value = ts
                    .setSelectRectState(
                        ts.selectRectState
                            .setMovingPoint(mouseWindowOffset)
                            .show()
                    )
                ts.offsetNegate
                // x: remove all selections from the current cursor
                sc.getCursorStateMs(ts.cursorId)?.also { cursorMs->
                    cursorMs.value = cursorMs.value.removeAllExceptMainCell()
                }
            }
        }
    }

    override fun endDrag_forTest(wbws: WbWsSt, cellAddress: CellAddress,isCtrPressed: Boolean) {
        forTest(wbws, cellAddress) {
            endDrag(wbws, it.posInWindowOrZero,isCtrPressed)
        }
    }

    override fun endDrag(wbws: WbWsSt, mouseWindowOffset: Offset, isCtrPressed: Boolean) {
        findThumbStateThen(wbws) { thumbStateMs ->
            val ts by thumbStateMs
            if (ts.isShowingSelectedRange) {
                val rect = ts.selectRectState
                thumbStateMs.value = ts
                    .setSelectRectState(
                        rect.deactivate()
                            .hide()
                    )
                val (startCell, endCell) = thumbStateMs.value.getStartEndCells()
                endThumbDragAction.invokeSuitableAction(wbws, startCell, endCell, isCtrPressed)
            }
        }
    }


}
