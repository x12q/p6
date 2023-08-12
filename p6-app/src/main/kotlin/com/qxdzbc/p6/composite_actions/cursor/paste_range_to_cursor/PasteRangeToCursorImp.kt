package com.qxdzbc.p6.composite_actions.cursor.paste_range_to_cursor

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.composite_actions.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class PasteRangeToCursorImp @Inject constructor(
    private val stateCont:StateContainer,
    private val pasteRangeAction: PasteRangeAction,
) : PasteRangeToCursor {

    private val sc  = stateCont

    override fun pasteRange(wbws: WbWs) {
        val cursorState = sc.getCursorState(wbws)
        if (cursorState != null) {
            pasteRangeAction.pasteRange(wbws, cursorState.mainRange ?: RangeAddress(cursorState.mainCell), undo = true)
        }
    }

    override fun pasteRange(wbwsSt: WbWsSt) {
        val cursorState = sc.getCursorState(wbwsSt)
        if (cursorState != null) {
            pasteRangeAction.pasteRange(
                targetWbWsSt=cursorState,
                targetRangeAddress=cursorState.mainRange ?: RangeAddress(cursorState.mainCell),
                undoable = true
            )
        }
    }
}
