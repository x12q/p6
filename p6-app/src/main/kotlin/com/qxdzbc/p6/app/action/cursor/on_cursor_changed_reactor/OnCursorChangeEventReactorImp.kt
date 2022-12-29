package com.qxdzbc.p6.app.action.cursor.on_cursor_changed_reactor

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell.MoveSliderAction
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state.TextSizeSelectorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class, boundType=OnCursorChangeEventReactor::class)
class OnCursorChangeEventReactorImp @Inject constructor(
    val stateContainerSt: St<@JvmSuppressWildcards StateContainer>,
    private val makeSliderFollowCellAct: MoveSliderAction
) : OnCursorChangeEventReactor {

    private val sc by stateContainerSt

    override fun onCursorChanged(cursorId: CursorId) {
        sc.getCursorState(cursorId)?.also { cursorState ->
            onCursorChanged(cursorState)
        }
    }

    override fun onCursorChanged(cursorState: CursorState) {
        updateFormatIndicator(cursorState)
        makeSliderFollowCellAct.makeSliderFollowCell(cursorState,cursorState.mainCell,false)
    }

    override fun updateFormatIndicator(cursorState:CursorState){
        val cursorId = cursorState.id
        sc.getCellFormatTable(cursorId)?.also { cft->
            sc.getTextSizeSelectorStateMs(cursorId.wbKey)?.also { textSizeSelectorStateMs ->
                textSizeSelectorStateMs.value = textSizeSelectorStateMs.value.setHeaderText(
                    cft.textSizeTable.getFirstValue(cursorState.mainCell)?.toString() ?: TextSizeSelectorState.defaultHeader
                )
            }

            sc.getTextColorSelectorStateMs(cursorId.wbKey)?.also { textColorSelectorStateMs ->
                textColorSelectorStateMs.value = textColorSelectorStateMs.value.setCurrentColor(
                    cft.textColorTable.getFirstValue(cursorState.mainCell)
                )
            }

            sc.getCellBackgroundColorSelectorStateMs(cursorId.wbKey)?.also {cellBackgroundColorSelectorStateMs->
                cellBackgroundColorSelectorStateMs.value = cellBackgroundColorSelectorStateMs.value.setCurrentColor(
                    cft.cellBackgroundColorTable.getFirstValue(cursorState.mainCell)
                )
            }
        }
    }
}
