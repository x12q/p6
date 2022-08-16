package com.emeraldblast.p6.ui.common.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertEquals
import org.junit.Test

class OtherComposeFunctionsKtTest {

    @Test
    fun testMakeRect() {
        val p1 = Offset(5F,8F)
        val p2 = Offset(30F,20F)
        val r12 = makeRect(p1,p2)
        assertEquals(Rect(p1,p2),r12)

        val p3 = Offset(3F,20F)
        assertEquals(Rect(Offset(3F,8F),Offset(5F,20F)), makeRect(p1,p3))


        val p4 = Offset(7F,4F)
        assertEquals(Rect(
            Offset(5F,4F),
            Offset(7F,8F)
        ), makeRect(p1,p4))

        val p5 = Offset(1F,4F)
        assertEquals(Rect(p5,p1), makeRect(p1,p5))
    }
}
