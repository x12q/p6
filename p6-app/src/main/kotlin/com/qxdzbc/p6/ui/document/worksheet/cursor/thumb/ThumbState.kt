package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState

interface ThumbState {
    val cursorId: CursorStateId
    val mainCell:CellAddress

    val cellLayoutCoorMap: Map<CellAddress, LayoutCoorWrapper>

    val isShowingSelectedRange:Boolean
    fun setIsShowingSelectedRange(i:Boolean):ThumbState

    val selectedRangeSize:DpSize
    val selectedRangeOffset:Offset


    val selectRectState: SelectRectState
    fun setSelectRectState(i:SelectRectState):ThumbState

}
