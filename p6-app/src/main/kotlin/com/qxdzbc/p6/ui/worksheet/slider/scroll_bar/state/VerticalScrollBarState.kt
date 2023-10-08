package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.common.compose.layout_coor_wrapper.P6Layout
import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.ScrollBarConstants
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForVerticalWsEdgeSlider
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

/**
 * State for vertical edge slider. Exist as a singleton under [WsScope]
 */
@WsScope
@ForVerticalWsEdgeSlider
@ContributesBinding(scope = WsAnvilScope::class, boundType = ScrollBarState::class)
class VerticalScrollBarState(
    thumbLengthRatioMs: Ms<Float>,
    thumbPositionRatioMs: Ms<Float>,
    maxLengthRatio: Float,
    minLengthRatio: Float,
    reductionRatio: Float,
    thumbLayoutCoorMs: Ms<P6Layout?>,
    railLayoutCoorMs: Ms<P6Layout?>
) : AbsScrollBarState(
    thumbLengthRatioMs = thumbLengthRatioMs,
    thumbPositionRatioMs = thumbPositionRatioMs,
    maxLengthRatio = maxLengthRatio,
    minLengthRatio = minLengthRatio,
    reductionRatio = reductionRatio,
    thumbLayoutCoorMs = thumbLayoutCoorMs,
    railLayoutCoorMs = railLayoutCoorMs
) {

    @Inject
    constructor() : this(
        thumbLengthRatioMs = StateUtils.ms(ScrollBarConstants.maxLengthRatio),
        maxLengthRatio = ScrollBarConstants.maxLengthRatio,
        minLengthRatio = ScrollBarConstants.minLength,
        reductionRatio = ScrollBarConstants.reductionRate,
        thumbLayoutCoorMs = StateUtils.ms(null),
        railLayoutCoorMs = StateUtils.ms(null),
        thumbPositionRatioMs = StateUtils.ms(ScrollBarConstants.startingThumbPositionRatio)
    )

    override val thumbStartInParentPx: Float?
        get() = thumbLayoutCoor?.boundInParent?.top

    override val thumbEndInWindowPx: Float?
        get() = thumbLayoutCoor?.boundInWindow?.bottom

    override val thumbStartInWindowPx: Float?
        get() = thumbLayoutCoor?.boundInWindow?.top

    override val railEndInWindowPx: Float?
        get() = railLayoutCoor?.boundInWindow?.bottom

    override val railStartInWindowPx: Float?
        get() = railLayoutCoor?.boundInWindow?.top

    override val type: ScrollBarType = ScrollBarType.Vertical

    override val railLengthPx: Float?
        get() = railLayoutCoor?.boundInWindow?.height

    override fun computeThumbOffset(density: Density): DpOffset {
        val rt = DpOffset(
            x = 0.dp,
            y = with(density) { thumbPositionInPx.toDp() },
        )
        return rt
    }
}