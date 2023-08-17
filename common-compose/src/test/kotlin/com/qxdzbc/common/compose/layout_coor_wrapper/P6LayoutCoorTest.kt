package com.qxdzbc.common.compose.layout_coor_wrapper

import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor.Companion.replaceWith
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.*

internal class P6LayoutCoorTest{
    @Test
    fun replaceWith(){
        val l1:P6LayoutCoor? = null
        val l2:P6LayoutCoor = mock(){
            whenever(it.forceRefresh()) doReturn it
        }
        val l3 = l1.replaceWith(l2)
        l3 shouldBe l2
        val l4 = l1.replaceWith(l1)
        l4.shouldBeNull()
    }
}
