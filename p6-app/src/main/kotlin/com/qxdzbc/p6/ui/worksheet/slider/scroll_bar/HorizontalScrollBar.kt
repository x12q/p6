package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.HSpacer
import com.qxdzbc.common.compose.view.testApp
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.GridSliderImp
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.HorizontalScrollBarState


/**
 * [ScrollBar] but on the horizontal axis.
 */
@Composable
fun HorizontalScrollBar(
    state: ScrollBarState,
    railModifier: Modifier = Modifier,
    thumbModifier: Modifier = Modifier,
    onDrag: (data:OnDragThumbData) -> Unit,
    onClickOnRail: (clickPositionRatio: Float) -> Unit,
    allowComputationAtBottom: Boolean,
) {
    ScrollBar(
        state,railModifier,thumbModifier,onDrag,onClickOnRail,allowComputationAtBottom
    )
}

@Preview
@Composable
fun Preview_HorizontalBar() {

    val sliderState: Ms<GridSlider> = rms(GridSliderImp.forPreview())
    val state = remember {
        HorizontalScrollBarState()
    }

    var dragRatio: OnDragThumbData? by rms(null)
    var clickRatio: Float? by rms(null)

    Column {
        HorizontalScrollBar(
            state = state,
            onDrag = {
                dragRatio = it
            },
            onClickOnRail = {
                clickRatio = it
            },
            allowComputationAtBottom = true,
        )
        HSpacer(50.dp)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Click ratio: ${clickRatio}")

            Text("Drag ratio: ${dragRatio}")

            Text("Thumb position ratio: ${state.thumbPositionRatio}")

            Text("${sliderState}")
        }

    }

}

fun main() {
    testApp {
        Preview_HorizontalBar()
    }
}