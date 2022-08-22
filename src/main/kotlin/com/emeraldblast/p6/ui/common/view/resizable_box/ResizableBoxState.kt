package com.emeraldblast.p6.ui.common.view.resizable_box

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emeraldblast.p6.ui.common.compose.LayoutCoorsUtils.ifAttached
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.ms

data class ResizableBoxState(
    val isMouseDownMs: Ms<Boolean> = ms(false),
    val currentMousePosMs: Ms<Offset?> = ms(null),
    val anchorMousePosMs: Ms<Offset?> = ms(null),
    val heightMs: Ms<Dp> = ms(100.dp),
    val widthMs: Ms<Dp> = ms(100.dp),
    val resizeStripWidth: Dp = 15.dp,
    val minHeight: Dp = 50.dp,
    val minWidth: Dp = 50.dp,
    val anchorHeightMs: Ms<Dp> = ms(0.dp),
    var anchorWidthMs: Ms<Dp> = ms(0.dp),
    var thumbLCRight: Ms<LayoutCoordinates?> = ms(null),
    var thumbLCLeft: Ms<LayoutCoordinates?> = ms(null),
    var thumbLCTop: Ms<LayoutCoordinates?> = ms(null),
    var thumbLCBot: Ms<LayoutCoordinates?> = ms(null),
    var boxLc: Ms<LayoutCoordinates?> = ms(null),
    ) {
    companion object{
        val default = ResizableBoxState()
    }
    fun onPress(pte: PointerEvent, dimen: ResizeStyle): ResizableBoxState {
        val lc = getLc(dimen)
        lc.value.ifAttached { l ->
            pte.changes.firstOrNull()?.position?.also {
                anchorMousePosMs.value = l.localToWindow(it)
            }
        }
        isMouseDownMs.value = true
        val anchorSize = getAnchorSize(dimen)
        anchorSize.value = this.getSizeMs(dimen).value
        return this
    }

    fun getAnchorSize(dimen: ResizeStyle): Ms<Dp> {
        return when (dimen) {
            ResizeStyle.__LEFT -> anchorWidthMs
            ResizeStyle.__RIGHT -> anchorWidthMs
            ResizeStyle.__TOP -> anchorHeightMs
            ResizeStyle.__BOT -> anchorHeightMs
        }
    }

    fun onMove(pte: PointerEvent, dimen: ResizeStyle): ResizableBoxState {
        if (this.isMouseDownMs.value) {
            val lc = this.getLc(dimen)
            lc.value.ifAttached { l ->
                pte.changes.firstOrNull()?.position?.also {
                    currentMousePosMs.value = l.localToWindow(it)
                }
            }
            val sizeMs = this.getSizeMs(dimen)
            val minSize = getMinSize(dimen)
            val diff = this.getDiff(dimen)
            val newSize = getAnchorSize(dimen).value + diff
            sizeMs.value = maxOf(newSize, minSize)
        }
        return this
    }

    fun onRelease(): ResizableBoxState {
        this.anchorHeightMs.value = this.heightMs.value
        this.anchorWidthMs.value = this.widthMs.value
        isMouseDownMs.value = false
        return this
    }

    fun getLc(dimen: ResizeStyle): Ms<LayoutCoordinates?> {
        return when (dimen) {
            ResizeStyle.__LEFT -> thumbLCLeft
            ResizeStyle.__RIGHT -> thumbLCRight
            ResizeStyle.__TOP -> thumbLCTop
            ResizeStyle.__BOT -> thumbLCBot
        }
    }

    fun getDiff(dimen: ResizeStyle): Dp {
        val currentMousePos = currentMousePosMs.value
        val anchorMousePos = anchorMousePosMs.value
        if (currentMousePos != null && anchorMousePos != null) {
            return when (dimen) {
                ResizeStyle.__LEFT -> -(currentMousePos.x - anchorMousePos.x)
                ResizeStyle.__RIGHT -> currentMousePos.x - anchorMousePos.x
                ResizeStyle.__TOP -> -(currentMousePos.y - anchorMousePos.y)
                ResizeStyle.__BOT -> currentMousePos.y - anchorMousePos.y
            }.dp
        } else {
            return 0.dp
        }
    }

    fun getSizeMs(dimen: ResizeStyle): Ms<Dp> {
        return when (dimen) {
            ResizeStyle.__LEFT -> widthMs
            ResizeStyle.__RIGHT -> widthMs
            ResizeStyle.__TOP -> heightMs
            ResizeStyle.__BOT -> heightMs
        }
    }

    fun getMinSize(dimen: ResizeStyle): Dp {
        return when (dimen) {
            ResizeStyle.__LEFT -> minWidth
            ResizeStyle.__RIGHT -> minWidth
            ResizeStyle.__TOP -> minHeight
            ResizeStyle.__BOT -> minHeight
        }
    }
}
