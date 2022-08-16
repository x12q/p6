package com.emeraldblast.p6.app.communication.event.data_structure.range.range_to_clipboard

import com.emeraldblast.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse
import com.emeraldblast.p6.app.action.range.RangeId
import com.emeraldblast.p6.app.action.range.RangeId.Companion.toModel
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.proto.RangeProtos.RangeToClipboardResponseProto
import kotlin.test.Test
import kotlin.test.assertEquals

class RangeToClipboardResponseTest {
    @Test
    fun fromProtoBytes() {
        val proto = RangeToClipboardResponseProto.newBuilder()
            .setRangeId(
                RangeId(
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
