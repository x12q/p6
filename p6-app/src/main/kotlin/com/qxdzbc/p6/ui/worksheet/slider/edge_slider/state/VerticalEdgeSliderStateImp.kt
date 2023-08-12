package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.worksheet.di.comp.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.EdgeSliderUtils
import javax.inject.Inject
import kotlin.math.max

@WsScope
data class VerticalEdgeSliderStateImp(
    val thumbPositionMs: Ms<DpOffset>,
    val thumbLengthRatioMs: Ms<Float>,
) : VerticalEdgeSliderState {

    @Inject
    constructor():this(
        thumbPositionMs=ms(DpOffset.Zero),
        thumbLengthRatioMs=ms(EdgeSliderUtils.maxLength)
    )


    override fun thumbLength(railLength: Dp): Dp {
        return railLength * thumbLengthRatioMs.value
    }

    override fun setThumbLengthRatio(ratio: Float) {
        thumbLengthRatioMs.value = ratio
    }

    override var thumbOffset: DpOffset by thumbPositionMs

    override fun setThumbOffsetWhenDrag(density: Density, dragDelta: Float) {
        val sliderThumbYPx = with(density) {
            max(thumbOffset.y.toPx() + dragDelta,0f)
        }

        val sliderOffset = run {
            val slideThumYOffsetDp = with(density) {
                sliderThumbYPx.toDp()
            }
            DpOffset(0.dp, slideThumYOffsetDp)
        }
        thumbOffset = sliderOffset
    }


}