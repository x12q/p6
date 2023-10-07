package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state

import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.layout_coor_wrapper.P6Layout
import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.ScrollBarConstants
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForHorizontalWsEdgeSlider
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

/**
 * State for horizontal edge slider. Exist as a singleton under [WsScope]
 */
@WsScope
@ForHorizontalWsEdgeSlider
@ContributesBinding(scope = WsAnvilScope::class, boundType = ScrollBarState::class)
class HorizontalScrollBarState(
    thumbLengthRatioMs: Ms<Float>,
    thumbPositionRatioMs: Ms<Float>,
    maxLengthRatio: Float,
    minLengthRatio: Float,
    reductionRatio: Float,
    moveBackRatio: Float,
    thumbLayoutCoorMs: Ms<P6Layout?>,
    railLayoutCoorMs: Ms<P6Layout?>
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

    init {
        require(maxLengthRatio in 0.0..1.0)
        require(minLengthRatio in 0.0..1.0)
        require(reductionRatio in 0.0..1.0)
        require(moveBackRatio in 0.0..1.0)
        require(thumbLengthRatioMs.value in 0.0..1.0)
        require(thumbPositionRatioMs.value in 0.0..1.0)
    }

    override val thumbStartInParentPx: Float?
        get() = thumbLayoutCoor?.boundInParent?.left

    override val thumbEndInWindowPx: Float?
        get() = thumbLayoutCoor?.boundInWindow?.right

    override val thumbStartInWindowPx: Float?
        get() = thumbLayoutCoor?.boundInWindow?.left

    override val railEndInWindowPx: Float?
        get() = railLayoutCoor?.boundInWindow?.right

    override val railStartInWindowPx: Float?
        get() = railLayoutCoor?.boundInWindow?.left

    override val type: ScrollBarType = ScrollBarType.Horizontal

    override val railLengthPx: Float?
        get() = railLayoutCoor?.boundInWindow?.width

    override fun computeThumbOffset(density: Density): DpOffset {
        val rt = DpOffset(
            x = with(density) {
                thumbPositionInPx.toDp()
            },
            y = 0.dp,
        )
        return rt
    }
}