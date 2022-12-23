package com.qxdzbc.p6.app.action.cursor.propagate_move_cursor_event

import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CommonPropagateMoveCursorEventImp @Inject constructor(

) : PropagateMoveCursorEvent {
    override fun propagateMoveCursorEvent(cursorId: CursorId) {

    }
}
