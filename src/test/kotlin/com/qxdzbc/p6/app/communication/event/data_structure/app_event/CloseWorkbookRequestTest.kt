package com.qxdzbc.p6.app.communication.event.data_structure.app_event

import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.AppEventProtos.CloseWorkbookRequestProto
import kotlin.test.Test
import kotlin.test.assertEquals

class CloseWorkbookRequestTest {

    @Test
    fun toProtoBytes() {
        val i = CloseWorkbookRequest(
            windowId = "windowId",
            wbKey = WorkbookKey("Wb")
        )

        assertEquals(CloseWorkbookRequestProto.newBuilder().setWindowId("windowId").setWorkbookKey(i.wbKey.toProto()).build().toByteString(),i.toProtoBytes())
    }
}
