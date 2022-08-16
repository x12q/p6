package com.emeraldblast.p6.app.communication.event.data_structure.app_event

import com.emeraldblast.p6.app.common.proto.toProto
import com.emeraldblast.p6.app.action.app.close_wb.CloseWorkbookResponse
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.proto.AppEventProtos.CloseWorkbookResponseProto
import org.mockito.kotlin.mock
import kotlin.test.*

class CloseWorkbookResponseTest {
    val pb = CloseWorkbookResponseProto.newBuilder()
        .setIsError(false)
        .setWorkbookKey(WorkbookKey("WB").toProto())
        .setWindowId("windowId")

    @Test
    fun `fromProtoBytes std case`() {
        val o = CloseWorkbookResponse.fromProtoBytes(pb.build().toByteString())
        assertEquals(pb.isError, o.isError)
        assertEquals(pb.workbookKey.toModel(), o.wbKey)
        assertNull(o.errorReport)
        assertEquals(pb.windowId, o.windowId)
    }

    @Test
    fun `fromProtoBytes null windowId`(){
        val b = this.pb.clearWindowId()
        val o = CloseWorkbookResponse.fromProtoBytes(b.build().toByteString())
        assertNull(o.windowId)
    }

    @Test
    fun `fromProtoBytes error case`(){
        val b = this.pb.setIsError(true).setErrorReport(CommonErrors.ExceptionError.header.toErrorReport().toProto())
        val o = CloseWorkbookResponse.fromProtoBytes(b.build().toByteString())
        assertNotNull(o.errorReport)
        assertTrue { o.isError }
        assertTrue(o.errorReport?.isType(CommonErrors.ExceptionError.header)!!)
    }

    @Test
    fun isLegal(){
        val imap = mapOf(
            CloseWorkbookResponse(isError = false, wbKey = mock(),null,null) to true,
            CloseWorkbookResponse(isError = false, wbKey = mock(),null, mock()) to false,
            CloseWorkbookResponse(isError = true, wbKey = mock(),null,null) to true,
        )
        for((o,v) in imap){
            assertEquals(v, o.isLegal())
        }
    }
}
