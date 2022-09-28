package com.qxdzbc.p6.ui.document.worksheet.ruler

import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.p6R
import com.qxdzbc.common.compose.*
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectStateImp
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId

data class RulerStateImp constructor(
    override val wsIdSt:St<WorksheetId>,
    override val type: RulerType,
    override val sliderMs: Ms<GridSlider>,
    override val defaultItemSize: Int = if (type == RulerType.Col) p6R.size.value.defaultColumnWidth else p6R.size.value.defaultRowHeight,
    private val itemLayoutMapMs: Ms<Map<Int, LayoutCoorWrapper>> = ms(emptyMap()),
    override val itemSelectRectMs: Ms<SelectRectState> = ms(SelectRectStateImp()),
    private val rulerLayoutMs: Ms<LayoutCoorWrapper?> = ms(null),
    private val itemSizeMapMs: Ms<Map<Int, Int>> = ms(emptyMap()),
    override val resizerLayoutMap: Map<Int, LayoutCoordinates> = emptyMap(),
) : RulerState {
    override val wsId: WorksheetId
        get() = wsIdSt.value

    override fun setWsIdSt(wsIdSt: St<WorksheetId>): RulerState {
        return this.copy(wsIdSt=wsIdSt)
    }

    override fun getResizerLayout(itemIndex: Int): LayoutCoordinates? {
        return this.resizerLayoutMap[itemIndex]
    }

    override fun addResizerLayout(itemIndex: Int, layoutCoordinates: LayoutCoordinates): RulerState {
        return this.copy(resizerLayoutMap = resizerLayoutMap + (itemIndex to layoutCoordinates))
    }

    override fun clearResizerLayoutCoorsMap(): RulerState {
        if(this.resizerLayoutMap.isNotEmpty()){
            return this.copy(resizerLayoutMap = emptyMap())
        }
        return this
    }

    override val rulerLayout: LayoutCoorWrapper? by rulerLayoutMs
    override fun setLayout(layout: LayoutCoordinates): RulerState {
        rulerLayoutMs.value = layout.wrap()
        return this
    }

    override fun setLayout(layout: LayoutCoorWrapper): RulerState {
        rulerLayoutMs.value = layout
        return this
    }

    override val itemLayoutMap: Map<Int, LayoutCoorWrapper> by itemLayoutMapMs
    override fun addItemLayout(itemIndex: Int, layoutCoordinates: LayoutCoorWrapper): RulerState {
        this.itemLayoutMapMs.value = this.itemLayoutMap + (itemIndex to layoutCoordinates)
        return this
    }

    override fun clearItemLayoutCoorsMap(): RulerState {
        if (this.itemLayoutMap.isNotEmpty()) {
            this.itemLayoutMapMs.value = emptyMap()
        }
        return this
    }

    override val itemSizeMap: Map<Int, Int> by this.itemSizeMapMs

    override fun changeItemSize(itemIndex: Int, diff: Float): RulerState {
        val sd = diff.toInt()
        if (sd != 0) {
            val oldSize = this.getItemSizeOrDefault(itemIndex)
            val newSize = oldSize + sd
            if (newSize == this.defaultItemSize) {
                return this.removeItemSize(itemIndex)
            } else {
                this.itemSizeMapMs.value = this.itemSizeMap + (itemIndex to newSize)
            }
        }
        return this
    }

    override fun setItemSize(itemIndex: Int, size: Int): RulerState {
        this.itemSizeMapMs.value = this.itemSizeMap + (itemIndex to size)
        return this
    }

    override fun getItemSizeOrDefault(itemIndex: Int): Int {
        return itemSizeMap[itemIndex] ?: this.defaultItemSize
    }

    override fun removeItemSize(itemIndex: Int): RulerState {
        if (itemIndex in itemSizeMap.keys) {
            itemSizeMapMs.value = itemSizeMap - itemIndex
        }
        return this
    }

    override val wbKeySt: St<WorkbookKey>
        get() = wsId.wbKeySt
    override val wsNameSt: St<String>
        get() = wsId.wsNameSt

    override val wbKey: WorkbookKey
        get() = wsId.wbKey
    override val wsName: String
        get() = wsId.wsName
}
