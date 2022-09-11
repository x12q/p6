package com.qxdzbc.p6.app.communication.event.data_structure.app_event

import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.AppProtos
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto
import test.TestSample
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CreateNewWorkbookResponseTest {
//    @Test
//    fun `fromProtoByte windowId valid`() {
//        val proto = AppProtos.CreateNewWorkbookResponseProto.newBuilder()
//            .setWindowId("abc")
//            .build()
//
//        val o = CreateNewWorkbookResponse.fromProtoBytes(
//            data = proto.toByteString(),
//        )
//        assertNull(o.errorReport)
//        assertNull(o.wbKey)
//        assertEquals("abc", o.windowId)
//    }

//    @Test
//    fun `fromProtoByte ok`() {
//        val proto = AppProtos.CreateNewWorkbookResponseProto.newBuilder()
//            .setWbKey(WorkbookKey("wbK").toProto())
//            .build()
//
//        val o = CreateNewWorkbookResponse.fromProtoBytes(
//            data = proto.toByteString(),
//        )
//        assertNull(o.errorReport)
//        assertNotNull(o.wbKey)
//        assertNull(o.windowId)
//    }

//    @Test
}
