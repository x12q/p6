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

    override val isMovingDownward: Boolean
        get() = movingPoint.y > anchorPoint.y

    override fun isMovingDownward(ratio: Double): Boolean {
        if(isMovingDownward){
            val r = rect
            return r.width/r.height <= ratio
        }else{
            return false
        }
    }

    override val isMovingUpward: Boolean
        get() = movingPoint.y < anchorPoint.y

    override fun isMovingUpward(ratio: Double): Boolean {
        if(isMovingUpward){
            val r = rect
            return r.width/r.height <= ratio
        }else{
            return false
        }
    }

    override val isMovingToTheLeft: Boolean
        get() = movingPoint.x < anchorPoint.x

    override fun isMovingToTheLeft(ratio: Double): Boolean {
        if(isMovingToTheLeft){
            val r = rect
            return r.height/r.width <= ratio
        }else{
            return false
        }
    }

    override val isMovingToTheRight: Boolean
        get() = movingPoint.x > anchorPoint.x

    override fun isMovingToTheRight(ratio: Double): Boolean {
        if(isMovingToTheRight){
            val r = rect
            return r.height/r.width <= ratio
        }else{
            return false
        }
    }

}
