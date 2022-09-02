package com.qxdzbc.p6.app.communication.event.data_structure.range.range_to_clipboard

import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.action.range.RangeIdImp.Companion.toModel
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.RangeProtos.RangeToClipboardResponseProto
import kotlin.test.Test
import kotlin.test.assertEquals

class RangeToClipboardResponseTest {
    @Test
    fun fromProtoBytes() {
        val proto = RangeToClipboardResponseProto.newBuilder()
            .setRangeId(
                RangeIdImp(
                    rangeAddress = RangeAddress("C1:J2"),
                    wbKey = WorkbookKey("bb"),
                    wsName = "QWE"
                ).toProto()
            ).setWindowId("windowId")
            .build()
        val m = RangeToClipboardResponse.fromProtoBytes(proto.toByteString())
        assertEquals(proto.windowId, m.windowId)
        assertEquals(proto.rangeId.toModel(), m.rangeId)
    }
}
