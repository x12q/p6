package com.qxdzbc.p6.app.communication.event.data_structure.workbook_event

import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookResponse
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.p6.proto.DocProtos
import test.TestSample
import kotlin.test.*

class LoadWorkbookResponseTest{
//    @Test
//    fun t1(){
//        val proto = LoadWorkbookResponseProto.newBuilder()
//            .setIsError(true)
//            .setErrorReport(ErrorHeader("1","2").toErrorReport().toProto())
//            .build()
//        val r = LoadWorkbookResponse.fromProtoBytes(
//            data = proto.toByteString(),
//            translatorGetter = TestSample::mockTranslatorGetter3
//        )
//        assertEquals(proto.isError,r.isError)
//        assertNotNull(r.errorReport)
//        assertTrue { proto.errorReport.toModel().isType(r.errorReport!!) }
//        assertNull(r.workbook)
//    }
//
//    @Test
//    fun t2(){
//        val proto = LoadWorkbookResponseProto.newBuilder()
//            .setIsError(false)
//            .setWorkbook(DocProtos.WorkbookProto.getDefaultInstance())
//            .build()
//        val r = LoadWorkbookResponse.fromProtoBytes(
//            data = proto.toByteString(),
//            translatorGetter = TestSample::mockTranslatorGetter3
//        )
//        assertEquals(proto.isError,r.isError)
//        assertNull(r.errorReport)
//        assertNotNull(r.workbook)
//    }

}
