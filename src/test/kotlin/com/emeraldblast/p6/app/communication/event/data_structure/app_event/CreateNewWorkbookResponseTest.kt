package com.emeraldblast.p6.app.communication.event.data_structure.app_event

import com.emeraldblast.p6.app.common.proto.ProtoUtils.toProto
import com.emeraldblast.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.proto.AppEventProtos.CreateNewWorkbookResponseProto
import com.emeraldblast.p6.proto.DocProtos.WorkbookProto
import test.TestSample
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CreateNewWorkbookResponseTest {
    @Test
    fun `fromProtoByte windowId valid`() {
        val proto = CreateNewWorkbookResponseProto.newBuilder()
            .setIsError(false)
            .setWindowId("abc")
            .build()

        val o = CreateNewWorkbookResponse.fromProtoBytes(
            data = proto.toByteString(),
            translatorGetter = TestSample::mockTranslatorGetter3
        )
        assertEquals(false, o.isError)
        assertNull(o.errorReport)
        assertNull(o.workbook)
        assertEquals("abc", o.windowId)
    }

    @Test
    fun `fromProtoByte ok`() {
        val proto = CreateNewWorkbookResponseProto.newBuilder()
            .setIsError(false)
            .setWorkbook(WorkbookProto.newBuilder().build())
            .build()

        val o = CreateNewWorkbookResponse.fromProtoBytes(
            data = proto.toByteString(),
            translatorGetter = TestSample::mockTranslatorGetter3
        )
        assertEquals(false, o.isError)
        assertNull(o.errorReport)
        assertNotNull(o.workbook)
        assertNull(o.windowId)
    }

    @Test
    fun `fromProtoByte error case`() {
        val er = CommonErrors.Unknown.header.toErrorReport()
        val proto = CreateNewWorkbookResponseProto.newBuilder()
            .setIsError(true)
            .setErrorReport(er.toProto())
            .build()

        val o = CreateNewWorkbookResponse.fromProtoBytes(
            data = proto.toByteString(),
            translatorGetter = TestSample::mockTranslatorGetter3
        )
        assertEquals(true, o.isError)
        assertNotNull(o.errorReport)
        assertNull(o.workbook)
        assertNull(o.windowId)
    }
}
