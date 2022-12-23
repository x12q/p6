package com.qxdzbc.p6.ui.document.worksheet.cursor.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface CursorStateFactory {
    fun create(
        @Assisted("1") idMs: Ms<CursorId>,
        @Assisted("2") cellLayoutCoorsMapSt:St<Map<CellAddress, LayoutCoorWrapper>>,
        @Assisted("3") thumbStateMs: Ms<ThumbState>,
        @Assisted("4") mainCellMs: Ms<CellAddress>
    ): CursorStateImp
}
