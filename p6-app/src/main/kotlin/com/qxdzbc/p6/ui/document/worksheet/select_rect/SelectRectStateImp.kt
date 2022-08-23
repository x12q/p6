package com.qxdzbc.p6.ui.document.worksheet.select_rect

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.qxdzbc.common.compose.RectUtils.makeRect

data class SelectRectStateImp(
    override val isShow: Boolean = false,
    override val anchorPoint: Offset = Offset(0F, 0F),
    override val movingPoint: Offset = Offset(0F, 0F),
    override val isActive: Boolean = false
) : SelectRectState {

    override fun show(): SelectRectState {
        if (!this.isShow) {
            return this.copy(isShow = true)
        }
        return this
    }

    override fun hide(): SelectRectState {
        if (this.isShow) {
            return this.copy(isShow=false)
        }
        return this
    }

    override val rect: Rect = makeRect(this.anchorPoint, this.movingPoint)
    override fun setMovingPoint(point: Offset): SelectRectState {
        if(point!=this.movingPoint){
            return this.copy(movingPoint = point)
        }
        return this
    }

    override fun setAnchorPoint(anchorPoint: Offset): SelectRectState {
        if (this.anchorPoint == anchorPoint) {
            return this
        }
        return this.copy(anchorPoint = anchorPoint)
    }

    override fun setActiveStatus(isDown: Boolean): SelectRectState {
        if (this.isActive == isDown) {
            return this
        }
        return this.copy(isActive = isDown)
    }
}
