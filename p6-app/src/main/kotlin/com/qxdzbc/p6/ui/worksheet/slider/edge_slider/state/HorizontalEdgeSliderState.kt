package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.p6.ui.worksheet.di.comp.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.comp.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.EdgeSliderUtils
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.di.qualifiers.HorizontalWsEdgeSliderStateQualifier
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

/**
 * State for horizontal edge slider. Exist as a singleton under [WsScope]
 */
@WsScope
@HorizontalWsEdgeSliderStateQualifier
@ContributesBinding(scope = WsAnvilScope::class, boundType = EdgeSliderState::class)
class HorizontalEdgeSliderState(
    thumbLengthRatioMs: Ms<Float>,
    thumbPositionRatioMs: Ms<Float>,
    maxLengthRatio: Float,
    minLengthRatio: Float,
    reductionRatio: Float,
    moveBackRatio: Float,
    thumbLayoutCoorMs: Ms<P6LayoutCoor?>,
    railLayoutCoorMs: Ms<P6LayoutCoor?>
) : AbsEdgeSliderState(
    thumbLengthRatioMs = thumbLengthRatioMs,
    thumbPositionRatioMs = thumbPositionRatioMs,
    maxLengthRatio = maxLengthRatio,
    minLengthRatio = minLengthRatio,
    reductionRatio = reductionRatio,
    moveBackRatio = moveBackRatio,
    thumbLayoutCoorMs = thumbLayoutCoorMs,
    railLayoutCoorMs = railLayoutCoorMs
) {

    @Inject
    constructor() : this(
        thumbLengthRatioMs = StateUtils.ms(EdgeSliderUtils.maxLength),
        maxLengthRatio = EdgeSliderUtils.maxLength,
        minLengthRatio = EdgeSliderUtils.minLength,
        reductionRatio = EdgeSliderUtils.reductionRate,
        moveBackRatio = EdgeSliderUtils.moveBackRatio,
        thumbLayoutCoorMs = StateUtils.ms(null),
        railLayoutCoorMs = StateUtils.ms(null),
        thumbPositionRatioMs = StateUtils.ms(EdgeSliderUtils.startingThumbPositionRatio)
    )

    override val thumbStartInParentPx: Float?
        get() = thumbLayoutCoor?.layout?.boundsInParent()?.left

    override val railEndInWindowPx: Float?
        get() = railLayoutCoor?.layout?.boundsInWindow()?.right

    override val thumbEndInWindowPx: Float?
        get() = thumbLayoutCoor?.layout?.boundsInWindow()?.right

    override val thumbStartInWindowPx: Float?
        get() = thumbLayoutCoor?.layout?.boundsInWindow()?.left

    override val railStartInWindowPx: Float?
        get() = railLayoutCoor?.layout?.boundsInWindow()?.left

    override val type: EdgeSliderType
        get() = EdgeSliderType.Horizontal

    override val railLengthPx: Float?
        get() = railLayoutCoor?.layout?.boundsInWindow()?.width

    override fun computeThumbOffset(density: Density): DpOffset {
        val rt= DpOffset(
            y= 0.dp,
            x = with(density) { thumbPositionInPx.toDp() },
        )
        return rt
    }
}