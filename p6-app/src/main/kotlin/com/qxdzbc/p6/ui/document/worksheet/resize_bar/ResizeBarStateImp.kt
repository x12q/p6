package com.qxdzbc.p6.ui.document.worksheet.resize_bar

import androidx.compose.ui.geometry.Offset
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType

data class ResizeBarStateImp(
    override val dimen: RulerType,
    override val size: Int,
    override val selectableAreaWidth: Int = P6R.size.value.resizerThickness,
    override val isShow: Boolean = false,
    override val isActive: Boolean = false,
    override val thickness: Int = P6R.size.value.defaultResizeCursorThickness,
    override val position: Offset = Offset(0F,0F),
    override val anchorPoint: Offset = Offset(0F,0F),
    override val isShowThumb: Boolean=false,
) : ResizeBarState {
    override fun changePosition(newPosition: Offset): ResizeBarState {
        if(this.position!=newPosition){
            return this.copy(position = newPosition)
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

    override fun show(): ResizeBarState {
        if(this.isShow){
            return this
        }
        return this.copy(isShow=true)
    }

    override fun hide(): ResizeBarState {
        if(this.isShow){
            return this.copy(isShow = false)
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
        if(anchorPoint!= this.anchorPoint){
            return this.copy(anchorPoint = anchorPoint)
        }
        return this
    }
}
