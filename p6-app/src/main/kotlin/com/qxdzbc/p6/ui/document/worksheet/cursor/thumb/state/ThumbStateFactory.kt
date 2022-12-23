package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state

import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ThumbStateFactory {
    fun create(
        @Assisted("1") cursorIdSt: St<CursorId>,
        @Assisted("2") mainCellSt: St<CellAddress>,
        @Assisted("3") cellLayoutCoorMapSt: St<Map<CellAddress, LayoutCoorWrapper>>,
    ): ThumbStateImp
}
