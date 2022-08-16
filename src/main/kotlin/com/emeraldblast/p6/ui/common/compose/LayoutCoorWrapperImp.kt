package com.emeraldblast.p6.ui.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.DpSize
import com.emeraldblast.p6.ui.common.compose.LayoutCoorsFunctions.ifAttachedComposable

class LayoutCoorWrapperImp(override val layout: LayoutCoordinates) : LayoutCoorWrapper {
    override val size: DpSize
        get() = layout.size.toDpSize()

    override val boundInWindow: Rect get(){
        if(layout.isAttached){
            return layout.boundsInWindow()
        }else{
            return Rect(Offset(0F,0F), Size.Zero)
        }
    }
    override val posInWindow: Offset get(){
        if(layout.isAttached){
            return layout.positionInWindow()
        }else{
            return Offset(0F,0F)
        }
    }
    override fun localToWindow(local: Offset): Offset {
        return this.layout.localToWindow(local)
    }

    override fun windowToLocal(window: Offset): Offset {
        return this.layout.windowToLocal(window)
    }

    override fun isAttached(): Boolean {
        return layout.isAttached
    }

    override fun ifAttached(f: (lc:LayoutCoorWrapper)->Unit) {
        if(this.layout.isAttached){
            f(this)
        }
    }
    @Composable
    override fun ifAttachedComposable(f:@Composable (lc: LayoutCoorWrapper) -> Unit) {
        if(this.layout.isAttached){
            f(this)
        }
    }
}
