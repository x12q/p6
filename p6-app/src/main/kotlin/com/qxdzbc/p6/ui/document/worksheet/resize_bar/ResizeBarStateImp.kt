package com.qxdzbc.p6.ui.document.worksheet.resize_bar

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.qxdzbc.p6.ui.document.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType

data class ResizeBarStateImp(
    override val rulerType: RulerType,
    override val thumbSize: Dp,
    override val selectableAreaWidth: Dp = WorksheetConstants.resizerThickness,
    override val isShowBar: Boolean = false,
    override val isActive: Boolean = false,
    override val thickness: Dp = WorksheetConstants.defaultResizeCursorThickness,
    override val offset: Offset = Offset(0F,0F),
    override val anchorPointOffset: Offset = Offset(0F,0F),
    override val isShowThumb: Boolean=false,
) : ResizeBarState {

    override fun changePosition(newPosition: Offset): ResizeBarState {
        if(this.offset!=newPosition){
            return this.copy(offset = newPosition)
        }
        return this
    }

    override fun showThumb(): ResizeBarState {
        if(!isShowThumb){
            return this.copy(isShowThumb=true)
        }
        return this
    }

    override fun hideThumb(): ResizeBarState {
        if(isShowThumb){
            return this.copy(isShowThumb = false)
        }
        return this
    }

    override fun showBar(): ResizeBarState {
        if(this.isShowBar){
            return this
        }
        return this.copy(isShowBar=true)
    }

    override fun hideBar(): ResizeBarState {
        if(this.isShowBar){
            return this.copy(isShowBar = false)
        }
        return this
    }

    override fun activate(): ResizeBarState {
        if(!this.isActive){
            return this.copy(isActive =true)
        }
        return this
    }

    override fun deactivate(): ResizeBarState {
        if(this.isActive){
            return this.copy(isActive=false)
        }
        return this
    }

    override fun setAnchor(anchorPoint: Offset): ResizeBarState {
        if(anchorPoint!= this.anchorPointOffset){
            return this.copy(anchorPointOffset = anchorPoint)
        }
        return this
    }
}
