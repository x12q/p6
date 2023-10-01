package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.ScrollBarConstants
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.di.qualifiers.HorizontalWsEdgeSliderStateQualifier
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

/**
 * State for horizontal edge slider. Exist as a singleton under [WsScope]
 */
@WsScope
@HorizontalWsEdgeSliderStateQualifier
@ContributesBinding(scope = WsAnvilScope::class, boundType = ScrollBarState::class)
class HorizontalScrollBarState(
    thumbLengthRatioMs: Ms<Float>,
    thumbPositionRatioMs: Ms<Float>,
    maxLengthRatio: Float,
    minLengthRatio: Float,
    reductionRatio: Float,
    moveBackRatio: Float,
    thumbLayoutCoorMs: Ms<P6LayoutCoor?>,
    railLayoutCoorMs: Ms<P6LayoutCoor?>
) : AbsScrollBarState(
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
        thumbLengthRatioMs = ms(ScrollBarConstants.maxLength),
        maxLengthRatio = ScrollBarConstants.maxLength,
        minLengthRatio = ScrollBarConstants.minLength,
        reductionRatio = ScrollBarConstants.reductionRate,
        moveBackRatio = ScrollBarConstants.moveBackRatio,
        thumbLayoutCoorMs = ms(null),
        railLayoutCoorMs = ms(null),
        thumbPositionRatioMs = ms(ScrollBarConstants.startingThumbPositionRatio)
    )

    init{
        require(maxLengthRatio in 0.0 .. 1.0)
        require(minLengthRatio in 0.0 .. 1.0)
        require(reductionRatio in 0.0 .. 1.0)
        require(moveBackRatio in 0.0 .. 1.0)
        require(thumbLengthRatioMs.value in 0.0 .. 1.0)
        require(thumbPositionRatioMs.value in 0.0 .. 1.0)
    }

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

    override val type: ScrollBarType
        get() = ScrollBarType.Horizontal

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