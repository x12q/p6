package com.qxdzbc.p6.ui.worksheet.slider.edge_slider

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.EdgeSliderState
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.VerticalEdgeSliderState


/**
 * [ScrollBar] but on the vertical axis.
 */
@Composable
fun VerticalScrollBar(
    state: EdgeSliderState,
    railModifier: Modifier = Modifier,
    thumbModifier: Modifier = Modifier,
    onDrag: (OnDragThumbData) -> Unit,
    onClickOnRail: (clickPositionRatio: Float) -> Unit,
    allowComputationAtBot: () -> Boolean = { true },
) {
    ScrollBar(
        state,railModifier,thumbModifier,onDrag,onClickOnRail,allowComputationAtBot
    )
}

@Preview
@Composable
fun Preview_VerticalScrollBar() {

    val sliderState: Ms<GridSlider> = rms(GridSliderImp.forPreview())
    val state = remember {
        VerticalEdgeSliderState()
    }

    var dragRatio: OnDragThumbData? by rms(null)
    var clickRatio: Float? by rms(null)

    Row {
        VerticalScrollBar(
            state = state,
            onDrag = {
                dragRatio = it
            },
            onClickOnRail = {
                clickRatio = it
            }
        )
        HSpacer(50.dp)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Click ratio: ${clickRatio}")

            Text("Drag ratio: ${dragRatio}")

            Text("Thumb position ratio: ${state.thumbPositionRatio}")

            Text("Thumb scroll ratio: ${state.thumbScrollRatio}")


        }

    }

}

fun main() {
    testApp {
        Preview_VerticalScrollBar()
    }
}