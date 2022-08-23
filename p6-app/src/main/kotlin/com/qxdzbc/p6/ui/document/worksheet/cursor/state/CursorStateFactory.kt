package com.qxdzbc.p6.ui.document.worksheet.cursor.state

import com.qxdzbc.common.compose.Ms
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface CursorStateFactory {
    fun create(
        @Assisted("1") idMs: Ms<CursorStateId>,
    ): CursorStateImp
}
