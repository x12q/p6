package com.qxdzbc.p6.app.action.cursor.on_cursor_changed_reactor

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.window.tool_bar.UpdateFormatIndicator
import com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell.MoveSliderAction
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class, boundType=CommonReactionOnCursorChanged::class)
class CommonReactionOnCursorChangedImp @Inject constructor(
    val stateContainerSt:StateContainer,
    private val makeSliderFollowCellAct: MoveSliderAction,
    val updateFormatIndicator: UpdateFormatIndicator,
) : CommonReactionOnCursorChanged {

    private val sc  = stateContainerSt

    /**
     * This function will do:
     * - update format indicators on toolbar, such as text color selector, cell background color selector to reflect the format state of the main cell of the updated cursor.
     * - make worksheet sliders follow the cursor
     */
    override fun onCursorChanged(cursorId: WbWsSt) {
        sc.getCursorState(cursorId)?.also { cursorState ->
            updateFormatIndicator.updateFormatIndicator(cursorState)
            makeSliderFollowCellAct.makeSliderFollowCell(cursorState,cursorState.mainCell,false)
        }
    }
}
