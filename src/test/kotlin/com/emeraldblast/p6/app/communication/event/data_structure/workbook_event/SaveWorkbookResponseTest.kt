package com.emeraldblast.p6.app.communication.event.data_structure.workbook_event

import com.emeraldblast.p6.app.common.proto.toProto
import com.emeraldblast.p6.app.action.app.save_wb.SaveWorkbookResponse
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.common.exception.error.ErrorHeader
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.WorkbookProtos.SaveWorkbookResponseProto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SaveWorkbookResponseTest{
    @Test
    fun fromProtoBytes(){
        val proto = SaveWorkbookResponseProto.newBuilder()
            .setWorkbookKey(WorkbookKey("B").toProto())
            .setPath("folder123/file.txt")
            .setIsError(false)
            .build()

        val o = SaveWorkbookResponse.fromProtoBytes(proto.toByteString())
        assertEquals(proto.workbookKey.toModel(),o.wbKey)
        assertEquals(proto.path, o.path)
        assertEquals(proto.isError, o.isError)
        assertNull(o.errorReport)
    }

    @Test
    fun `fromProtoBytes error case`(){
        val ee = ErrorReport(header= ErrorHeader("S","M"))

        val proto = SaveWorkbookResponseProto.newBuilder()
            .setWorkbookKey(WorkbookKey("B").toProto())
            .setIsError(true)
            .setErrorReport(ee.toProto())
            .build()

        val o = SaveWorkbookResponse.fromProtoBytes(proto.toByteString())
        assertEquals(proto.workbookKey.toModel(),o.wbKey)
        assertEquals(proto.path, o.path)
        assertEquals(proto.isError, o.isError)
        assertTrue { o.errorReport?.isType(ee.header) ?: false}
    }
}
