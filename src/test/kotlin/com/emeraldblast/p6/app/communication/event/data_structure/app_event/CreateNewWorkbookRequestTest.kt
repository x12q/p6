package com.emeraldblast.p6.app.communication.event.data_structure.app_event

import com.emeraldblast.p6.app.communication.event.P6Events
import com.emeraldblast.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class CreateNewWorkbookRequestTest {

    @Test
    fun toProto() {
        val i = CreateNewWorkbookRequest("wid")
        val o = i.toProto()
        assertEquals(i.windowId,o.windowId)

        val i2 = CreateNewWorkbookRequest(null)
        val o2 = i2.toProto()
        assertFalse(o2.hasWindowId())
    }

    @Test
    fun toP6Msg(){
        val i = CreateNewWorkbookRequest("wid")
        val msg = i.toP6Msg()
        assertEquals(P6Events.App.CreateNewWorkbook.event, msg.event)
        assertEquals(i.toProto().toByteString(), msg.data)
    }
}
