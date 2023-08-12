package com.qxdzbc.p6.communication.event.data_structure.app_event

import com.qxdzbc.p6.composite_actions.app.create_new_wb.CreateNewWorkbookRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class CreateNewWorkbookRequestTest {

    @Test
    fun toProto() {
        val i = CreateNewWorkbookRequest("wid",)
        val o = i.toProto()
        assertEquals(i.windowId,o.windowId)

        val i2 = CreateNewWorkbookRequest(null)
        val o2 = i2.toProto()
        assertFalse(o2.hasWindowId())
    }
}
