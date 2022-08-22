package com.qxdzbc.p6.app.communication.event.data_structure.workbook_event

import com.qxdzbc.p6.app.communication.event.P6Events
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookRequest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoadWorkbookRequestTest {

    @Test
    fun toP6Message() {
        val req = LoadWorkbookRequest("abc","")
        val p6Msg = req.toP6Msg()
        assertEquals(P6Events.App.LoadWorkbook.event, p6Msg.header.eventType)
        assertEquals(p6Msg.data,req.toProto().toByteString())
    }
}
