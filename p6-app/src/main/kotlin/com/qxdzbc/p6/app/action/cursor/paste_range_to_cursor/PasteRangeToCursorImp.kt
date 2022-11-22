package com.qxdzbc.p6.app.action.cursor.paste_range_to_cursor

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class PasteRangeToCursorImp @Inject constructor(
    private val stateContSt:St<@JvmSuppressWildcards StateContainer>,
    private val pasteRangeAction: PasteRangeAction,
)  : PasteRangeToCursor {
    private val sc by stateContSt
    override fun pasteRange(wbws: WbWs) {
        val cursorState = sc.getCursorState(wbws)
        if(cursorState!=null){
            pasteRangeAction.pasteRange(wbws,cursorState.mainRange ?: RangeAddress(cursorState.mainCell),)
        }
    }

    override fun pasteRange(wbwsSt: WbWsSt) {
        val cursorState = sc.getCursorState(wbwsSt)
        if(cursorState!=null){
            pasteRangeAction.pasteRange(cursorState,cursorState.mainRange ?: RangeAddress(cursorState.mainCell),)
        }
    }
}
