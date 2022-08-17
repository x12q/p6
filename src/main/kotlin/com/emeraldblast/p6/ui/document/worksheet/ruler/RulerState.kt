package com.emeraldblast.p6.ui.document.worksheet.ruler

import androidx.compose.ui.layout.LayoutCoordinates
import com.emeraldblast.p6.app.action.common_data_structure.WithWbWs
import com.emeraldblast.p6.ui.common.compose.LayoutCoorWrapper
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St
import com.emeraldblast.p6.ui.document.worksheet.select_rect.SelectRectState
import com.emeraldblast.p6.ui.document.worksheet.slider.GridSlider
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetId

interface RulerState: WithWbWs {
    val wsIdSt:St<WorksheetId>
    fun setWsIdSt(wsIdSt:St<WorksheetId>):RulerState
    val dimen: RulerType
    val sliderMs: Ms<GridSlider>

    val resizerLayoutMap:Map<Int,LayoutCoordinates>
    fun getResizerLayout(itemIndex: Int):LayoutCoordinates?
    fun addResizerLayout(itemIndex: Int,layoutCoordinates: LayoutCoordinates):RulerState
    fun clearResizerLayoutCoorsMap():RulerState

    val rulerLayout: LayoutCoorWrapper?
    fun setLayout(layout: LayoutCoordinates): RulerState
    fun setLayout(layout: LayoutCoorWrapper): RulerState

    val itemSelectRectMs: Ms<SelectRectState>
    val itemSelectRect: SelectRectState get() = itemSelectRectMs.value

    val itemLayoutMap: Map<Int, LayoutCoorWrapper>
    fun addItemLayout(itemIndex: Int, layoutCoordinates: LayoutCoorWrapper): RulerState
    fun clearItemLayoutCoorsMap(): RulerState

    val defaultItemSize:Int
    val itemSizeMap:Map<Int,Int>
    fun changeItemSize(itemIndex: Int, diff:Float): RulerState
    fun setItemSize(itemIndex: Int, size:Int):RulerState
    fun getItemSizeOrDefault(itemIndex: Int):Int
    fun removeItemSize(itemIndex: Int):RulerState
}
