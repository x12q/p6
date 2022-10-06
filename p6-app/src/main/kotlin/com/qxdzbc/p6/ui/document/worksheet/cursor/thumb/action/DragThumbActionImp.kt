package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action

import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbState
import javax.inject.Inject

class DragThumbActionImp @Inject constructor(
    @StateContainerSt
    val scSt: St<@JvmSuppressWildcards StateContainer>
) : DragThumbAction {
    val sc by scSt

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
            }
        }
    }

    override fun endDrag_forTest(wbws: WbWsSt, cellAddress: CellAddress) {
        forTest(wbws, cellAddress) {
            endDrag(wbws, it.posInWindowOrZero)
        }
    }

    override fun endDrag(wbws: WbWsSt, mouseWindowOffset: Offset) {
        findThumbStateThen(wbws) { thumbStateMs ->
            val ts by thumbStateMs
            if (ts.isShowingSelectedRange) {
                val rect = ts.selectRectState
                thumbStateMs.value = ts
                    .setSelectRectState(
                        rect.deactivate()
                            .hide()
                    )
            }
        }
    }
}
