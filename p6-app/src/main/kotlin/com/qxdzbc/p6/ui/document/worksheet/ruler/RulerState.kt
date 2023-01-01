package com.qxdzbc.p6.ui.document.worksheet.ruler

import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId

interface RulerState: WbWsSt,RulerSig {
    val wsIdSt:St<WorksheetId>
    val wsId:WorksheetId
    fun setWsIdSt(wsIdSt:St<WorksheetId>):RulerState
    override val type: RulerType
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

    /**
     * Width or height of items in this ruler state.
     * If this ruler state server a column ruler, this map contains the items' height.
     * If this ruler state server a row ruler, this map contains the items' width.
     */
    val itemSizeMap:Map<Int,Int>
    fun changeItemSize(itemIndex: Int, diff:Float): RulerState
    fun setItemSize(itemIndex: Int, size:Int):RulerState
    fun setMultiItemSize(itemMap:Map<Int,Int>):RulerState
    fun getItemSizeOrDefault(itemIndex: Int):Int
    fun removeItemSize(itemIndex: Int):RulerState
}
