package com.qxdzbc.common.compose

import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.ColorUtils.getContrastColor
import io.kotest.matchers.shouldBe
import kotlin.test.*

internal class ColorUtilsTest {

    @Test
    fun findContrast() {
        Color.Black.getContrastColor() shouldBe Color.White
    }
}
