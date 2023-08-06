package com.qxdzbc.p6.ui.document.worksheet.select_rect

import androidx.compose.ui.geometry.Offset
import kotlin.test.*

internal class SelectRectStateImpTest{
    @Test
    fun movingFlags(){
        val s1 = SelectRectStateImp(
            false,false, Offset.Zero,Offset.Zero
        ).copy(
            anchorPoint = Offset(3F,4F),
            movingPoint = Offset(3F,5F)
        )
        assertTrue(s1.isMovingDownward)
        assertTrue(s1.setMovingPoint(Offset(3F,1F)).isMovingUpward)
        assertTrue(s1.setMovingPoint(Offset(2F,4F)).isMovingToTheLeft)
        assertTrue(s1.setMovingPoint(Offset(6F,4F)).isMovingToTheRight)
    }
}
