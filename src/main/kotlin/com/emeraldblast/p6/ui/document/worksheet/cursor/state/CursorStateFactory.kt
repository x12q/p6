package com.emeraldblast.p6.ui.document.worksheet.cursor.state

import com.emeraldblast.p6.ui.common.compose.Ms
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface CursorStateFactory {
    fun create(
        @Assisted("1") idMs: Ms<CursorStateId>,
    ): CursorStateImp
}
