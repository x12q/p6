package com.qxdzbc.p6.ui.worksheet.ruler

import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Dp
import com.qxdzbc.common.compose.layout_coor_wrapper.P6Layout
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.ui.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.state.WorksheetId

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

    val rulerLayout: P6Layout?
    fun setLayout(layout: LayoutCoordinates): RulerState
    fun setLayout(layout: P6Layout): RulerState

    val itemSelectRectMs: Ms<SelectRectState>
    val itemSelectRect: SelectRectState get() = itemSelectRectMs.value

    val itemLayoutMap: Map<Int, P6Layout>
    fun addItemLayout(itemIndex: Int, layoutCoordinates: P6Layout): RulerState
    fun clearItemLayoutCoorsMap(): RulerState

    val defaultItemSize: Dp

    /**
     * Width or height of items in this ruler state.
     * If this ruler state server a column ruler, this map contains the items' height.
     * If this ruler state server a row ruler, this map contains the items' width.
     */
    val itemSizeMap: Map<Int, Dp>
//    fun changeItemSize(itemIndex: Int, diff:Float): RulerState
    fun getItemSizeOrDefault(itemIndex: Int): Dp
    fun removeItemSize(itemIndex: Int):RulerState

    fun setMultiItemSize(itemMap: Map<Int, Dp>): RulerState
    fun setItemSize(itemIndex: Int, size: Dp): RulerState
}
