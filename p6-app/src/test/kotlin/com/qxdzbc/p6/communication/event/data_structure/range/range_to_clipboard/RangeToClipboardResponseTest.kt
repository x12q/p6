package com.qxdzbc.p6.communication.event.data_structure.range.range_to_clipboard

import com.qxdzbc.p6.composite_actions.range.range_to_clipboard.RangeToClipboardResponse
import com.qxdzbc.p6.composite_actions.range.RangeIdDM
import com.qxdzbc.p6.composite_actions.range.RangeIdDM.Companion.toModel
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.proto.RangeProtos.RangeToClipboardResponseProto
import kotlin.test.Test
import kotlin.test.assertEquals

class RangeToClipboardResponseTest {
    @Test
    fun fromProtoBytes() {
        val proto = RangeToClipboardResponseProto.newBuilder()
            .setRangeId(
                RangeIdDM(
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
