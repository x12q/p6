package com.qxdzbc.common.compose.layout_coor_wrapper

import com.qxdzbc.common.compose.layout_coor_wrapper.P6Layout.Companion.replaceWith
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.*

internal class P6LayoutTest{
    @Test
    fun replaceWith(){
        val l1:P6Layout? = null
        val l2:P6Layout = mock(){
            whenever(it.forceRefresh()) doReturn it
        }
        val l3 = l1.replaceWith(l2)
        l3 shouldBe l2
        val l4 = l1.replaceWith(l1)
        l4.shouldBeNull()
    }
}
